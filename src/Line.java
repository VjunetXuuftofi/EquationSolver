/**
 * Created by thomaswoodside on 4/25/16.
 */
import java.awt.*;
import java.util.regex.*;

public class Line {
    private static final Pattern slopeIntercept =
            Pattern.compile("y ?= ?(-? ?(\\d|/)*)x( ?(\\+|-) ?(\\d|/)*)?");
    private static final Pattern standard =
            Pattern.compile("((\\d)*)x ?(((\\+|-) ?(\\d)*)y ?)?= ?(-? ?(\\d)*)");
    private static final Pattern pointSlope =
            Pattern.compile("y( ?(-|\\+) ?(\\d|/)*)? ?= ?-? ?(\\d|/)*\\(x( ?(\\+|-) ?(\\d|/)*)?\\)");
    private String enteredString;
    private Matcher slopeInterceptMatch;
    private Matcher standardMatch;
    private Matcher pointSlopeMatch;
    private SlopeIntercept inSIForm;
    private StandardForm inSForm;
    private PointSlope inPSForm;

    private int givenForm;

    public SlopeIntercept getInSIForm() {
        return inSIForm;
    }

    public StandardForm getInSForm() {
        return inSForm;
    }

    public PointSlope getInPSForm() {
        return inPSForm;
    }

    public Line(String s)
    {
        enteredString = s;
        pointSlopeMatch = pointSlope.matcher(enteredString);
        standardMatch = standard.matcher(enteredString);
        slopeInterceptMatch = slopeIntercept.matcher(enteredString);
        getPattern();
        extractInfo();
    }
    public void getPattern()
    {

        if (slopeInterceptMatch.find())
        {
            givenForm = 0;
        }
        else if (standardMatch.find())
        {
            givenForm = 1;
        }
        else if (pointSlopeMatch.find())
        {
            givenForm = 2;
        }
    }
    public void extractInfo()
    {
        if (givenForm == 0)
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

            inSIForm = new SlopeIntercept(slope, intercept);
            inSForm = inSIForm.toStandardForm();
        }
        else if (givenForm == 1)
        {
            Fraction A;
            try {
                A = new Fraction(standardMatch.group(1).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                A = new Fraction(0);
            }
            Fraction B;
            try {
                B = new Fraction(standardMatch.group(4).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                B = new Fraction(0);
            }
            Fraction C;
            try {
                C = new Fraction(standardMatch.group(7).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                C = new Fraction(0);
            }

            inSForm = new StandardForm(A, B, C);
            inSIForm = inSForm.toSlopeInterceptForm();
        }
        else if (givenForm == 2)
        {
            Fraction x;
            try {
                x = new Fraction(pointSlopeMatch.group(6).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                x = new Fraction(0);
            }
            Fraction y;
            try {
                y = new Fraction(pointSlopeMatch.group(1).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                y = new Fraction(0);
            }
            Fraction slope;
            try {
                slope = new Fraction(pointSlopeMatch.group(4).replaceAll(" ", ""));
            } catch (NullPointerException e)
            {
                slope = new Fraction(0);
            }
            inPSForm = new PointSlope(x, y, slope);
            inSForm = inPSForm.toStandardForm();
            inSIForm = inPSForm.toSlopeIntercept();
        }
    }
    public boolean pointOnLine(Fraction x, Fraction y)
    {
        return inSIForm.getM().multiply(x).add(inSIForm.getB()).equals(y);

    }
    public boolean pointOnLine(int x, int y)
    {
        Fraction xinF = new Fraction(x);
        Fraction yinF = new Fraction(y);
        return inSIForm.getM().multiply(xinF).add(inSIForm.getB()).equals(yinF);
    }
    public Coordinates intersectionPoint(Line l)
    {
        if (inSIForm.getM().equals(l.getInSIForm().getM()))
        {
            return null;
        }
        else {
            Fraction combinedConstants = inSIForm.getB().subtract(l.getInSIForm().getB());
            Fraction combinedCoefficients = l.getInSIForm().getM().subtract(inSIForm.getM());
            Fraction xIntersect = new Fraction(combinedConstants, combinedCoefficients);
            Fraction yIntersect = inSIForm.getM().multiply(xIntersect).add(inSIForm.getB());
            return new Coordinates(xIntersect, yIntersect);
        }
    }
    public void graph(Graphics g, int xLow, int xHigh, int screenwidth)
    {
        int yCartStart = inSIForm.getM().multiply(xLow).add(inSIForm.getB()).toInt();
        int yCartEnd = inSIForm.getM().multiply(xHigh).add(inSIForm.getB()).toInt();
        Coordinates start = new Coordinates(xLow, yCartStart).mapToScreen(xLow, xHigh, screenwidth);
        Coordinates end = new Coordinates(xHigh, yCartEnd).mapToScreen(xLow, xHigh, screenwidth);
        g.drawLine(start.getX().toInt(), start.getY().toInt(), end.getX().toInt(), end.getY().toInt());
    }
}
