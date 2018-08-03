package com.anmol.hibiscus

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.anmol.hibiscus.Helpers.Dbstudentnoticebookshelper
import com.anmol.hibiscus.Model.Notice
import java.util.ArrayList

class StudentNoticeDataActivity : AppCompatActivity() {
    var booked = false
    var id:String?=null
    var bookmarksmenu:MenuItem?=null
    var dbstudentnoticebookshelper:Dbstudentnoticebookshelper?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        }
        setContentView(R.layout.activity_student_notice_data)
        title = "Notices"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        id = intent.getStringExtra("id")
        dbstudentnoticebookshelper = Dbstudentnoticebookshelper(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.noticedata1, menu)
        bookmarksmenu = menu.findItem(R.id.action_book)
        bookmarksmenu!!.setIcon(R.drawable.starwhite)

        var boomarks:List<String> = ArrayList()
        boomarks = dbstudentnoticebookshelper!!.readbook()
        var i = 0
        booked = false
        while (i < boomarks.size) {
            if (boomarks[i] == id) {
                bookmarksmenu!!.setIcon(R.drawable.stargolden)
                booked = true
            }
            i++
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // action with ID action_refresh was selected
            R.id.action_book -> if (!booked) {
                dbstudentnoticebookshelper!!.insertBook(id!!)
                bookmarksmenu!!.setIcon(R.drawable.stargolden)
            } else {
                dbstudentnoticebookshelper!!.deletebook(id!!)
                bookmarksmenu!!.setIcon(R.drawable.starwhite)
            }
            R.id.action_share -> {
                val shareintent = Intent()
                shareintent.action = Intent.ACTION_SEND
                shareintent.type = "text/plain"
                shareintent.putExtra(Intent.EXTRA_TEXT, title.toString() + " :\nhttps://canopydevelopers.com/sharednotice/" + id)
                startActivity(Intent.createChooser(shareintent, "Share notice"))
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
