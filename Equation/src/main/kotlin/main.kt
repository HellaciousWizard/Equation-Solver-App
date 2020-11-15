import java.lang.Math.sqrt

fun QuadraticEqn(l:List<String>)
{
    var L = mutableListOf<String>()
    var i=0
    while (i<=l.size-1)
    {
        if (i<=l.size-3 && l[i+1] == "^")
        {
            L.add(l[i]+l[i+1]+l[i+2])
            i += 2
        }
        else if(i+1<=l.size-1 && l[i] == "-")
        {
            if(l[i+1].slice(0..0)=="x")
                L.add(l[i]+"1")
            else
            {
                L.add(l[i]+l[i+1])
                i++
            }
        }
        else if(i+1<=l.size-1 && l[i] == "+")
        {
            if (l[i + 1].slice(0..0) == "x")
                L.add(l[i] + "1")
        }
        else
            L.add(l[i])
        i++
    }
    println(L)
    i=0
    var a=0.0
    var b=0.0
    var c=0.0
    while(i<=L.size-1)
    {
        if(i+1<L.size)
        {
            if(L[i+1]=="x^2")
                a=L[i].toDouble()
            else if(L[i+1]=="x")
                b=L[i].toDouble()
            else if(L[i]=="=")
                c=-(L[i+1].toDouble())
        }
        i++
    }
    var d = 0.0
    var x1 = 0.0
    var x2 = 0.0
    d = b * b - 4 * a * c
    when {
        d<0 -> println("\nComplex solution")
        d == 0.0 -> println("\nValue of x:"+(-b/(2*a)))
        else -> {
            x1 = (-b + kotlin.math.sqrt(d)) / (2 * a)
            x2 = (-b - kotlin.math.sqrt(d)) / (2 * a)
            println("Value of x1:$x1")
            print("Value of x2:$x2")
        }
    }
}
fun main(args : Array<String>)
{
    print("\nEnter the equation: ")

    var equation = readLine()
    var st = equation
    var sb = String()
    val str = sb.toString()
    var i = 0
    var t = 0

    print("\nGiven Equation is: $st")

    if (equation != null)
    {
        for(ii in equation!!.indices)
        {
            if(equation[i].isLetter() || equation[i]==('+') || equation[i]==('-') || equation[i]==('*') || equation[i]==('/') || equation[i]==('^') || equation[i]==('(') || equation[i]==(')') || equation[i]==('{') || equation[i]==('}') || equation[i]==('[') || equation[i]==(']') || equation[i]==('>') || equation[i]==('<')|| equation[i]==('='))
            {
                if(i>3)
                {
                    if(equation.substring(i-3,i)=="sep")
                    {
                        equation = equation.substring(0, i) + equation[i] + "sep" + equation.substring(i + 1, equation.length)
                        i += 3
                    }
                    else
                    {
                        equation=equation.substring(0,i)+"sep${equation[i]}sep"+equation.substring(i+1,equation.length)
                        i+=6
                    }

                }
                else
                {
                    equation=equation.substring(0,i)+"sep${equation[i]}sep"+equation.substring(i+1,equation.length)
                    i+=6
                }
            }

            i++
        }
    }

    var eq1 = equation!!.split("sep")
    print("\n$eq1")

    QuadraticEqn(eq1)

    for(i in equation.indices)
    {
        if(equation[i]=='^')
            t++
    }
    if(t>0)
        print("\nQuadratic equation")
    else
        print("\nLinear Equation")
}