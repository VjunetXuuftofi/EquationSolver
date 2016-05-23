/**
 * Line.java
 * Thomas Woodside
 * May 5 2016
 * A class to handle the graphing, intersection, and creation of lines.
 */
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.*;

class Line extends Equation
{
    //The following are regular expressions designed to identify different forms of linear equations.
    private static final Pattern JUST_X_PATTERN =
            Pattern.compile("x ?= ?(-?(\\d|/)*)");
    // x = A
    private static final Pattern JUST_Y_PATTERN =
            Pattern.compile("^y ?= ?(-?(\\d|/)*)$");
    // y = A
    private static final Pattern SLOPE_INTERCEPT_PATTERN =
            Pattern.compile("y ?= ?(-? ?(\\d|/)*)x( ?(\\+|-) ?(\\d|/)*)?");
    // y = mx + b
    private static final Pattern STANDARD_PATTERN =
            Pattern.compile("((\\d)*)x ?(((\\+|-) ?(\\d)*)y ?)?= ?(-? ?(\\d)*)"); // e.g. 2x + 3y = 4
    // Ax + Bx = C
    private static final Pattern POINT_SLOPE_PATTERN =
            Pattern.compile("y( ?(-|\\+) ?(\\d|/)*)? ?= ?(-? ?(\\d|/)*)\\(x( ?(\\+|-) ?(\\d|/)*)?\\)");
    // y - y2 = m(x - x2)

    //Matchers for the regex patterns.
    private Matcher justXMatch;
    private Matcher justYMatch;
    private Matcher slopeInterceptMatch;
    private Matcher standardMatch;
    private Matcher pointSlopeMatch;

    //The line in various forms (if available).
    private SlopeInterceptLine inSIForm;
    private StandardFormLine inSForm;
    private PointSlopeLine inPSForm;
    private ConstantXLine constantX;

    //The line in all its forms
    private ArrayList<Form> forms = new ArrayList<Form>();



    public Line(String enteredString) throws ValidationException
    {
        //Instantiates the matchers for each regex pattern.
        justXMatch = JUST_X_PATTERN.matcher(enteredString);
        justYMatch = JUST_Y_PATTERN.matcher(enteredString);
        pointSlopeMatch = POINT_SLOPE_PATTERN.matcher(enteredString);
        standardMatch = STANDARD_PATTERN.matcher(enteredString);
        slopeInterceptMatch = SLOPE_INTERCEPT_PATTERN.matcher(enteredString);
        extractInfo();
    }
    public void extractInfo() throws ValidationException
    {
        //Extracts information from the lines and ensures that the equation is converted into all necessary forms.

        if (justXMatch.find())
        {
            constantX = new ConstantXLine(new Fraction(justXMatch.group(1).replaceAll(" ", "")));
            forms.add(constantX);
        }
        else if (justYMatch.find())
        {
            Fraction intercept = new Fraction(justYMatch.group(1).replaceAll(" ", ""));
            inSIForm = new SlopeInterceptLine(new Fraction(0), intercept);
            inSForm = inSIForm.toStandardForm();
            forms.add(inSIForm);
            forms.add(inSForm);
        }
        if (slopeInterceptMatch.find())
        {
            Fraction slope;
            try {
                slope = new Fraction(slopeInterceptMatch.group(1).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                slope = new Fraction(0);
            }
            Fraction intercept;
            try {
                intercept = new Fraction(slopeInterceptMatch.group(3).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                intercept = new Fraction(0);
            }

            inSIForm = new SlopeInterceptLine(slope, intercept);
            inSForm = inSIForm.toStandardForm();
            forms.add(inSIForm);
            forms.add(inSForm);
        }
        else if (standardMatch.find())
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
                C = new Fraction(standardMatch.group(7).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                C = new Fraction(0);
            }

            inSForm = new StandardFormLine(A, B, C);
            inSIForm = inSForm.toSlopeInterceptForm();
            forms.add(inSForm);
            forms.add(inSIForm);
        }
        else if (pointSlopeMatch.find())
        {
            Fraction x;
            try
            {
                x = new Fraction(pointSlopeMatch.group(6).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                x = new Fraction(0);
            }
            Fraction y;
            try
            {
                y = new Fraction(pointSlopeMatch.group(1).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                y = new Fraction(0);
            }
            Fraction slope;
            try
            {
                slope = new Fraction(pointSlopeMatch.group(4).replaceAll(" ", ""));
            }
            catch (NullPointerException e)
            {
                slope = new Fraction(0);
            }
            inPSForm = new PointSlopeLine(x, y, slope);
            inSForm = inPSForm.toStandardForm();
            inSIForm = inPSForm.toSlopeIntercept();
            forms.add(inPSForm);
            forms.add(inSForm);
            forms.add(inSIForm);
        }
        else
        {
            throw new ValidationException("");
        }
    }
    public boolean pointOn(Coordinates c)
    {
        return inSIForm.getM().multiply(c.getX()).add(inSIForm.getB()).equals(c.getY());
    }

    public Coordinates[] intersectionPoints(Equation e)
    {
        //Determines and returns the intersection point of two lines. If the lines are parallel, returns null.
        if (e instanceof Line)
        {
            Line l = (Line) e;
            if (inSIForm.getM().equals(l.getInSIForm().getM()))
            {
                return null;
            }
            else
            {
                Fraction combinedConstants = inSIForm.getB().subtract(l.getInSIForm().getB());
                Fraction combinedCoefficients = l.getInSIForm().getM().subtract(inSIForm.getM());
                Fraction xIntersect = new Fraction(combinedConstants, combinedCoefficients);
                Fraction yIntersect = solveY(xIntersect)[0];
                Coordinates[] toreturn = {new Coordinates(xIntersect, yIntersect)};
                return toreturn;
            }
        }
        else if (e instanceof Parabola)
        {
            Parabola p = (Parabola) e;
            Fraction a = p.getInSF().getA();
            Fraction b = p.getInSF().getB().subtract(inSIForm.getM());
            Fraction c = p.getInSF().getC().subtract(inSIForm.getB());
            Fraction[] results = Parabola.qFormula(a, b, c);
            if (results == null)
            {
                return null;
            }
            Fraction x1 = results[0];
            Fraction x2 = results[1];
            Fraction y1 = inSIForm.getM().multiply(x1).add(inSIForm.getB());
            Fraction y2 = inSIForm.getM().multiply(x2).add(inSIForm.getB());
            Coordinates[] toreturn = {new Coordinates(x1, y1), new Coordinates(x2, y2)};
            return toreturn;
        }
        return null;
    }
    public void graph(Graphics g, int low, int high)
    {
        //Graphs the line.
        g.setColor(Color.black);
        if (constantX != null)
        {
            // Constant X graphs must be dealt with separately (they aren't functions).
            Coordinates start = new Coordinates(constantX.getX(),
                    new Fraction(low)).mapToScreen(low, high, Client.SCREEN_WIDTH);
            g.drawLine(start.getX().toInt(), start.getY().toInt(), start.getX().toInt(), 0);
        }
        else
        {
            // All other graphs can be graphed in a similar way.
            // There is already an min and max x for the left and right of the screen. The below maps these x
            // values to y values.
            int yCartStart = solveY(new Fraction(low))[0].toInt();
            int yCartEnd = solveY(new Fraction(high))[0].toInt();
            Coordinates start = new Coordinates(low, yCartStart).mapToScreen(low, high, Client.SCREEN_WIDTH);
            Coordinates end = new Coordinates(high, yCartEnd).mapToScreen(low, high, Client.SCREEN_WIDTH);
            g.drawLine(start.getX().toInt(), start.getY().toInt(), end.getX().toInt(), end.getY().toInt());

        }
    }
    public Fraction[] solveX(Fraction y)
    {
        //Solve for x given y in an equation
        Fraction[] toreturn = {y.subtract(inSIForm.getB()).divide(inSIForm.getM())};
        return toreturn;
    }
    public Fraction[] solveY(Fraction x)
    {
        //Solves for y on the line given x.
        Fraction[] toreturn = {x.multiply(inSIForm.getM()).add(inSIForm.getB())};
        return toreturn;
    }
    public SlopeInterceptLine getInSIForm()
    {
        return inSIForm;
    }
    public StandardFormLine getInSForm()
    {
        return inSForm;
    }
    public PointSlopeLine getInPSForm()
    {
        return inPSForm;
    }
    public Fraction[] getXIntercepts()
    {
        Fraction[] toreturn = solveX(new Fraction(0));
        return toreturn;
    }
    public Fraction[] getYIntercepts()
    {
        Fraction[] toreturn = {inSIForm.getM()};
        return toreturn;
    }
    public ArrayList<Form> getForms()
    {
        return new ArrayList<Form>(forms); // to copy
    }
}