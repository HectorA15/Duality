package duality.util;

import duality.model.Points;
import duality.model.Restriction;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    public static Points calcularInterseccion(Restriction r1, Restriction r2) {
        double a1 = r1.getRecursoX();
        double b1 = r1.getRecursoY();
        double c1 = r1.getLimite();

        double a2 = r2.getRecursoX();
        double b2 = r2.getRecursoY();
        double c2 = r2.getLimite();

        double denominador = a1 * b2 - a2 * b1;

        if (denominador == 0) {
            return null;
        }

        double x = (c1 * b2 - c2 * b1) / denominador;
        double y = (a1 * c2 - a2 * c1) / denominador;

        return new Points(x, y);
    }

    public static List<Points> todasIntersecciones(List<Restriction> restricciones) {
        List<Points> intersecciones = new ArrayList<>();

        for (int i = 0; i < restricciones.size(); i++) {
            for (int j = i + 1; j < restricciones.size(); j++) {
                Points p = calcularInterseccion(restricciones.get(i), restricciones.get(j));
                if (p != null) {
                    intersecciones.add(p);
                }
            }
        }

        return intersecciones;
    }
}