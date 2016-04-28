/**
 * Created by thomaswoodside on 4/27/16.
 */
public class Coordinates {
    Fraction x;
    Fraction y;

    public Coordinates(Fraction x, Fraction y)
    {
        this.x = x;
        this.y = y;
    }
    public Coordinates(int x, int y)
    {
        this.x = new Fraction(x);
        this.y = new Fraction(y);
    }
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
