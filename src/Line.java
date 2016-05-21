/**
 * Line.java
 * Thomas Woodside
 * May 5 2016
 * A class to handle the graphing, intersection, and creation of lines.
 */
import java.awt.*;
import java.util.regex.*;

class Line {
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
    // y1 - y2 = m(x1 - x2)

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


    Line(String enteredString)
    {
        //Instantiates the matchers for each regex pattern.
        justXMatch = JUST_X_PATTERN.matcher(enteredString);
        justYMatch = JUST_Y_PATTERN.matcher(enteredString);
        pointSlopeMatch = POINT_SLOPE_PATTERN.matcher(enteredString);
        standardMatch = STANDARD_PATTERN.matcher(enteredString);
        slopeInterceptMatch = SLOPE_INTERCEPT_PATTERN.matcher(enteredString);
        extractInfo();
    }
    private void extractInfo()
    {
        //Extracts information from the lines and ensures that the equation is converted into all necessary forms.

        if (justXMatch.find())
        {
            constantX = new ConstantXLine(new Fraction(justXMatch.group(1).replaceAll(" ", "")));
        }
        else if (justYMatch.find())
        {
            Fraction intercept = new Fraction(justYMatch.group(1).replaceAll(" ", ""));
            inSIForm = new SlopeInterceptLine(new Fraction(0), intercept);
            inSForm = inSIForm.toStandardForm();
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
        }
    }
    public boolean pointOnLine(Fraction x, Fraction y)
    {
        //Determines if a given point is on the line.
        return inSIForm.getM().multiply(x).add(inSIForm.getB()).equals(y);

    }
    public boolean pointOnLine(int x, int y)
    {
        //Determines if a given point is on the line.
        Fraction yinF = new Fraction(y);
        return inSIForm.getM().multiply(x).add(inSIForm.getB()).equals(yinF);
    }

    Coordinates intersectionPoint(Line l)
    {
        //Determines and returns the intersection point of two lines. If the lines are parallel, returns null.
        if (inSIForm.getM().equals(l.getInSIForm().getM()))
        {
            return null;
        }
        else
        {
            Fraction combinedConstants = inSIForm.getB().subtract(l.getInSIForm().getB());
            Fraction combinedCoefficients = l.getInSIForm().getM().subtract(inSIForm.getM());
            Fraction xIntersect = new Fraction(combinedConstants, combinedCoefficients);
            Fraction yIntersect = solveY(xIntersect);
            return new Coordinates(xIntersect, yIntersect);
        }
    }
    void graph(Graphics g, int low, int high)
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
            int yCartStart = solveY(low).toInt();
            int yCartEnd = solveY(high).toInt();
            Coordinates start = new Coordinates(low, yCartStart).mapToScreen(low, high, Client.SCREEN_WIDTH);
            Coordinates end = new Coordinates(high, yCartEnd).mapToScreen(low, high, Client.SCREEN_WIDTH);
            g.drawLine(start.getX().toInt(), start.getY().toInt(), end.getX().toInt(), end.getY().toInt());

        }
    }
    public Fraction solveX(Fraction y)
    {
        //Solve for x given y in an equation
        return y.subtract(inSIForm.getB()).divide(inSIForm.getM());
    }
    public Fraction solveY(Fraction x)
    {
        //Solves for y on the line given x.
        return x.multiply(inSIForm.getM()).add(inSIForm.getB());
    }
    public Fraction solveY(int x)
    {
        //Solves for y on the line given x.
        return inSIForm.getM().multiply(x).add(inSIForm.getB());
    }
    public SlopeInterceptLine getInSIForm() {
        return inSIForm;
    }
    public StandardFormLine getInSForm() {
        return inSForm;
    }
    public PointSlopeLine getInPSForm() {
        return inPSForm;
    }
}