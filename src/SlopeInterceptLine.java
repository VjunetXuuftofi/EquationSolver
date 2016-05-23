/**
 * SlopeInterceptLine.java
 * Thomas Woodside
 * May 21 2016
 * A class to store information of lines in slope-intercept form:
 * y = mx + b
 */
public class SlopeInterceptLine extends Form
{
    private Fraction m;
    private Fraction b;

    public SlopeInterceptLine(Fraction slope, Fraction intercept)
    {
        //Sample equation: y = -5x + 2
        this.m = slope; // e.g. -5
        this.b = intercept; //e.g. + 2
    }

    public Fraction getM() {
        return m;
    }

    public Fraction getB() {
        return b;
    }


    public StandardFormLine toStandardForm()
    {
        //Converts the equation into Standard Form.
        int tomultiply = m.getMyDenom() * b.getMyDenom();
        Fraction A = m.multiply(tomultiply);
        Fraction C = b.multiply(tomultiply);
        int gcd;
        try
        {
            gcd = Fraction.gcd(A.getMyNumer(), C.getMyNumer());
            gcd = Fraction.gcd(gcd, tomultiply);
        } catch (ArithmeticException e)
        {
            //For cases when the y intercept (b) is 0.
            gcd = 1;
        }
        A = A.divide(gcd).multiply(-1);
        C = C.divide(gcd);
        Fraction B = new Fraction(tomultiply / gcd);
        if (A.getMyNumer() < 0)
        {
            A = A.multiply(-1);
            B = B.multiply(-1);
            C = C.multiply(-1);
        }
        return new StandardFormLine(A, B, C);
    }
    public String toString()
    {
        String formattedm = m.toString();
        String formattedb = b.toString();
        if (m.isOne())
        {
            formattedm = m.getMyNumer() < 0 ? " - " : "";
        }
        if (b.getMyNumer() > 0)
        {
            formattedb = " + " + formattedb;
        } else if (b.getMyNumer() == 0)
        {
            formattedb = "";
        }
        return "y = " + formattedm + "x" + formattedb;
    }
}
