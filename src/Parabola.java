/**
 * Parabola.java
 * Thomas Woodside
 * Last Modified May 13 2016
 * A class to handle the graphing, intersection, and creation of parabolas.
 */
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.*;

public class Parabola extends Equation
{
    private static final Pattern noX = Pattern.compile("y ?= ?(-?(\\d)*)x\\^2 ?((\\+|-)? ?(\\d)*)?");
    // y = Ax^2 (+C)
    private static final Pattern standardForm =
            Pattern.compile("y ?= ?(-?(\\d)*)x\\^2 ?(((\\+|-) ?(\\d)*)x) ?((\\+|-)? ?(\\d)*)?");
    // y = Ax^2 + Bx (+C)
    private static final Pattern vertexForm =
            Pattern.compile("y ?= ?(-?(\\d|/)*)\\(x ?((\\+|-) ?(\\d|/)*)?\\)\\^2 ?((\\+|-) ?(\\d|/)*)?");
    // y = m(x - h)^2 + k

    //Matchers for the regex.
    private Matcher noXMatch;
    private Matcher standardMatch;
    private Matcher vertexMatch;

    //The equations in two forms.
    private StandardFormParabola inSF;
    private VertexFormParabola inVF;

    //The equation in all forms.
    private ArrayList<Form> forms = new ArrayList<Form>();


    public Parabola(String enteredString) throws ValidationException
    {
        //Creates a Parabola from a string with its equation.
        noXMatch = noX.matcher(enteredString);
        vertexMatch = vertexForm.matcher(enteredString);
        standardMatch = standardForm.matcher(enteredString);
        extractInfo();
    }
    public void extractInfo() throws ValidationException
    {
        //Extracts the coefficient and constant information from the equation and ensures it is
        // converted into both forms.
        if (standardMatch.find())
        {
            Fraction A;
            try
            {
                A = new Fraction(standardMatch.group(1).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                A = new Fraction(0);
            }
            Fraction B;
            try
            {
                B = new Fraction(standardMatch.group(4).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                B = new Fraction(0);
            }
            Fraction C;
            try
            {
                C = standardMatch.group(7).equals("") ? new Fraction(0) :
                        new Fraction(standardMatch.group(7).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                C = new Fraction(0);
            }

            inSF = new StandardFormParabola(A, B, C);
            inVF = inSF.toVertexForm();

        }
        else if (vertexMatch.find())
        {
            Fraction slope;
            try
            {
                slope = new Fraction(vertexMatch.group(1).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                slope = new Fraction(1);
            }
            Fraction x;
            try
            {
                x = new Fraction(vertexMatch.group(3).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                x = new Fraction(0);
            }
            Fraction k;
            try
            {
                k = new Fraction(vertexMatch.group(6).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                k = new Fraction(0);
            }

            inVF = new VertexFormParabola(x, k, slope);
            inSF = inVF.toStandardForm();
        }
        else if (noXMatch.find())
        {
            Fraction A;
            try
            {
                A = new Fraction(noXMatch.group(1).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                A = new Fraction(1);
            }
            Fraction B = new Fraction(0);
            Fraction C;
            try
            {
                C = noXMatch.group(3).equals("") ? new Fraction(0) :
                        new Fraction(noXMatch.group(3).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                C = new Fraction(0);
            }

            inSF = new StandardFormParabola(A, B, C);
            inVF = inSF.toVertexForm();
        }
        else {
            throw new ValidationException("");
        }
        forms.add(inVF);
        forms.add(inSF);
    }
    public boolean pointOn(Coordinates c)
    {
        return inSF.getA().multiply(c.getX()).multiply(c.getX()).add(inSF.getB().multiply(c.getX())).add(inSF.getC()).equals(c.getY());
    }

    public Coordinates[] intersectionPoints(Equation e)
    {
        Fraction a;
        Fraction b;
        Fraction c;
        if (e instanceof Line)
        {
            Line l = (Line) e;
            a = inSF.getA();
            b = inSF.getB().subtract(l.getInSIForm().getM());
            c = inSF.getC().subtract(l.getInSIForm().getB());
        }
        else if (e instanceof Parabola)
        {
            Parabola p = (Parabola) e;
            a = inSF.getA();
            b = inSF.getB().subtract(p.getInSF().getB());
            c = inSF.getC().subtract(p.getInSF().getC());
        }
        else
        {
            return null;
        }
        Fraction[] results = qFormula(a, b, c);
        if (results == null)
        {
            return null;
        }
        Fraction x1 = results[0];
        Fraction x2 = results[1];
        Fraction y1;
        Fraction y2;
        if (e instanceof Line)
        {
            Line l = (Line) e;
            y1 = l.getInSIForm().getM().multiply(x1).add(l.getInSIForm().getB());
            y2 = l.getInSIForm().getM().multiply(x2).add(l.getInSIForm().getB());
        }
        else
        {
            Parabola p = (Parabola) e;
            y1 = p.getInSF().getA().multiply(x1).multiply(x1).add(x1.multiply(inSF.getB())).add(inSF.getC());
            y2 = p.getInSF().getA().multiply(x2).multiply(x2).add(x2.multiply(inSF.getB())).add(inSF.getC());
        }
        Coordinates[] toreturn = {new Coordinates(x1, y1), new Coordinates(x2, y2)};
        return toreturn;
    }

    public static Fraction[] qFormula(Fraction a, Fraction b, Fraction c)
    {
        //Performs the quadratic formula.
        Fraction inRoot = b.multiply(b).subtract(a.multiply(c).multiply(4));
        if (inRoot.isLessThan(0))
        {
            return null;
        }

        Fraction rooted = inRoot.sqrt();
        Fraction result1 = b.multiply(-1).add(rooted).divide(a.multiply(2));
        Fraction result2 = b.multiply(-1).subtract(rooted).divide(a.multiply(2));
        Fraction[] results = {result1, result2};
        return results;
    }

    public void graph(Graphics g, int xLow, int xHigh)
    {
        //Graphs the parabola. Works by solving for y on for every 1/10 and connecting these points.
        Fraction y = solveY(new Fraction(xLow))[0];
        Coordinates lastcoordinates = new Coordinates(new Fraction(xLow), y).mapToScreen(xLow, xHigh, Client.SCREEN_WIDTH);
        Coordinates coordinates;
        for (Fraction i = new Fraction(xLow); i.isLessThan(xHigh); i = i.add(new Fraction(1, 10)))
        {
            y = solveY(i)[0];
            coordinates = new Coordinates(i, y).mapToScreen(xLow, xHigh, Client.SCREEN_WIDTH);
            g.drawOval(coordinates.getX().toInt(), coordinates.getY().toInt(), 1, 1);
            g.drawLine(lastcoordinates.getX().toInt(), lastcoordinates.getY().toInt(), coordinates.getX().toInt(),
                    coordinates.getY().toInt());
            lastcoordinates = coordinates;

        }
    }
    public StandardFormParabola getInSF()
    {
        return inSF;
    }

    public VertexFormParabola getInVF() {
        return inVF;
    }
    public Fraction[] solveY(Fraction x)
    {
        //Solves for the y coordinate on the parabola given the x coordinate.
        Fraction[] toreturn = {inSF.getA().multiply(x).multiply(x).add(inSF.getB().multiply(x)).add(inSF.getC())};
        return toreturn;
    }
    public Fraction[] solveX(Fraction y)
    {
        return qFormula(inSF.getA(), inSF.getB(), inSF.getC().subtract(y));
    }
    public Fraction[] getXIntercepts()
    {
        return solveX(new Fraction(0));
    }
    public Fraction[] getYIntercepts()
    {
        return solveY(new Fraction(0));
    }
    public ArrayList<Form> getForms()
    {
        return new ArrayList<Form>(forms); // to copy
    }
}
