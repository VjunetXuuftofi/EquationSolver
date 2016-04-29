import java.awt.*;

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

    public Fraction getX() {
        return x;
    }

    public Fraction getY() {
        return y;
    }

    public String toString()
    {

        return "(" + x + "," + y + ")";
    }
    public void graph(Graphics g, int xLow, int xHigh, int screenwidth)
    {
        int diameter = 6;
        Coordinates tograph = new Coordinates(x, y).mapToScreen(xLow, xHigh, screenwidth);
        g.fillOval(tograph.getX().subtract(diameter/2).toInt(),
                tograph.getY().subtract(diameter/2).toInt(), diameter, diameter);
        g.drawString(toString(), tograph.getX().subtract(50).toInt(), tograph.getY().subtract(15).toInt());
    }
    public Coordinates mapToScreen(int xLow, int xHigh, int screenWidth)
    {
        int span = xHigh - xLow;
        int xMapped = x.subtract(xLow).divide(span).multiply(screenWidth).toInt();
        int yMapped = screenWidth - y.subtract(xLow).divide(span).multiply(screenWidth).toInt();
        return new Coordinates(xMapped, yMapped);
    }
}
