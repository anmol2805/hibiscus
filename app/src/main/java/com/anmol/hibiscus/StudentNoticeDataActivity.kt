package com.anmol.hibiscus

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.anmol.hibiscus.Helpers.Dbstudentnoticebookshelper
import com.anmol.hibiscus.Model.Notice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_student_notice_data.*
import java.util.ArrayList

class StudentNoticeDataActivity : AppCompatActivity() {
    var booked = false
    var id:String?=null
    var posted_by:String?=null
    var bookmarksmenu:MenuItem?=null
    var editmenu:MenuItem?=null
    var deletemenu:MenuItem?=null
    var dbstudentnoticebookshelper:Dbstudentnoticebookshelper?=null
    var mtitle:String?=null
    var noticedescription:String?=null
    var time:String?=null
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
        posted_by = intent.getStringExtra("posted")
        mtitle = intent.getStringExtra("title")
        noticedescription = intent.getStringExtra("des")
        time = intent.getStringExtra("date")
        titleid.text = mtitle
        descript.text = noticedescription
        date.text = time
        posted.text = posted_by
        dbstudentnoticebookshelper = Dbstudentnoticebookshelper(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.noticedata1, menu)
        bookmarksmenu = menu.findItem(R.id.action_book)
        editmenu = menu.findItem(R.id.action_edit)
        deletemenu = menu.findItem(R.id.action_delete)
        deletemenu!!.isVisible = false
        editmenu!!.isVisible = false
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
        val auth = FirebaseAuth.getInstance()
        val hibdatabase = FirebaseDatabase.getInstance().reference.child("Students").child(auth!!.currentUser!!.uid).child("hibiscus")
        hibdatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val uid = p0.child("sid").value.toString()
                if(uid == posted_by){
                    deletemenu!!.isVisible = true
                    editmenu!!.isVisible = true
                }
            }

        })
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
                shareintent.putExtra(Intent.EXTRA_TEXT, title.toString() + " :\nhttps://canopydevelopers.com/sharedstudentnotice/" + id)
                startActivity(Intent.createChooser(shareintent, "Share notice"))
            }
            R.id.action_delete ->{
                val alertDialogBuilder = AlertDialog.Builder(this)
                val alertDialog = alertDialogBuilder.create()
                alertDialogBuilder.setTitle("Confirm")
                alertDialogBuilder.setMessage("Are you sure you want to delete this notice?")
                alertDialogBuilder.setPositiveButton("Delete") { p0, p1 ->
                    FirebaseDatabase.getInstance().reference.child("Studentnoticeboard").child(id!!).removeValue().addOnCompleteListener {
                        Toast.makeText(this,"Deleted Successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                alertDialogBuilder.setNegativeButton("Cancel") { p0, p1 ->
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }
            R.id.action_edit ->{
                val intent = Intent(this,PostingActivity::class.java)
                intent.putExtra("title",mtitle)
                intent.putExtra("description",noticedescription)
                startActivity(intent)
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
