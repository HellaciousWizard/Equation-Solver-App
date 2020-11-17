import java.lang.Math.sqrt

fun QuadraticEqn(l:List<String>)
{
    var L = mutableListOf<String>()
    var i=0
    if(l[0] == "x")
        L.add("1")
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
    println("\n$L")
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
                c=(L[i-1].toDouble())
        }
        i++
    }
    var d = 0.0
    var x1 = 0.0
    var x2 = 0.0
    d = b * b - 4 * a * c
    println("a:$a")
    println("b:$b")
    println("c:$c")
    println("d:$d")
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

fun StrtoList(s:String): MutableList<String>
{
    var st = ""
    var i = 0
    var L = mutableListOf<String>()
    print("\nGiven Equation is: $s")

    if (s !== null)
    {
        while(i<s.length)
        {
            if(s[i].isDigit() || s[i]==='-' || s[i]==='.')
                st += s[i]
            else
            {
                if(st !== "")
                {
                    L.add(st)
                    st = ""
                }
                L.add(s[i].toString())
            }
            if(st !== "" && (i===s.length-1 || s[i+1]==='-'))
            {
                L.add(st)
                st = ""
            }
            i++
        }
    }
    return L
}

fun Type(s: String): String
{
    var i = 0
    var p = 0
    while (i < s.length)
    {
        if(s[i] === '^' && Character.getNumericValue(s[i+1])>p)
            p = Character.getNumericValue(s[i+1])
        i++
    }
    if (p === 2)
        return "Quadratic"
    else if(p > 2)
        return "Polynomial"
    else
        return "Linear"
}
fun main(args : Array<String>)
{
    var eq = mutableListOf<String>()
    print("\nEnter the equation: ")

    var eqns = mutableListOf<String>()
    var t = readLine().toString()
    while(t.length !== 0)
    {
        eqns.add(t)
        t = readLine().toString()
    }

    for(i in eqns)
    {
        eq = StrtoList(i)
        println("\n$eq")
        print(Type(i))
        if(Type(i) === "Quadratic")
        QuadraticEqn(eq)
    }



    //if(Type(eqns) === "Quadratic")
    //QuadraticEqn(eq)

}