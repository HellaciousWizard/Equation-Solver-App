package com.example.graphview

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var decor: View
    lateinit var anim1: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        decor = window.decorView
        var intt: Intent
        var eqnstr: MutableList<String>
        var ms = mainsolver()

        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        decor.setOnSystemUiVisibilityChangeListener {
            if (it == 0)
                decor.systemUiVisibility = hide()
        }

        var iv = findViewById<ImageView>(R.id.iViewDown)
        var tv = findViewById<TextView>(R.id.textView)
        var ett = findViewById<TextView>(R.id.eTXT)
        tv.measure(0,0)
        iv.measure(0,0)
        //tv.text = "${iv.marginTop}+${tv.marginTop}+${tv.measuredHeight}+${fab.measuredHeight}+${iv.measuredHeight}"
        iv.y = (iv.marginTop+tv.marginTop+tv.measuredHeight+iv.measuredHeight).toFloat()
        anim1 = ObjectAnimator.ofFloat(iv, View.Y,(iv.marginTop+tv.marginTop+tv.measuredHeight+iv.measuredHeight+100).toFloat())
        //anim1 = ObjectAnimator.ofFloat(iv, View.Y,100F)
        anim1.repeatMode = ObjectAnimator.REVERSE
        anim1.repeatCount = ObjectAnimator.INFINITE
        anim1.duration = 1000
        anim1.start()
        fab.setOnClickListener{
            eqnstr = eTXT.text.toString().split(",").toMutableList()
            eqnstr = ms.giveSol(eqnstr,Type(eqnstr[0]))
            if (eqnstr.contains("0")){
                Toast.makeText(this,eqnstr[0], Toast.LENGTH_SHORT).show()
            }
            else {
                intt = Intent(this, Solve::class.java)
                intt.putExtra("eqn", /*eqnstr.toString()*/eTXT.text.toString())
                intt.putExtra("eqlist", eqnstr.joinToString(","))
                ms.cset.remove('~')
                ms.cset.sort()
                intt.putExtra("chrset",ms.cset.joinToString(","))
                startActivity(intt)
            }
        }

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