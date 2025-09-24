package duality.controller;

import duality.model.Restriction;
import duality.model.Points;
import duality.util.EquationParser;
import duality.util.Intersection;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    @FXML private TextField objXField;
    @FXML private TextField objYField;
    @FXML private VBox restrictionsBox;
    @FXML private Button addBtn;
    @FXML private Button centerBtn;
    @FXML private Canvas plotCanvas;
    @FXML private TextArea outputArea;

    private final List<TextField> restrictionFields = new ArrayList<>();

    @FXML
    public void initialize() {
        // Agrega una restricción por defecto
        addRestrictionField();
        // Listeners para actualización en vivo
        objXField.textProperty().addListener((obs, oldV, newV) -> updatePlotAndOutput());
        objYField.textProperty().addListener((obs, oldV, newV) -> updatePlotAndOutput());
        addBtn.setOnAction(e -> addRestrictionField());
        centerBtn.setOnAction(e -> centerPlot());
    }

    private void addRestrictionField() {
        HBox row = new HBox(6);
        TextField restrictionField = new TextField();
        restrictionField.setPromptText("Ej: 2x+3y=12");
        restrictionField.setPrefWidth(140);
        Button removeBtn = new Button("-");
        removeBtn.getStyleClass().add("circle-red");
        removeBtn.setOnAction(e -> {
            restrictionsBox.getChildren().remove(row);
            restrictionFields.remove(restrictionField);
            updatePlotAndOutput();
        });
        restrictionField.textProperty().addListener((obs, oldV, newV) -> updatePlotAndOutput());

        row.getChildren().addAll(restrictionField, removeBtn);
        restrictionsBox.getChildren().add(row);
        restrictionFields.add(restrictionField);
        updatePlotAndOutput();
    }

    private void updatePlotAndOutput() {
        drawPlot();
        updateOutputArea();
    }

    private void drawPlot() {
        double w = plotCanvas.getWidth();
        double h = plotCanvas.getHeight();
        var gc = plotCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, w, h);

        // Cuadrícula
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);
        for (int i = 1; i < 10; i++) {
            double x = i * w / 10;
            double y = i * h / 10;
            gc.strokeLine(x, 0, x, h);
            gc.strokeLine(0, y, w, y);
        }

        // Ejes
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(0, h - 2, w, h - 2);
        gc.strokeLine(2, 0, 2, h);

        // Dibuja restricciones (ejemplo: línea diagonal)
        gc.setLineWidth(2);
        Color[] colors = {Color.FIREBRICK, Color.ROYALBLUE, Color.SEAGREEN, Color.ORANGE, Color.CORNFLOWERBLUE};
        int colorIdx = 0;
        for (TextField tf : restrictionFields) {
            var restr = EquationParser.parse(tf.getText());
            if (restr != null) {
                gc.setStroke(colors[colorIdx++ % colors.length]);
                double x1 = 0, y1 = h - 30;
                double x2 = w - 30, y2 = 30;
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void updateOutputArea() {
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

    private void centerPlot() {
        // Aquí puedes resetear parámetros de zoom/pan si los añades
        drawPlot();
    }
}