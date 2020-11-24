import java.lang.StringBuilder

class mainsolver {
    private var estr = ""
    lateinit var lhs: MutableList<String>
    lateinit var rhs: MutableList<String>

    private fun isOk(s: String): Boolean{
        if (s.filter { it == '=' }.count() != 1) {
            //print("1\n")
            return false
        }
        if (s[0] == '+' || s[s.length-1] == '+' || s[s.length-1] == '-') {
            //print("2\n")
            return false
        }
        return true
    }
    fun stL(s:String): MutableList<String>
    {
        var st = ""
        var L = mutableListOf<String>()

        var i = 0
        while(i<s.length)
        {
            if (s[i].isDigit() || s[i] == '.')
                st += s[i]
            //else if ()
            /*else {
                if (st !== "") {
                    L.add(st)
                    st = ""
                }
                L.add(s[i].toString())
            }
            if (st != "" && (i == s.length - 1 || s[i + 1] == '-')) {
                L.add(st)
                st = ""
            }*/
            i++
        }
        return L
    }

    fun ripNdip(s: String){

        var sb = StringBuilder()

        val l = s.split("=").toMutableList()

        lhs = emptyList<String>().toMutableList()
        var i = 1
        sb.append(l[0][0])
        while (i<l[0].length){
            print(l[0][i].toString()+ "  [" + sb.toString() + "] \n")
            if (l[0][i]=='+' || (l[0][i]=='-' && l[0][i-1]!='(')/*Add more symbol checks here if needed*/) {
                lhs.add(sb.toString())
                lhs.add("+")
                sb.clear()
                if (l[0][i]=='-')
                sb.append(l[0][i])
            }
            else {
                sb.append(l[0][i])
            }
            i++
        }
        lhs.add(sb.toString())

        /*sb.append('[')
        i = 0
        l[0].forEach {
            when (it)
            {
                '+' -> {
                    sb.append(it)
                }
                '-' -> {
                    if (i!=0) {
                        if (l[0][i - 1] != '(' || l[0][i - 1] != '=')
                        sb.append(it)
                    }
                }
                '/' -> {
                    sb.append(it)
                }
            }
            i++
        }
        sb.append(']')
        print(sb.toString()+"\n")
        if (sb.toString() != "[]") {
            lhs = l[0].split(sb.toString().toRegex()).toMutableList()
            i = 1
            for (x in 1 until sb.length-1){
                /*if (sb[x] == '-'){
                    lhs.add(i, sb[x].toString())
                    lhs[i+1] = "-" + lhs[i+1]
                }
                else*/
                lhs.add(i, sb[x].toString())
                i+=2
            }
            lhs.removeIf{o: String? -> o == ""}
        }
        else
            lhs = mutableListOf(l[0])

        sb.clear()
        sb.append('[')
        l[1].forEach {
            when (it)
            {
                '+' -> {
                    sb.append(it)
                }
                '-' -> {
                    sb.append(it)
                }
                '/' -> {
                    sb.append(it)
                }
            }
            i++
        }
        sb.append(']')
        if (sb.toString() != "[]") {
            rhs = l[1].split(sb.toString().toRegex()).toMutableList()
            i = 1
            for (x in 1 until sb.length-1){
                rhs.add(i, sb[x].toString())
                i+=2
            }
            rhs.removeIf{o: String? -> o == ""}
        }
        else
            rhs = mutableListOf(l[0])*/

        print("\n" + lhs.toString() + " = " /*+ rhs.toString()*/)

    }

    fun giveSol(elist: MutableList<String>): String {
        //return elist.size.toString()+" Nos."
        when (elist.size) {
            1 -> {
                elist[0] = elist[0].replace("\\s".toRegex(), "")      //Remove Spaces
                return if (isOk(elist[0])) {
                    ripNdip(elist[0])
                    "This is gonna be da solution!!!"
                }
                    else "Fake Equation"    //Count '=' signs and check if the count is 1
            }
            2 -> {
                /*elist.forEach {
                    it = it.replace("\\s".toRegex(), "")               //Remove Spaces
                    return if (estr.filter { it == '=' }.count() == 1) {       //Count '=' signs and check if the count is 1
                        "This is gonna be da solution!!!"
                    } else
                        "Fake Equation"
                }*/
                return "It is 2"
            }
        }
        return "Nothing Happened"
    }
}