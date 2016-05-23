/**
 * ConstantXLine.java
 * Last Modified May 5 2016
 * Thomas Woodside
 * Stores information for equations of lines where the x coordinate is constant.
 */
class ConstantXLine extends Form
{
    private Fraction x;

    public Fraction getX()
    {
        return x;
    }

    public ConstantXLine(Fraction x)
    {
        //Only one data member to assign : the constant x coordinate.
        this.x = x;

    }
}
