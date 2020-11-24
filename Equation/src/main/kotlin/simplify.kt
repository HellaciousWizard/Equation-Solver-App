import java.lang.StringBuilder

class dset (ss: String)
{
    var s: String = ss
    var i = 0
    var co = 0
    var ex = 0
}

fun main() {
    var m = mainsolver()
    var l = mutableListOf("7x-y=4") //"-8x^2-67x+79x^2=-69"
    print(m.giveSol(l))
}
