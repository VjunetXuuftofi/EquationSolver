/**
 * StandardFormParabola.java
 * Thomas Woodside
 * May 21 2016
 * A class to store information of parabolas in vertex form:
 * y = m(x-h)^2 + k
 */
public class VertexFormParabola extends Form
{
    private Fraction x;
    private Fraction k;
    private Fraction m;

    public VertexFormParabola(Fraction x, Fraction k, Fraction slope)
    {
        //Sample equation: y = -4(x-2)^2 + 5
        this.x = x; // -h e.g. -2
        this.k = k; // e.g. +5
        this.m = slope; //e.g. -4
    }
    public StandardFormParabola toStandardForm()
    {
        Fraction A = m;
        Fraction B = x.multiply(2).multiply(m);
        Fraction C = x.multiply(x).multiply(m).add(k);
        return new StandardFormParabola(A, B, C);
    }

    public Fraction getX() {
        return x;
    }

    public Fraction getK() {
        return k;
    }

    public Fraction getM() {
        return m;
    }

    public String toString()
    {
        String formattedx = x.toString();
        String formattedk = k.toString();
        if (x.getMyNumer() > 0)
        {
            formattedx = "+" + formattedx;
        } else if (x.getMyNumer() == 0)
        {
            formattedx = "";
        }
        if (k.getMyNumer() > 0)
        {
            formattedk = "+" + formattedk;
        } else if (k.getMyNumer() == 0)
        {
            formattedk = "";
        }

        return "y=" + m + "(x" + formattedx + ")^2" + formattedk;
    }
}
