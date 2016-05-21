/**
 * Client.java
 * Thomas Woodside
 * Last Modified May 13 2016
 * A client class to instantiate and draw the various equations.
 */
import javax.swing.*;
import java.awt.*;

public class Client extends JFrame
{
    static final int SCREEN_WIDTH = 600;
    private static final int coordLow = -25;
    private static final int coordHigh = 25;

    private Line todraw = new Line("y = x");
    private Line todraw2 = new Line("y = 4x - 6");
    private Parabola todraw3 = new Parabola("y = x^2");
    private Line todraw4 = new Line("y -5 = -3(x+4)");

    private Client ()
    {
        this.setSize(SCREEN_WIDTH, SCREEN_WIDTH);
        this.setVisible(true);
    }
    public static void main(String[] args)
    {
        new Client();
//        Scanner sc = new Scanner(System.in);
//        while (true)
//        {
//            System.out.println("Welcome to Equation Solve-graph. What would you like to do?");
//            System.out.println("A. Create a new equation.");
//            System.out.println("B. Solve for x of an entered equation");
//            System.out.println("C. Solve for y of an entered equation");
//            System.out.println("D. Solve for the intersection points of two entered equations.");
//            System.out.println("E. Graph some or all of your entered equations.");
//            String input = sc.nextLine();
//            switch (input)
//            {
//                case ("A"):
//
//
//
//            }
//
//
//        }
    }
    public void paint(Graphics g)
    {
        //Drawing the objects todraw:
        //todraw.graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        //todraw2.graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        //todraw3.graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        todraw4.graph(g, coordLow, coordHigh);
        //Drawing an intersection point:
        try
        {
            todraw.intersectionPoint(todraw2).graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        } catch (NullPointerException e)
        {
            System.out.println("Lines are parallel.");
        }
        try
        {
            Coordinates[] intersections = todraw3.intersectionPoints(todraw);
            intersections[0].graph(g, coordLow, coordHigh, SCREEN_WIDTH);
            intersections[1].graph(g, coordLow, coordHigh, SCREEN_WIDTH);
        } catch (NullPointerException e)
        {
            System.out.println("There is no intersection");
        }

        //Drawing the axes lines
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
            //Drawing the gridlines
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
