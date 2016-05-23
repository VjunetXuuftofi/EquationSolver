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
        //If two ints are given
        if (denominator == 0)
        {
            System.out.println("Denominators cannot be zero.");
            myNumer = 0;
            myNumer = 1;
        }
        else
        {
            myNumer = numerator;
            myDenom = denominator;
            simplify();
        }
    }

    public Fraction(int numerator)
    {
        //Alternate constructor handles whole numbers
        myNumer = numerator;
        myDenom = 1;
        simplify();
    }
    public Fraction(Fraction numerator, Fraction denominator)
    {
        //If the numerator and denominator are both Fractions.
        myNumer = numerator.getMyNumer() * denominator.getMyDenom();
        myDenom = numerator.getMyDenom() * denominator.getMyNumer();
        simplify();
    }

    private Fraction(double numerator, double denominator) // Only used by sqrt
    {
        // A constructor to be used where doubles are given. gcd() only works with integers, so the doubles
        // are made bigger to preserve precision
        final int largestRoundInt = 1000000;
        Fraction f = new Fraction((int) (numerator*largestRoundInt), (int) (denominator*largestRoundInt)); //Highest precision available with ints
        myNumer = f.getMyNumer();
        myDenom = f.getMyDenom();
    }

    public Fraction (String s)
    {
        //Instantiates a Fraction given a string such as "2/3".
        if (s.equals(""))
        {
            //Where the coefficient is implied (e.g. y = x + 2)
            myNumer = 1;
            myDenom = 1;
        }
        else if (s.equals("-"))
        {
            myNumer = -1;
            myDenom = 1;
        }
        else if (s.equals("+"))
        {
            myNumer = 1;
            myDenom = 1;
        }
        else if (s.contains("/"))
        {
            //Improper fractions
            myNumer = Integer.parseInt(s.substring(0, s.indexOf("/")));
            myDenom = Integer.parseInt(s.substring(s.indexOf("/")+1));
        }
        else
        {
            //Whole numbers
            myNumer = Integer.parseInt(s);
            myDenom = 1;
        }
        simplify();
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

    //Equality Method


    public boolean equals(Fraction f)
    {
        //Comparison method
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

    private Fraction reciprocal()
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
        //Multiplies a fraction with an integer and returns the result
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
        //Divides one fraction by an int and returns the result
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

    public int toInt()
    {
        //Converts a Fraction into an integer where necessary (for instance in graphing).
        return myNumer / myDenom;
    }

    public Fraction subtract(Fraction f)
    {
        //Subtracts one fraction from another and returns the result.
        if (myDenom == f.getMyDenom())
        {
            return new Fraction(myNumer - f.getMyNumer(), myDenom);
        }
        else
        {
            int numer = myNumer * f.getMyDenom() - f.getMyNumer() * myDenom;
            int denom = myDenom * f.getMyDenom();
            return new Fraction(numer, denom);
        }
    }
    public Fraction subtract(int i)
    {
        //Subtracts an int from a fraction and returns the result.
        return new Fraction(myNumer - i * myDenom, myDenom);
    }
    public Fraction add(int i)
    {
        //Adds one fraction with an int and returns the result.
        return new Fraction(myNumer + i * myDenom, myDenom);
    }
    public boolean isOne()
    {
        //Determines whether a Fraction is equal to one or negative one (for display purposes
        // in certain equations).
        return Math.abs(myDenom) == 1 && Math.abs(myNumer) == 1;
    }
    public double toDouble()
    {
        return 1.*myNumer/myDenom;
    }

    public Fraction sqrt()
    {
        return new Fraction(Math.sqrt(myNumer), Math.sqrt(myDenom));
    }
    public boolean isLessThan(int i)
    {
        //Compares the fraction with an integer and returns the result.
        return myNumer/myDenom < i;
    }
    public boolean isMoreThan(int i)
    {
        //Compares the fraction with an integer and returns the result.
        return myNumer/myDenom > i;
    }
    public boolean isMoreThan(Fraction f)
    {
        //Compares the fraction with an integer and returns the result.
        return myNumer/myDenom > f.getMyNumer()/f.getMyDenom();
    }
    public Fraction abs()
    {
        return new Fraction(Math.abs(myNumer), myDenom);
    }

}

