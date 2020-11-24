import java.lang.StringBuilder

class dset (ss: String)
{
    var s: String = ss
    var i = 0
    var co = 0
    var ex = 0
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

fun main() {
    var m = mainsolver()
    var l = mutableListOf("7x-y=4") //"-8x^2-67x+79x^2=-69"
    print(m.giveSol(l))
}