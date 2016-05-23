/**
 * Equation.java
 * Last Modified May 22 2016
 * Thomas Woodside
 * The abstract superclass that specifies what every equation class needs to have.
 */

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.ArrayList;

public abstract class Equation
{
    abstract void extractInfo() throws ValidationException;
    abstract boolean pointOn(Coordinates c);
    abstract Coordinates[] intersectionPoints(Equation e);
    abstract void graph(Graphics g, int low, int high);
    abstract Fraction[] solveX(Fraction y);
    abstract Fraction[] solveY(Fraction x);
    abstract Fraction[] getYIntercepts();
    abstract Fraction[] getXIntercepts();
    abstract ArrayList<Form> getForms();



}
