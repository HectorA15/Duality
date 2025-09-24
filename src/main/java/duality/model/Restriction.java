package duality.model;

public class Restriction {
    private double recursoX;
    private double recursoY;
    private double limite;

    public Restriction() {}

    public Restriction(double x, double y, double limite) {
        this.recursoX = x;
        this.recursoY = y;
        this.limite = limite;
    }

    public double getRecursoX() { return recursoX; }
    public double getRecursoY() { return recursoY; }
    public double getLimite() { return limite; }

    public void setRecursoX(double recursoX) { this.recursoX = recursoX; }
    public void setRecursoY(double recursoY) { this.recursoY = recursoY; }
    public void setLimite(double limite) { this.limite = limite; }
}