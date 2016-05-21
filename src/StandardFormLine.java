/**
 * StandardFormLine.java
 * Thomas Woodside
 * May 21 2016
 * A class to store information of lines in standard form:
 * Ax + By = C
 */
class StandardFormLine {
    private Fraction A;
    private Fraction B;
    private Fraction C;

    StandardFormLine(Fraction A, Fraction B, Fraction C)
    {
        //Sample equation 2x+3y=16
        this.A = A; // e.g. 2
        this.B = B; // e.g. 3
        this.C = C; // e.g. 16
    }
    SlopeInterceptLine toSlopeInterceptForm()
    {
        return new SlopeInterceptLine(A.multiply(-1).divide(B), C.divide(B));
    }
    public String toString()
    {
        String formattedA = A.toString();
        String formattedB = B.toString();
        if (B.isOne())
        {
            formattedB = B.getMyNumer() < 0 ? "-" + "" : "";
        }
        if (A.isOne())
        {
            formattedA = "";
        }
        if (B.getMyNumer() > 0)
        {
            formattedB = "+" + formattedB;
        }
        return formattedA + "x" + formattedB + "y=" + C.toString();
    }
}
