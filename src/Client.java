/**
 * Client.java
 * Thomas Woodside
 * Last Modified May 13 2016
 * A client class to instantiate and draw the various equations.
 */
import javax.swing.*;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends JFrame
{
    static final int SCREEN_WIDTH = 600;
    private static int coordLow = -25;
    private static int coordHigh = 25;
    private static ArrayList<Equation> equations = new ArrayList<Equation>();
    private static ArrayList<Equation> toGraph = new ArrayList<Equation>();


    private Client ()
    {
        this.setSize(SCREEN_WIDTH, SCREEN_WIDTH);
        this.setVisible(true);
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Equation Solve-graph.");
        while (true)
        {
            System.out.println("What would you like to do?");
            System.out.println("A. Create a new equation.");
            System.out.println("B. See my entered equations in all forms.");
            System.out.println("C. Solve for x of an entered equation");
            System.out.println("D. Solve for y of an entered equation");
            System.out.println("E. Solve for the intersection point(s) of two entered equations.");
            System.out.println("F. Graph some or all of your entered equations.");
            System.out.println("G. See if a point is on an entered equation.");
            String input = sc.nextLine();
            switch (input)
            {
                case ("A"):
                    showValidEquationForms();
                    System.out.print("Please enter an equation in one of the above forms: ");
                    createEquation(sc.nextLine());
                    break;
                case ("B"):
                    showEquations();
                    System.out.print("Press enter to continue: ");
                    sc.nextLine();
                    break;
                case ("C"):
                    Equation c = selectEquation();
                    solveX(c);
                    break;
                case ("D"):
                    Equation d = selectEquation();
                    solveY(d);
                    break;
                case ("E"):
                    printIntersections();
                    break;
                case ("F"):
                    prepareToGraph();
                    break;
                case ("G"):
                    pointOn();
                    break;


            }
        }
    }
    public static void main(String[] args)
    {
        new Client();
    }

    private static void createEquation (String s)
    {
        try
        {
            //It might be a parabola, in which case add it to the equations ArrayList
            equations.add(new Parabola(s));
        }
        catch (ValidationException e)
        {
            try
            {
                //It might not be a Parabola, in which case it might be  a line.
                equations.add(new Line(s));
            }
            catch (ValidationException v)
            {
                //If it isn't a line, print error issue.
                System.out.println("Your input could not be interpreted. Make sure it is in one of the " +
                        "supported forms of parabolas or lines.");
                try
                {
                    Thread.sleep(2000); // To let the user read the error
                } catch (java.lang.InterruptedException ie)
                {}
            }
        }
    }
    private static void showValidEquationForms()
    {
        //Shows all the valid forms that can be entered.
        System.out.println("Valid forms:");
        System.out.println("Name | form | example");
        System.out.println("LINES:");
        System.out.println("Constant x | x = A | x = 1");
        System.out.println("Constant y | y = A | y = 1");
        System.out.println("Slope-intercept | y = mx + b | y = 2x + 3");
        System.out.println("Standard Form | Ax + By = C | 2x + 3y = 4");
        System.out.println("Point-slope form | y - y2 = m(x-x2) | y - 1 = 2(x - 3)");
        System.out.println("PARABOLAS:");
        System.out.println("Only x^2 with constant| y = Ax^2 + C | y = 2x^2 + 3");
        System.out.println("Standard Form | y = Ax^2 + Bx + C | y = 2x^2 + 3x + 4");
        System.out.println("Vertex Form | y = A(x-h)^2 + k | y = 2(x-3)^2 + 4");
        System.out.println("NB: You do not have to explicitly enter any coefficients of 1 " +
                "(e.g. you don't have to say 1x) or any constants of zero (e.g. + 0)");

    }
    public static void showEquations()
    {
        //Shows each equation in all of its forms with some other information too.
        for (int i = 0; i < equations.size(); i ++)
        {
            Equation e = equations.get(i);
            System.out.println("---------" + "Equation " + (i+1) + "----------------------");
            for (int j = 0; j < e.getForms().size(); j++)
            {
                if (e.getForms().get(j) != null)
                {
                    System.out.println(e.getForms().get(j));
                }
            }
            if (e instanceof Line) // Only lines have slope
            {
                System.out.println("Slope: " + ((Line) e).getInSIForm().getM());
            }
            Fraction[] xIntercepts = e.getXIntercepts();
            Fraction[] yIntercepts = e.getYIntercepts();
            System.out.print("X-intercepts: ");
            displaySolutions(xIntercepts);
            System.out.print("Y-intercepts: ");
            displaySolutions(yIntercepts);
        }
    }
    private static Equation selectEquation()
    {
        //Lets the user select an equation for use in other functions.
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true)
        {
            showEquations();
            System.out.print("Please enter the number of the equation you wish to select: ");
            choice = sc.nextInt() - 1;
            if (choice >= equations.size())
            {
                System.out.println("Invalid input");
                try {
                    Thread.sleep(2000);
                } catch (java.lang.InterruptedException e)
                {}
                continue;
            }
            break;
        }
        return equations.get(choice);
    }

    private static void solveX(Equation e)
    {
        //Lets the user input a y for an equation and see any x values that satisfy that y.
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the y for which you would like to solve for x: ");
        Fraction y = new Fraction(sc.nextLine());
        Fraction[] solutions = e.solveX(y);
        System.out.print("Solution(s): ");
        displaySolutions(solutions);
        System.out.println("Press enter to continue.");
        sc.nextLine();
    }
    private static void solveY(Equation e)
    {
        //Lets the user input a x for an equation and see any y values that satisfy that x.
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the x for which you would like to solve for y: ");
        Fraction x = new Fraction(sc.nextLine());
        Fraction[] solutions = e.solveY(x);
        System.out.print("Solution(s): ");
        displaySolutions(solutions);
        System.out.println("Press enter to continue.");
        sc.nextLine();
    }

    private static void printIntersections()
    {
        //Lets the user select two equations and find any intersections.
        System.out.println("EQUATION 1:");
        try {
            Thread.sleep(2000); // Let's the user read the above message.
        } catch (java.lang.InterruptedException e)
        {}
        Equation e1 = selectEquation();
        System.out.println("EQUATION 2:");
        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e)
        {}
        Equation e2 = selectEquation();
        displaySolutions(e1.intersectionPoints(e2));
        System.out.print("Press enter to continue: ");
        Scanner sc = new Scanner(System.in);
    }

    private static void displaySolutions(Object[] f)
    {
        // Displays solutions for intersections, x or y intercepts, etc. Sometimes there are no solutions, sometimes
        // only one, and sometimes two.
        Scanner sc = new Scanner(System.in);
        if (f != null)
        {
            System.out.print("Solutions: ");
            for (int j = 0; j < f.length; j++)
            {
                System.out.print(f[j]);
                if (j < f.length -1) // Ensures there is no trailing comma.
                {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        else
        {
            System.out.println("No Solutions.");
        }
        System.out.print("Press enter to continue: ");
        sc.nextLine();
    }

    private void prepareToGraph()
    {
        // Lets the user specify any equations they wish to graph.
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            toGraph.add(selectEquation());
            System.out.println("Do you want to add another equation to graph? (yes or no)");
            String choice = sc.nextLine();
            if (choice.equals("no"))
            {
                break;
            }
        }
        repaint();
    }
    private static void pointOn()
    {
        System.out.println("Please choose the equation which you want to check: ");
        Equation e = selectEquation();
        Coordinates c;
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.print("Please enter the coordinates which you want to see are on the equation" +
                    " in the following format: (x,y) e.g. (2/3,1): ");
            try
            {
                c = new Coordinates(sc.nextLine());
                break;
            } catch (Exception ex)
            {
                System.out.println("Invalid input");
            }
        }
        System.out.println("Point is on equation: " + e.pointOn(c));
        System.out.print("Press enter to continue: ");
        sc.nextLine();
    }

    public void paint(Graphics g)
    {
        for (Equation e: toGraph)
        {
            e.graph(g, coordLow, coordHigh);
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
