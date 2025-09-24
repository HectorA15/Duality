package duality.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import duality.model.Restriction;
import duality.model.Points;
import duality.util.EquationParser;
import duality.util.Intersection;

import java.util.ArrayList;
import java.util.List;

public class MainView {

    private final List<TextField> restrictionFields = new ArrayList<>();
    private final TextArea outputArea = new TextArea();

    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Calculadora de Utilidad Máxima (JavaFX)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Función objetivo
        HBox objetivoBox = new HBox(5, new Label("Función objetivo:"),
                new TextField(), new Label("x +"),
                new TextField(), new Label("y"));
        objetivoBox.setPadding(new Insets(5));

        // Restricciones
        VBox restriccionesBox = new VBox(5);
        restriccionesBox.setPadding(new Insets(5));
        Label restriccionesLabel = new Label("Restricciones:");
        Button addRestrictionBtn = new Button("Agregar restricción");
        addRestrictionBtn.setOnAction(e -> addRestrictionField(restriccionesBox));
        restriccionesBox.getChildren().add(addRestrictionBtn);

        // Output
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(8);

        // Botón calcular
        Button calcularBtn = new Button("Calcular Intersecciones");
        calcularBtn.setOnAction(e -> calcularRestricciones());

        root.getChildren().addAll(title, objetivoBox, restriccionesLabel, restriccionesBox, calcularBtn, outputArea);

        // Al inicio, agrega dos restricciones
        addRestrictionField(restriccionesBox);
        addRestrictionField(restriccionesBox);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Duality - JavaFX");
        stage.show();
    }

    private void addRestrictionField(VBox restriccionesBox) {
        HBox row = new HBox(5);
        TextField restrictionField = new TextField();
        restrictionField.setPromptText("Ej: 2x+3y=12");
        Button removeBtn = new Button("Eliminar");
        removeBtn.setOnAction(e -> {
            restriccionesBox.getChildren().remove(row);
            restrictionFields.remove(restrictionField);
        });
        row.getChildren().addAll(restrictionField, removeBtn);
        restriccionesBox.getChildren().add(row);
        restrictionFields.add(restrictionField);
    }

    private void calcularRestricciones() {
        outputArea.clear();
        List<Restriction> restricciones = new ArrayList<>();
        for (TextField tf : restrictionFields) {
            Restriction r = EquationParser.parse(tf.getText());
            if (r != null) restricciones.add(r);
        }
        List<Points> intersecciones = Intersection.todasIntersecciones(restricciones);

        if (intersecciones.isEmpty()) {
            outputArea.setText("No hay puntos de intersección válidos.");
        } else {
            StringBuilder sb = new StringBuilder("Intersecciones encontradas:\n");
            int i = 1;
            for (Points p : intersecciones) {
                sb.append(String.format("T%d: %s\n", i++, p));
            }
            outputArea.setText(sb.toString());
        }
    }
}