import javax.swing.*;
import java.awt.*;

/**
 * Created by thomaswoodside on 4/25/16.
 */
public class Client extends JFrame
{
    private static final int SCREEN_WIDTH = 600;
    private static int coordLow = -25;
    private static int coordHigh = 25;

    private Line todraw = new Line("y = 2x+3");
    private Line todraw2 = new Line("x = 4");

    private Client ()
    {
        this.setSize(SCREEN_WIDTH, SCREEN_WIDTH);
        this.setVisible(true);
    }
    public static void main(String[] args)
    {
        new Client();
    }
    public void paint(Graphics g)
    {
        int center = (coordHigh + coordLow)/2* SCREEN_WIDTH /(coordHigh - coordLow);
        todraw.graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        todraw2.graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        try {
            todraw.intersectionPoint(todraw2).graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        } catch (NullPointerException e)
        {
            System.out.println("Lines are parallel.");
        }

        Coordinates bottomYLine = new Coordinates(0, coordLow).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);
        Coordinates topYLine = new Coordinates(0, coordHigh).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);

        Coordinates bottomXLine = new Coordinates(coordLow, 0).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);
        Coordinates topXLine = new Coordinates(coordHigh, 0).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);

        g.drawLine(bottomXLine.getX().toInt(), bottomXLine.getY().toInt(),
                topXLine.getX().toInt(), topXLine.getY().toInt());
        g.drawLine(bottomYLine.getX().toInt(), bottomYLine.getY().toInt(),
                topYLine.getX().toInt(), topYLine.getY().toInt());


        g.setColor(Color.GRAY);
        for (int i = coordLow; i <= coordHigh; i += (coordHigh - coordLow)/10)
        {
            Coordinates onX = new Coordinates(i, 0).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);
            Coordinates onY = new Coordinates(0, i).mapToScreen(coordLow, coordHigh, SCREEN_WIDTH);
            g.drawString(String.valueOf(i), onX.getX().toInt(), onX.getY().toInt());
            g.drawString(String.valueOf(i), onY.getX().toInt(), onY.getY().toInt());
            g.drawLine(onX.getX().toInt(), 0, onX.getX().toInt(), SCREEN_WIDTH);
            g.drawLine(0, onY.getY().toInt(), SCREEN_WIDTH, onY.getY().toInt());

        }
        g.setColor(Color.BLACK);

    }
}
