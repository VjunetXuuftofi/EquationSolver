/**
 * StandardFormParabola.java
 * Thomas Woodside
 * May 21 2016
 * A class to store information of parabolas in standard form:
 * y = Ax^2 + Bx + C
 */
class StandardFormParabola {
    private Fraction A;
    private Fraction B;
    private Fraction C;

    StandardFormParabola(Fraction A, Fraction B, Fraction C)
    {
        // e.g. -2x^2 + 3x - 4
        this.A = A; // e.g. -2
        this.B = B; // e.g. +3
        this.C = C; // e.g. -4
    }
    VertexFormParabola toVertexForm()
    {
        //Converts the equation into Vertex Form.
        Fraction x = B.divide(A).divide(2);
        Fraction k = C.subtract(x.multiply(x).multiply(A));
        return new VertexFormParabola(x, k, A);
    }

    public Fraction getA() {
        return A;
    }

    public Fraction getB() {
        return B;
    }

    public Fraction getC() {
        return C;
    }

    public String toString()
    {
        String formattedB = B.toString();
        String formattedC = C.toString();
        if (B.isOne())
        {
            formattedB = B.getMyNumer() < 0 ? "-" + "" : "";
        }
        if (B.getMyNumer() > 0)
        {

            formattedB = "+" + formattedB;
        }
        if (C.getMyNumer() > 0)
        {
            formattedC = "+" + formattedC;
        }
        return "y=" + A + "x^2" + formattedB + "x" + formattedC;
    }
}
