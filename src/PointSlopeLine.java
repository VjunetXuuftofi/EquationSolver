/**
 * PointSlopeLine.java
 * Thomas Woodside
 * May 21 2016
 * A class to store information of lines in point-slope form:
 * y1 - y2 = m(x1 - x2)
 * Not used for anything other than conversion.
 */
class PointSlopeLine
{
    private Fraction x;
    private Fraction y;
    private Fraction m;

    PointSlopeLine(Fraction x, Fraction y, Fraction slope)
    {
        //Sample equation: y - 3 = -2(x-4)
        this.x = x; // -x2 in the form, e.g. in sample would be -4
        this.y = y; // -y2 in the form, e.g. in sample would be -3
        this.m = slope; // m in the form, e.g. in sample would be -2
    }
    SlopeInterceptLine toSlopeIntercept()
    {
        //Converts the equation into Slope Intercept Form.
        Fraction b = x.multiply(m).add(y.multiply(-1));
        return new SlopeInterceptLine(m, b);
    }
    StandardFormLine toStandardForm()
    {
        return toSlopeIntercept().toStandardForm();
    }
}
