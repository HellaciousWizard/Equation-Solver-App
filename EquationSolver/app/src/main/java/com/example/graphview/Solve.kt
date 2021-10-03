package com.example.graphview

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.android.synthetic.main.solve_g.*
import java.lang.Math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

class Solve: AppCompatActivity() {
    lateinit var decor: View
    var coeff = mutableListOf<Double>()
    var unknwn = mutableListOf<String>()
    var ms = mainsolver()
    var iv: ImageView? = null
    var btn: Button? = null
    var s1 = ""
    var s2 = ""
    var upper_limit = 0.0
    var lower_limit = 0.0

    fun isString(s: String): Boolean {
        var flag = true
        for (i in s) {
            if (!i.isLetter()) {
                flag = false
                break
            }
        }
        return flag
    }

    fun QuadraticEqn(l: List<String>) : String{
        var L = mutableListOf<String>()
        var i = 0
        if (l[0] == "x")
            L.add("1")

        /* Converting String List into Coefficient and unknowns */

        while (i <= l.size - 1) {
            if (i <= l.size - 3 && l[i + 1] == "^") {
                L.add(l[i] + l[i + 1] + l[i + 2])
                i += 2
            } else if (i + 1 <= l.size - 1 && l[i] == "-") {
                if (isString(l[i + 1]))
                    L.add(l[i] + "1")
                else {
                    L.add(l[i] + l[i + 1])
                    i++
                }
            } else if (i + 1 <= l.size - 1 && l[i] == "+") {
                if (l[i + 1].slice(0..0) == "x")
                    L.add(l[i] + "1")
            } else
                L.add(l[i])
            i++
        }

        /* Getting unknowns and removing if any */

        i = 0
        while (i <= L.size - 1) {
            if (i + 1 < L.size && i - 1 >= 0) {
                if (L[i] == "x^2") {
                    if (isString(L[i + 1])) {
                        unknwn.add(L[i + 1])
                        L.removeAt(i + 1)
                    } else if (isString(L[i - 1])) {
                        unknwn.add(L[i - 1])
                        L.removeAt(i - 1)
                    } else
                        unknwn.add("")
                } else if (L[i] == "x") {
                    if (isString(L[i + 1])) {
                        unknwn.add(L[i + 1])
                        L.removeAt(i + 1)
                    } else if (isString(L[i - 1])) {
                        unknwn.add(L[i - 1])
                        L.removeAt(i - 1)
                    } else
                        unknwn.add("")
                } else if (L[i] == "=") {
                    if (isString(L[i + 1])) {
                        unknwn.add(L[i + 1])
                        L.removeAt(i + 1)
                    } else if (isString(L[i - 1])) {
                        unknwn.add(L[i - 1])
                        L.removeAt(i - 1)
                    } else
                        unknwn.add("")
                }
            }
            i++
        }

//    println(L)

        /* Setting value of a,b and c*/
        i = 0
        var a = 0.0
        var b = 0.0
        var c = 0.0

        while (i <= L.size - 1) {
            if (i + 1 < L.size) {
                if (L[i + 1] == "x^2") {
                    try {
                        a = L[i].toDouble()
                    } catch (e: NumberFormatException) {
                    }
                } else if (L[i + 1] == "x") {
                    try {
                        b = L[i].toDouble()
                    } catch (e: NumberFormatException) {
                    }
                } else if (L[i] == "=") {
                    try {
                        c = L[i - 1].toDouble()
                    } catch (e: NumberFormatException) {
                    }
                }
            }
            i++
        }

        var x1: Double
        var x2: Double
        var u: Double
        var d = b * b - 4 * a * c
        coeff.clear()
        coeff.add(a)
        coeff.add(b)
        coeff.add(c)
        //print(coeff)

        when {
            d < 0 -> {
                if (b != 0.0) {
                    u = (-b / (2 * a)).pow(2) - (c / a)
                    u = sqrt(abs(u))
                    b = -b / (2 * a)
                    b = Math.round(b * 1000.0) / 1000.0
                    u = Math.round(u * 1000.0) / 1000.0
                    return "First Root = $b + ${u}i \nSecond Root = $b - ${u}i"
                } else {
                    u = sqrt(c / a)
                    u = Math.round(u * 1000.0) / 1000.0
                    return "First Root = $b + ${u}i \nSecond Root = $b - ${u}i"
                }
            }
            d == 0.0 -> return "X: ${Math.round((-b / (2 * a)) * 1000.0) / 1000.0}"
            else -> {
                x1 = (-b + kotlin.math.sqrt(d)) / (2 * a)
                x2 = (-b - kotlin.math.sqrt(d)) / (2 * a)
                return "First Root = ${Math.round(x1 * 1000.0) / 1000.0} \nSecond Root = ${Math.round(x2 * 1000.0) / 1000.0}"
            }
        }
        return "0"
    }

    fun StrtoList(s: String): MutableList<String> {
        var st = ""
        var i = 0
        var L = mutableListOf<String>()

        while (i < s.length) {
            if (s[i].isDigit() || s[i] == '.')
                st += s[i]
            else {
                if (st !== "") {
                    L.add(st)
                    st = ""
                }
                L.add(s[i].toString())
            }
            if (st !== "" && i == s.length - 1) {
                L.add(st)
                st = ""
            }
            i++
        }
        return L
    }

    fun Type(s: String): String {
        var i = 0
        var p = 0
        while (i < s.length) {
            if (s[i] === '^' && Character.getNumericValue(s[i + 1]) > p)
                p = Character.getNumericValue(s[i + 1])
            i++
        }
        if (p === 2)
            return "Quadratic"
        else if (p > 2)
            return "Polynomial"
        else
            return "Linear"
    }

    fun getCoFactor(A: Array<Array<Double?>>, temp: Array<Array<Double?>>, p: Int, q: Int, n: Int){
        var i = 0
        var j = 0
        for (row in 0 until n){
            for (col in 0 until n){
                if (row != p && col != q){
                    temp[i][j] = A[row][col]
                    j++
                    if (j == n-1){
                        j=0
                        i++
                    }
                }
            }
        }
    }

    fun determinant(A: Array<Array<Double?>>,n: Int): Double{
        var D = 0.0
        if (n==1)
            return A[0][0]!!

        var temp = Array<Array<Double?>>(n){ arrayOfNulls(n) }

        var sign = 1

        for (f in 0 until n){
            getCoFactor(A, temp, 0 ,f ,n)
            D += sign * A[0][f]!! * determinant(temp,n-1)
            sign*=-1
        }

        return D
    }

    fun adjoint(A: Array<Array<Double?>>,N: Int,adj: Array<Array<Double?>>): Int{
        if (N == 1){
            adj[0][0] = 1.0
            return 0
        }

        var sign = 1
        var temp = Array<Array<Double?>>(N){ arrayOfNulls(N) }

        for (i in 0 until N){
            for (j in 0 until N){
                getCoFactor(A, temp, i, j, N)
                sign = if ((i+j)%2==0)
                    1
                else
                    -1
                adj[j][i] = sign * determinant(temp, N-1)
            }
        }
        return 0
    }

    fun mat_mul(inv: Array<Array<Double?>>, A: Array<Array<Double?>>,B: Array<Double?>,N: Int): MutableList<Double?>{
        /*
        l=[]
        det = determinant(A,N)
        for i in inv:
            s=0
            c=0
            for j in i:
                s=s+j*B[c]
                c=c+1
            l.append(s/det)
        return(l)
         */
        var l = mutableListOf<Double?>()
        var det = determinant(A, N)
        var s: Double
        var c: Int
        for (i in inv){
            s = 0.0
            c = 0
            for (j in i)
                s+=j!! * B[c++]!!

            l.add(round(s/det*1000.0)/1000.0)
        }
        return l
    }

    fun LinearEqn(l:List<String>): MutableList<Double?> {
        var L = mutableListOf<Double>()
        var A = mutableListOf<Double>()
        var B = mutableListOf<Double>()
        var ans = mutableListOf<Double?>()
        var s = ""
        var j = 0
        var c = 0
        var neqn = 0
        var x = 0.0
        var y = 0.0
        var p : MutableList<Double?>

        /* To Calculate number of equation(neqn) */
        for (i in l)
        {
            /* To Calculate number of unknown(c) */
            while (j < i.length)
            {
                if (i[j].isLetter() && !(i[j + 1].isLetter()))
                    c++
                j++
            }
            neqn++
        }

        for (i in l)
        {
            j = 0
            while (i[j] != '=')
            {
                if (i[j].isDigit() || i[j] == '.' || (i[j] == '-' && i[j + 1].isDigit()))
                    s += i[j]
                else
                {
                    if (s != "")
                    {
                        L.add(s.toDouble())
                        s = ""
                    }
                }
                j++
            }
            if (s != "")
            {
                L.add(s.toDouble())
                s = ""
            }
        }

        coeff.clear()
        coeff = L

        if (neqn == c)
        {
            var i = 0
            while (i < L.size)
            {
                if (i % (c + 1) != c)
                    A.add(L[i])
                else
                    B.add(L[i] * (-1))
                i += 1
            }

            var AA = A
            var BB = B

            var N = sqrt(AA.size.toDouble()).toInt()
            var A = Array<Array<Double?>>(N) { arrayOfNulls(N) }
            var B = Array<Double?>(N) { 0.0 }

            for (i in BB.indices)
                B[i] = BB[i]

            for (i in 0 until N)
                for (j in 0 until N)
                    A[i][j] = AA[(i * N) + j]

            if (determinant(A, N) != 0.0)
            {
                var adj = Array<Array<Double?>>(N) { arrayOfNulls(N) }
                adjoint(A, N, adj)

                ans  = mat_mul(adj, A, B, N)
            }
            else
                Toast.makeText(this,"Either inconsistent or infinite solutions",Toast.LENGTH_LONG).show()
        }
        return ans
    }

    fun DigitCount(n:Double): Int
    {
        var c = 0
        var t = n.toInt()
        while(t != 0)
        {
            c++
            t /= 10
        }
        return c
    }
    
    fun Point(l:List<String>): MutableList<Double?>
    {
        var L = mutableListOf<Double?>()
        var mp = LinearEqn(l)
        var d : Double
        if(mp.size != 0)
            d = mp[0]!!
        else
            d = -coeff[2] / coeff[0]

        d += ((5.0.pow(DigitCount(d).toDouble())) / d)
        if(coeff[1]!=0.0) {
            if (-(d) <= d) {
                L.add(-(d))
                L.add(d)
            } else {
                L.add(d)
                L.add(-(d))
            }
        }
        else
        {
            if (-(d) <= d) {
                L.add(-(d/2))
                L.add(d*2)
            } else {
                L.add(d*2)
                L.add(-(d/2))
            }
        }
        return L
    }

    fun Graph(l:List<String>): MutableList<String>
    {
        var t : String
        var sum : Double
        var X = mutableListOf<String>()
        var Y = mutableListOf<String>()
        var G = mutableListOf<String>()
        var j : Int
        var c = 0
        var p = mutableListOf<Double?>()
        for(L in l)
        {
            t = Type(L)
            if (t == "Quadratic")
            {
                QuadraticEqn(StrtoList(L))
                var i = -5.0
                while (i <= 5.0)
                {
                    sum = 0.0
                    j = 0
                    while (j < coeff.size)
                    {
                        when (j)
                        {
                            0 -> sum += (coeff[j] * i * i)
                            1 -> sum += (coeff[j] * i)
                            2 -> sum += coeff[j]
                        }
                        j++
                    }
                    X.add(i.toString())
                    Y.add(sum.toString())
                    i += 0.5
                }
                G.add(X.joinToString(","))
                G.add(Y.joinToString(","))
                return G
            }
            else if (t == "Linear")
            {
                var x = mutableListOf<String>()
                var y = mutableListOf<String>()
                var u = 0
                var i = 0

                while (i < L.length)
                {
                    if(L[i].isLetter())
                        u++
                    i++
                }

                if(u == 1)
                {
                    p = Point(l)
                    var const = LinearEqn(mutableListOf(L))[0]
                    var i = p[0]!!

                    while (i <= p[1]!!)
                    {
                        x.add((round(const!! * 1000.0) / 1000.0).toString())
                        y.add((round((coeff[0] * i + coeff[1]) * 1000.0) / 1000.0).toString())
                        i += 0.5
                    }
                    c++
                    X.add(x.joinToString(","))
                    Y.add(y.joinToString(","))
                }
                else if(u == 2)
                {
                    if (c == 0)
                    {
                        p=Point(l)
                        LinearEqn(l)
                    }
                    if(coeff[1] != 0.0)
                    {
                        var i = p[0]!!
                        while (i <= p[1]!!) {
                            x.add((round(i * 1000.0) / 1000.0).toString())
                            y.add((((round((coeff[0] * i + coeff[2]) * 1000.0) / 1000.0) * -(coeff[1])).toString()))

                            i += 0.5
                        }
                    }
                    else{
                        var const = LinearEqn(l)[0]
                        var i = p[0]!!
                        while (i <= p[1]!!) {
                            x.add((round(const!! * 1000.0) / 1000.0).toString())
                            y.add((((round((coeff[0] * i + coeff[2]) * 1000.0) / 1000.0)).toString()))
                            i += 0.5
                        }
                    }
                    c++
                    if (c != l.size)
                    {
                        coeff.remove(coeff[0])
                        coeff.remove(coeff[0])
                        coeff.remove(coeff[0])
                    }
                    X.add(x.joinToString(","))
                    Y.add(y.joinToString(","))
                }

            }
        }
        G.add(X.joinToString("|"))
        G.add(Y.joinToString("|"))
        return G
    }

    fun returnimage(s:String) {

        /*upper_limit = 10.0
        lower_limit = -10.0
        s1 += java.lang.Double.toString(lower_limit)
        s2 += Math.pow(lower_limit, 3.0)
        var i = lower_limit + 1
        while (i <= upper_limit) {
            s1 += ",$i"
            s2 += "," + Math.pow(i, 3.0)
            i++
        }*/
        //Log.d("Error find", "oncreate")
        //iv = (R.id.image_view) as ImageView?
        //btn = (R.id.button) as Button?
        //Log.d("Error find", "before python")
        //PYTHON STARTED FROM HERE
        if (!Python.isStarted()) Python.start(AndroidPlatform(this))
        //Log.d("Error find", "python started")
        //tvw2.text="python started"
        val py = Python.getInstance()
        //Log.d("Error find", "instance created")
        //tvw2.text="instance created"
        //Python object to load script
        val pyo = py.getModule("GraphView")
        var obj = py.getModule("GraphView")
        //Log.d("S1 size",2)
        if (s!="[1.0x+-0.0=0]" || s!="[-1.0x+-0.0=0]" || s!= "[-1.0y+-0.0=0]" || s!="[1.0y+-0.0=0]") {
            var count = 1
            for (i in s1) {
                if (i == '|')
                    count++
            }
            obj = pyo.callAttr("Graph_Img", s, s1, s2, count)
        }
        else
        {
            if(s!="[1.0x+-0.0=0]" || s!="[-1.0x+-0.0=0]" )
                obj = pyo.callAttr("Graph_Img", "1x=0", "", "", 0)
            else if(s!= "[-1.0y+-0.0=0]" || s!="[1.0y+-0.0=0]")
                obj = pyo.callAttr("Graph_Img", "1y=0", "", "", 0)
        }

        //mutableListOf<String>(s1)
        //mutableListOf<String>(s2)
        val str = obj.toString()
        val data = Base64.decode(str, Base64.DEFAULT)
        val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
        iview.setImageBitmap(bmp)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solve_g)
        decor = window.decorView
        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        decor.setOnSystemUiVisibilityChangeListener {
            if (it == 0)
                decor.systemUiVisibility = hide()
        }
        etxt2.setText(intent.getStringExtra("eqn"))

        var eqn: MutableList<Double?>
        var li: String
        var eqlist = mutableListOf<String>()
        var charseT = intent.getStringExtra("chrset")!!.split(",").toMutableList()

        //eqn = etxt2.text.toString()
        //ms = mainsolver()
        //eqlist = eqn.split(",").toMutableList()

        eqlist = intent.getStringExtra("eqlist")!!.split(",").toMutableList()

        Log.d("Eq", (eqlist[0] != "1.0x+-0.0=0").toString())
        //if (tvw2.text == "Fake Equation")
        //  tvw.text = tvw2.text
        if ((eqlist[0] != "1.0x+-0.0=0" || eqlist[0] != "-1.0x+-0.0=0" || eqlist[0] != "-1.0y+-0.0=0")) {
            Log.d("true","true")
            if (Type(eqlist[0]) == "Linear") {
                //eqlist = ms.giveSol(eqlist, "Linear")

                if (eqlist.size < 3) {

                    Log.d("Points -> ", eqlist.toString())
                    var lp = Graph(eqlist)
                    s1 = lp[0]//"-5.0,-4.0,-3.0,-2.0,-1.0,0.0,1.0,2.0,3.0,4.0,5.0"
                    s2 = lp[1]//"-25.0,-16.0,-9.0,-4.0,-1.0,0.0,1.0,4.0,9.0,16.0,25.0"
                    Log.d("s1", s1.toString())
                    Log.d("s2", s2.toString())
                    iview.setBackgroundResource(R.drawable.ic_launcher_background)
                    returnimage(eqlist.joinToString(","))
                }
                eqn = LinearEqn(eqlist)//eqn.split(",").toString()
                li = ""
                for (i in eqn.indices) {
                    li += "${charseT[i]} = ${eqn[i]}\n"
                }
                tvw.text = li
                //tvw2.text = "${eqlist[0]} \nPoints -> $lp"
            } else if (Type(eqlist[0]) == "Quadratic") {
                //eqlist = ms.giveSol(eqlist,"Quadratic")
                Log.d("Points -> ", eqlist.toString())

                var lp = Graph(eqlist)
                s1 = lp[0]//"-5.0,-4.0,-3.0,-2.0,-1.0,0.0,1.0,2.0,3.0,4.0,5.0"
                s2 = lp[1]//"-25.0,-16.0,-9.0,-4.0,-1.0,0.0,1.0,4.0,9.0,16.0,25.0"
                Log.d("s1", s1.toString())
                Log.d("s2", s2.toString())
                iview.setBackgroundResource(R.drawable.ic_launcher_background)
                returnimage(eqlist.joinToString(","))
                //tvw2.text = "${eqlist[0]} \nPoints -> $s2"

                tvw.text = QuadraticEqn(StrtoList(eqlist[0]))//Graph(StrtoList(eqlist[0]))
            }
        }
        else
        {
            Log.d("false","false")
            tvw.text = eqlist[0]
            iview.setBackgroundResource(R.drawable.ic_launcher_background)
            returnimage(eqlist.joinToString(","))
        }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            decor.systemUiVisibility = hide()
        }
    }

    fun hide(): Int{
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}