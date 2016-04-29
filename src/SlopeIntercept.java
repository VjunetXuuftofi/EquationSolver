/**
 * Created by thomaswoodside on 4/26/16.
 */
public class SlopeIntercept {
    private Fraction m;
    private Fraction b;

    public SlopeIntercept (Fraction slope, Fraction intercept)
    {
        this.m = slope;
        this.b = intercept;
    }

    public Fraction getM() {
        return m;
    }

    public Fraction getB() {
        return b;
    }


    public StandardForm toStandardForm()
    {
        int tomultiply = m.getMyDenom() * b.getMyDenom();
        Fraction A = m.multiply(tomultiply);
        Fraction C = b.multiply(tomultiply);
        int gcd;
        try {
            gcd = Fraction.gcd(A.getMyNumer(), C.getMyNumer());
        } catch (ArithmeticException e)
        {
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
        return new StandardForm(A, B, C);
    }
    public String toString()
    {
        String formattedm = m.toString();
        String formattedb = b.toString();
        if (m.isOne())
        {
            if (m.getMyNumer() < 0)
            {
                formattedm = "-" + "";
            }
            else
            {
                formattedm = "";
            }
        }
        if (b.getMyNumer() > 0)
        {
            formattedb = "+" + formattedb;
        }
        return "y=" + formattedm + "x" + formattedb;
    }
}
