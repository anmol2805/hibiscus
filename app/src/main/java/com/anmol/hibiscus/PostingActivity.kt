package com.anmol.hibiscus

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_posting.*

class PostingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        }
        setContentView(R.layout.activity_posting)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        title = "Post Notice"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        submitnotice.setOnClickListener{
            val noticetitlestring = noticetitle!!.text.toString()
            val noticedescription = noticedes!!.text.toString()
        }

    }
}
