/**
 * Fraction.java
 * Thomas Woodside
 * Last Modified 2/3/16
 * Provides a class to allow simplification and arithmetic on fractions.
 */
public class Fraction
{
    //Instance Data
    private int myNumer; // Stores numerator
    private int myDenom; // Stores denominator


    //Methods

    //Constructor Methods
    public Fraction(int numerator, int denominator)
    {
        //Constructor. Handles division by zero and simplifies.
        if (denominator == 0)
        {
            System.out.println("Denominators cannot be zero.");
            denominator = 1;
        }
        myNumer = numerator;
        myDenom = denominator;
        simplify();
    }

    public Fraction(int numerator)
    {
        //Alternate constructor handles whole numbers
        myNumer = numerator;
        myDenom = 1;
    }

    public Fraction (String s)
    {
        if (s.equals(""))
        {
            myNumer = 1;
            myDenom = 1;
        }
        else if (s.equals("-"))
        {
            myNumer = -1;
            myDenom = 1;
        }
        else if (s.contains("/"))
        {
            myNumer = Integer.parseInt(s.substring(0, s.indexOf("/")));
            myDenom = Integer.parseInt(s.substring(s.indexOf("/")+1));
        }
        else
        {
            myNumer = Integer.parseInt(s);
            myDenom = 1;
        }
    }
    //Get Methods
    public int getMyNumer()
    {

        return myNumer;
    }

    public int getMyDenom()
    {
        return myDenom;
    }

    //Conversion Methods
    public String toString()
    {
        if (myDenom == 1)
        {
            return String.valueOf(myNumer);
        }
        return myNumer + "/" + myDenom;
    }

    public double decimal()
    {
        //Converts the fraction to a double
        return (double) myNumer / myDenom;
    }

    //Equality Method


    public boolean equals(Fraction f)
    {
        return myDenom == f.getMyDenom() && myNumer == f.getMyNumer();

    }

    private void simplify()
    {
        //Simplifies the fraction, handling zero and negative numbers.
        if (myNumer == 0)
        {
            myDenom = 1;
        }
        int greatestcommondivisor = gcd(myNumer, myDenom);
        myNumer /= greatestcommondivisor;
        myDenom /= greatestcommondivisor;
        if (myDenom < 0)
        {
            myDenom *= -1;
            myNumer *= -1;
        }
    }

    public static int gcd(int num1, int num2)
    {
        //Returns the greatest common divisor of the integers passed in
        if (num1 % num2 == 0) return num2;
        return gcd(num2, num1%num2);
    }

    public Fraction reciprocal()
    {
        //returns the reciprocal of the Fraction
        return new Fraction(myDenom, myNumer);
    }

    //Arithmetic Methods
    public Fraction multiply(Fraction f)
    {
        //Multiplies two fractions and returns the result
        int numer = myNumer * f.getMyNumer();
        int denom = myDenom * f.getMyDenom();
        return new Fraction(numer, denom);
    }

    public Fraction multiply(int i)
    {
        //Multiplies two fractions and returns the result
        int numer = myNumer * i;
        return new Fraction(numer, myDenom);
    }

    public Fraction divide(Fraction f)
    {
        //Divides one fraction by another and returns the result
        return multiply(f.reciprocal());
    }

    public Fraction divide(int i)
    {
        //Multiplies two fractions and returns the result
        int denom = myDenom * i;
        return new Fraction(myNumer, denom);
    }

    public Fraction add(Fraction f)
    {
        //Adds two fractions together and returns the result
        if (myDenom == f.getMyDenom())
        {
            return new Fraction(myNumer + f.getMyNumer(), myDenom);
        }
        else
        {
            int numer = myNumer * f.getMyDenom() + f.getMyNumer() * myDenom;
            int denom = myDenom * f.getMyDenom();
            return new Fraction(numer, denom);
        }
    }

    public Fraction subtract(Fraction f)
    {
        //Subtracts one fraction from another and returns the result.
        if (myDenom == f.getMyDenom())
        {
            return new Fraction(myNumer + f.getMyNumer(), myDenom);
        }
        else
        {
            int numer = myNumer * f.getMyDenom() - f.getMyNumer() * myDenom;
            int denom = myDenom * f.getMyDenom();
            return new Fraction(numer, denom);
        }
    }
    public boolean isOne()
    {
        if (Math.abs(myDenom) == 1 && Math.abs(myNumer) == 1)
        {
            return true;
        }
        return false;
    }

}
