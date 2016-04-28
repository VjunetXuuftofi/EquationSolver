/**
 * Created by thomaswoodside on 4/25/16.
 */
import java.util.regex.*;
import javax.script.*;

public class Line {
    private static final Pattern slopeIntercept =
            Pattern.compile("y ?= ?(-?(\\d|/)*)x( ?(\\+|-) ?(\\d|/)*)?");
    private static final Pattern standard =
            Pattern.compile("((\\d)*)x( ?((\\+|-) ?(\\d)*)y ?)?= ?(-? ?(\\d)*)");
    private static final Pattern pointSlope =
            Pattern.compile("y( ?(-|\\+) ?(\\d|/)*)? ?= ?-?(\\d|/)*\\(x( ?(\\+|-) ?(\\d|/)*)?\\)");
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
            inSIForm = new SlopeIntercept(new Fraction(slopeInterceptMatch.group(1).replaceAll(" ", "")),
                    new Fraction(slopeInterceptMatch.group(3).replaceAll(" ", "")));
            inSForm = inSIForm.toStandardForm();
        }
        else if (givenForm == 1)
        {
            inSForm = new StandardForm(new Fraction(standardMatch.group(1).replaceAll(" ", "")),
                    new Fraction(standardMatch.group(4).replaceAll(" ", "")),
                    new Fraction(standardMatch.group(7).replaceAll(" ", "")));
            inSIForm = inSForm.toSlopeInterceptForm();
        }
        else if (givenForm == 2)
        {
            inPSForm = new PointSlope(new Fraction(pointSlopeMatch.group(6).replaceAll(" ", "")),
                    new Fraction(pointSlopeMatch.group(1).replaceAll(" ", "")),
                    new Fraction(pointSlopeMatch.group(4).replaceAll(" ", "")));
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
        Fraction combinedConstants = inSIForm.getB().subtract(l.getInSIForm().getB());
        Fraction combinedCoefficients = l.getInSIForm().getM().subtract(inSIForm.getM());
        Fraction xIntersect = new Fraction(combinedConstants, combinedCoefficients);
        Fraction yIntersect = inSIForm.getM().multiply(xIntersect).add(inSIForm.getB());
        return new Coordinates(xIntersect, yIntersect);
    }
}
