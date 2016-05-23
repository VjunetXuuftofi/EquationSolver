/**
 * Coordinates.java
 * Thomas Woodside
 * Last Modified May 5 2016
 * Stores x and y coordinates and provides a method to map them to coordinates in a graphics window.
 */
import java.awt.*;

class Coordinates {
    private Fraction x;
    private Fraction y;

    public Coordinates(Fraction x, Fraction y)
    {
        // If the coordinates are given in fraction form
        this.x = x;
        this.y = y;
    }

    public Coordinates(int x, int y)
    {
        //If the coordinates are given in integer form (they will be converted to integer form.
        this.x = new Fraction(x);
        this.y = new Fraction(y);
    }

    public Coordinates(String s)
    {
        String coordinates = s.replace("(", "").replace(")", "");
        String[] split = coordinates.split(",");
        x = new Fraction(split[0]);
        y = new Fraction(split[1]);
    }

    public Fraction getX()
    {
        return x;
    }

    public Fraction getY()
    {
        return y;
    }

    public String toString()
    {
        //For painting and printing the coordinate labels.
        return "(" + x + "," + y + ")";
    }

    public void graph(Graphics g, int xLow, int xHigh, int screenwidth)
            // Graphs a point, displaying the x and y coordinates above it.
    {
        final int diameter = 6;
        final int X_OFFSET = 5; // To ensure the label doesn't obscure the point
        // Where to graph the point on the screen:
        Coordinates tograph = mapToScreen(xLow, xHigh, Client.SCREEN_WIDTH);
        // Drawing the point at the coordinate location
        g.fillOval(tograph.getX().subtract(diameter/2).toInt(),
                tograph.getY().subtract(diameter/2).toInt(), diameter, diameter);
        // Drawing the labels for the points.
        g.drawString(toString(),
                tograph.getX().add(X_OFFSET).toInt(),
                tograph.getY().toInt());
    }
    public Coordinates mapToScreen(int xLow, int xHigh, int screenWidth)
    {
        //Maps cartesian coordinates to coordinates on a square graphics window.
        int span = xHigh - xLow;
        int xMapped = x.subtract(xLow).divide(span).multiply(screenWidth).toInt();
        int yMapped = screenWidth - y.subtract(xLow).divide(span).multiply(screenWidth).toInt();
        return new Coordinates(xMapped, yMapped);
    }
}
