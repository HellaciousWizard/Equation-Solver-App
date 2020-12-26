package com.example.graphview

import java.lang.StringBuilder


class dset (ss: String)             //dataset
{
    var s = ss
    var typ = mutableListOf<String>()
    var va = '~'                    //Variable Name
    var op = '~'                    //Operator
    var co = 0F                      //Coefficient
    var ex = 0                      //Exponent/Order
    init {
        //print("-->" + ss + "\n")
        if (ss.length == 1 && !ss[0].isDigit() && !ss[0].isLetter()){
            typ.add("op")                                  //Operator
            op = ss[0]
        }
        else {
            var i = 0
            while (i<ss.length) {                       //Sequence #1
                if (ss[i].isDigit() || s[i] == '-' || s[i] == '.')
                    i++//co = co * 10 + ss[i++].toInt() - 48
                else
                    break
            }
            if (i!=0) {
                typ.add("co")
                if (s[0]=='-' && i==1)
                    co = -1F
                else {                             //Constant
                    co = ss.substring(0, i).toFloat()
                }
            }
            if (i != ss.length) {                  //Sequence #2
                if (ss[i].isLetter()) {
                    typ.add("va")
                    if (!typ.contains("co")) {
                        typ.add("co")
                        co = 1F
                    }
                    va = ss[i++]
                }
            }
            if (i != ss.length) {                       //Sequence #3
                if (ss[i++] == '^') {
                    typ.add("ex")                        //Exponent
                    while (i != s.length)
                        ex = ex * 10 + ss[i++].toInt() - 48
                }
            }
            else if (typ.contains("va")){
                //typ.add("ex")
                ex=1
            }
        }
    }
    fun tx(): String{
        if (typ[0] != "op")
            return "$typ[$co][$va][$ex]"
        return "$typ[$op]"
    }
}

class mainsolver {
    private var estr = ""
    lateinit var lhs: MutableList<dset>
    lateinit var rhs: MutableList<dset>
    var dlist = mutableListOf<MutableList<dset>>()
    var cset = mutableListOf<Char>()            //Common set of variables
    var eqtyp = ""

    private fun isOk(s: String): String{   //Checks if the given expression is okay to process
        if (s.isBlank()){
            return "Blank Submission is not allowed"
        }
        if (!s.contains("=")){
            return "= missing"
        }
        if (s[0]=='='){
            //print("1\n")
            return "LHS is missing"
        }
        if (s.filter { it == '=' }.count() != 1) {
            //print("2\n")
            return "Too many =s in one equation"
        }
        if (s[0] == '+' || s[s.length-1] == '+' || s[s.length-1] == '-' || s[s.length-1] == '=') {
            //print("3\n")
            if (s[s.length-1] == '=')
                return "RHS is missing"
            else
                return "Equation does not start or end with symbols"
        }
        if (s[s.indexOf("=")-1] == '+' || s[s.indexOf("=")+1] == '+') {
            //print("4\n")
            return "Symbols are not allowed before or after = sign"
        }
        var cit = 0
        s.forEach {
            if (it.isLetter())
                cit=1
        }
        if (cit==0){
            //print("5\n")
            return "No variable provided"
        }
        return "1"
    }

    fun fixeqn(s: String){     //This fixes a given equation to a general form

        var sb = StringBuilder()
        var ch: Char
        var hh: Int
        var hj: Int

        val l = s.split("=").toMutableList()  //l contains the strings of lhs and rhs separately
        var ll = mutableListOf<Int>()

        lhs = emptyList<dset>().toMutableList()
        var i = 1
        sb.append(l[0][0])
        while (i<l[0].length){
            //print(l[0][i].toString()+ "  [" + sb.toString() + "] \n")
            if (l[0][i]=='+' || (l[0][i]=='-' && l[0][i-1]!='(')/*Add more symbol checks here if needed*/) {
                lhs.add(dset(sb.toString()))
                if (!cset.contains(lhs.last().va))
                    cset.add(lhs.last().va)
                lhs.add(dset("+"))
                sb.clear()
                if (l[0][i]=='-')
                    sb.append(l[0][i])
            }
            else {
                sb.append(l[0][i])
            }
            i++
        }
        lhs.add(dset(sb.toString()))
        if (!cset.contains(lhs.last().va))
            cset.add(lhs.last().va)
        sb.clear()

        rhs = emptyList<dset>().toMutableList()
        i = 1
        sb.append(l[1][0])
        while (i<l[1].length){
            //print(l[1][i].toString()+ "  [" + sb.toString() + "] \n")
            if (l[1][i]=='+' || (l[1][i]=='-' && l[1][i-1]!='(')/*Add more symbol checks here if needed*/) {
                rhs.add(dset(sb.toString()))
                if (!cset.contains(rhs.last().va))
                    cset.add(rhs.last().va)
                rhs.add(dset("+"))
                sb.clear()
                if (l[1][i]=='-')
                    sb.append(l[1][i])
            }
            else {
                sb.append(l[1][i])
            }
            i++
        }
        rhs.add(dset(sb.toString()))
        if (!cset.contains(rhs.last().va))
            cset.add(rhs.last().va)
        sb.clear()

        lhs.add(dset("+")) // Shifting the stuff from rhs to lhs
        rhs.forEach {
            it.co *= -1
            lhs.add(it)
        }

        /*lhs.forEach {
            if (it.typ.contains("ex") && !hh.contains(it.ex)){
                    hh.add(it.ex)
            }
        }*/

        var hi = 0 //Joining similar terms
        while (hi<lhs.size) {
            hh = lhs[hi].ex
            ch = lhs[hi].va
            hj = hi + 2
            while (hj<lhs.size){
                if (lhs[hj].ex==hh && lhs[hj].va==ch) {
                    lhs[hi].co += lhs[hj].co
                    lhs.removeAt(hj)
                    lhs.removeAt(hj-1)
                }
                else
                    hj+=2
            }
            hi+=2
        }

        //println(cset)
        //lhs.forEach{
        //print(it.tx() + " ")
        //}
        //print("  =  0 In fixing\n")
        dlist.add(lhs)
        /*dlist[0].forEach{
            print(it.tx() + " ")
        }
        print("  =  0 In fixing\n")*/
    }

    fun getstrl(): MutableList<String>{
        var hh = 1
        var hi: Int         //Iterator
        var hj: Int         //Iterator
        var sb = StringBuilder()
        var dd: dset
        var retdis = mutableListOf<String>()
        var pset = mutableListOf<Char>()            //Private set of variables

        cset.remove('~')

        dlist.forEach {     //This to add spare parts which is needed
            //print("cset -> ")
            //println(cset)
            pset.plusAssign(cset)

            for (ii in it){
                if (ii.typ.contains("va") && pset.contains(ii.va))
                    pset.remove(ii.va)
            }

            for (ii in pset){
                it.add(dset("+"))
                it.add(dset("0.0$ii"))
            }
            //print("pset -> ")
            //println(pset)
            pset.clear()

            if (eqtyp == "Linear") {        //Change this later
                hi = 0
                while (hi < it.size) {       //Bubble sort according to variable ascii in ascending order
                    hj = 0

                    while (hj < (it.size - 2)) {

                        if (it[hj].va >= it[hj + 2].va) {
                            dd = it[hj]
                            it[hj] = it[hj + 2]
                            it[hj + 2] = dd
                        }

                        hj += 2
                    }

                    hi += 2
                }                                   //Sort Upto here
            }

            else if (eqtyp == "Quadratic") {        //Change This too
                hi = 0
                while (hi < it.size) {             //Bubble sort according to exponent in descending order
                    hj = 0

                    while (hj < (it.size - hi - 2)) {
                        if (it[hj].ex <= it[hj + 2].ex) {

                            dd = it[hj]
                            it[hj] = it[hj + 2]
                            it[hj + 2] = dd
                        }

                        hj+=2
                    }
                    if (it[hi].va != '~')
                        it[hi].va = 'x'
                    hi += 2
                }                                   //Sort upto here
            }

            for (ii in it){
                if (ii.va == '~' && !ii.typ.contains("op"))
                    hh = 0
                if (!ii.typ.contains("op")) {
                    if (ii.typ.contains("co"))
                        sb.append(ii.co)
                    if (ii.typ.contains("va"))
                        sb.append(ii.va)
                    if (ii.typ.contains("ex"))
                        sb.append("^${ii.ex}")
                }
                else
                    sb.append(ii.op)
            }
            if (hh == 0)
                sb.append("=0")
            else
                sb.append("+0.0=0")
            retdis.add(sb.toString())
            sb.clear()
        }
        return retdis
    }

    fun giveSol(elist: MutableList<String>, etyp: String): MutableList<String> {
        //return elist.size.toString()+" Nos."
        dlist.clear()
        cset.clear()
        eqtyp = etyp
        for (i in elist.indices) {
            elist[i] = elist[i].replace("\\s".toRegex(), "")      //Remove Spaces
            if (isOk(elist[i])=="1") {
                fixeqn(elist[i])
            }
            else
                return mutableListOf(isOk(elist[i]),"0")
        }
        //for (ii in dlist){
        //   ii.forEach {
        //       print(it.tx() + "  ")
        //    }
        //   println()
        //}
        return getstrl()
    }
}