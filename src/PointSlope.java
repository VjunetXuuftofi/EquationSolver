/**
 * Created by thomaswoodside on 4/26/16.
 */
public class PointSlope {
    private Fraction x;
    private Fraction y;
    private Fraction m;

    public PointSlope (Fraction x, Fraction y, Fraction slope)
    {
        this.x = x;
        this.y = y;
        this.m = slope;
    }
    public SlopeIntercept toSlopeIntercept()
    {
        Fraction b = x.multiply(m).add(y.multiply(-1));
        return new SlopeIntercept(m, b);
    }
    public StandardForm toStandardForm()
    {
        return toSlopeIntercept().toStandardForm();
    }
}
