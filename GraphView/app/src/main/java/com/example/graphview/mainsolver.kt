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
    var customerr = "0"

    private fun isOk(s: String): String{   //Checks if the given expression is okay to process
        if (s.isEmpty()){
            return "Blank Submission is not allowed"
        }
        if(s.contains("/") || s.contains("*"))
        {
            return "Invalid symbols used"
        }
        if (s[0]=='='){
            //print("1\n")
            return "LHS is missing"
        }
        if (s.filter { it == '=' }.count() != 1) {
            //print("2\n")
            return "Too many or too few =s in one equation"
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
        sb.clear()

        rhs = emptyList<dset>().toMutableList()
        i = 1
        sb.append(l[1][0])
        while (i<l[1].length){
            //print(l[1][i].toString()+ "  [" + sb.toString() + "] \n")
            if (l[1][i]=='+' || (l[1][i]=='-' && l[1][i-1]!='(')/*Add more symbol checks here if needed*/) {
                rhs.add(dset(sb.toString()))
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

        var hi = 0
        while (hi<lhs.size) {   //joining similar terms
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

        hi = 0                      //Removing all the 0 terms
        while (hi<lhs.size){
            if (lhs[hi].co == 0F){
                lhs.removeAt(hi)
                if (hi<lhs.size)
                    lhs.removeAt(hi)
            }
            else
                hi+=2
        }

        //lhs.forEach{
           // print(it.tx() + " ")
        //}
        //eqtyp = gettyp(lhs)
        //print("  =  0 In fixing\n")
        dlist.add(lhs)
        dlist.last().forEach{
            //print(it.tx() + " ")
            if (!cset.contains(it.va))
                cset.add(it.va)
        }
        var iii = 0
        cset.forEach {
            if (it.isLetter())
                iii++
        }
        if (gettyp(lhs) == "Not Linear" && iii>1)
            customerr = "Too many variables in quadratic equation"
        iii=0
        lhs.forEach {
            if (it.typ.contains("va"))
                iii=1
        }
        if (iii==0)
            customerr = "No variable provided"
        //print("  =  0 In fixing\n")
        //println(cset)
    }

    fun getstrl(flag:Boolean): MutableList<String>{
        var hh = 1
        var hi: Int         //Iterator
        var hj: Int         //Iterator
        var sb = StringBuilder()
        var dd: dset
        var retdis = mutableListOf<String>()
        var pset = mutableListOf<Char>()            //Private set of variables

        cset.remove('~')

        eqtyp = gettyp(dlist[0])
        dlist.forEach {     //This to add spare parts which is needed
            //print("cset -> ")
            //println(cset)
            pset.plusAssign(cset)

            for (ii in it){
                if (ii.typ.contains("va") && pset.contains(ii.va))
                    pset.remove(ii.va)
            }

            if (!flag) {
                for (ii in pset) {
                    it.add(dset("+"))
                    it.add(dset("0.0$ii"))
                }
            }
            //print("pset -> ")
            //println(pset)
            pset.clear()

            if (eqtyp != gettyp(it))
                return mutableListOf("Equation types must be same","0")

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

            else if (eqtyp == "Not Linear") {        //Change This too
                hi = 0
                while (hi < it.size) {             //Bubble sort according to variable ascii in ascending order
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

            if (sb.last() == '+')
                sb.append("0.0=0")
            else if (hh == 0 || flag)
                sb.append("=0")
            else
                sb.append("+0.0=0")
            retdis.add(sb.toString())
            sb.clear()
        }
        return retdis
    }

    fun gettyp(ml: MutableList<dset>): String{
        var hi = 0
        for (ii in ml){
            if (ii.ex > hi)
                hi = ii.ex
        }
        return if (hi == 1)
            "Linear"
        else
            "Not Linear"
    }

    fun giveSol(elist: MutableList<String>,flag:Boolean): MutableList<String> {
        //return elist.size.toString()+" Nos."
        dlist.clear()
        cset.clear()
        for (i in elist.indices) {
            elist[i] = elist[i].replace("\\s".toRegex(), "")      //Remove Spaces
            if (isOk(elist[i])=="1") {
                customerr = "0"
                fixeqn(elist[i])
                if (customerr != "0")
                    return mutableListOf(customerr,"0")
            }
            else
                return mutableListOf(isOk(elist[i]),"0")
        }
        if (elist.size<cset.size-1)
            return mutableListOf("Too few equations","0")//might be false
        //for (ii in dlist){
            //ii.forEach {
            //    print(it.tx() + "  ")
            //}
           // println()
        //}
        //if (dlist.isEmpty())
            //print("Empty")
        return getstrl(flag)
    }
}