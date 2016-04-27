/**
 * Created by thomaswoodside on 4/26/16.
 */
public class StandardForm {
    private Fraction A;
    private Fraction B;
    private Fraction C;

    public StandardForm (Fraction A, Fraction B, Fraction C)
    {
        this.A = A;
        this.B = B;
        this.C = C;
    }
    public SlopeIntercept toSlopeInterceptForm()
    {
        return new SlopeIntercept(A.multiply(-1).divide(B), C.divide(B));
    }
    public String toString()
    {
        String formattedA = A.toString();
        String formattedB = B.toString();
        if (B.isOne())
        {
            if (B.getMyNumer() < 0)
            {
                formattedB = "-" + "";
            }
            else
            {
                formattedB = "";
            }
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
