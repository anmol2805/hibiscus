package com.anmol.hibiscus

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.anmol.hibiscus.Helpers.Dbhelper
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,R.color.colorAccent)
        }
        setContentView(R.layout.activity_student_notice_data)
        title = "Notices"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        handleIntent(intent)

        dbstudentnoticebookshelper = Dbstudentnoticebookshelper(this)
    }

    private fun handleIntent(intent: Intent) {
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkData = intent.data
        if (appLinkData != null) {
            id = appLinkData.lastPathSegment
            val studentdatabase = FirebaseDatabase.getInstance().reference.child("Studentnoticeboard")
            studentdatabase.child(id!!).addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot){
                    if(p0.child("deleted").value!=null){
                        if(!(p0.child("deleted").value as Boolean)){
                            putvalues(id,p0.child("postedby").value.toString(),p0.child("title").value.toString(),p0.child("description").value.toString(),p0.child("time").value.toString())
                        }
                        else{
                            Toast.makeText(this@StudentNoticeDataActivity,"Notice is deleted",Toast.LENGTH_SHORT).show()
                            putvalues("","","","","")
                        }
                    }

                }

            })


        } else {
            putvalues(intent.getStringExtra("id"),intent.getStringExtra("posted"),intent.getStringExtra("title"),intent.getStringExtra("des"),intent.getStringExtra("date"))
        }
    }

    private fun putvalues(mid: String?, mposted: String?, ntitle: String?, mdes: String?, mdate: String?) {
        id = mid
        noticedescription = mdes
        time = mdate
        posted_by = mposted
        mtitle = ntitle
        titleid.text = mtitle
        descript.text = noticedescription
        date.text = time
        posted.text = posted_by
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
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

        val boomarks: List<String>
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
        val hibdatabase = FirebaseDatabase.getInstance().reference.child("Students").child(auth.currentUser!!.uid).child("hibiscus")
        hibdatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val uid = p0.child("sid").value.toString()
                if(uid == posted_by || uid == "b516008" || uid == "b216008"){
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
                val dialog = AlertDialog.Builder(this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to delete this notice?")
                        .setPositiveButton("Delete") { dialogInterface, _ ->
                            val map = HashMap<String,Any>()
                            map["deleted"] = true
                            FirebaseDatabase.getInstance().reference.child("Studentnoticeboard").child(id!!).updateChildren(map).addOnCompleteListener {
                                dialogInterface.dismiss()
                                Toast.makeText(this,"Deleted Successfully",Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }.setNegativeButton("Cancel"){ dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }.create()

                dialog.show()
            }
            R.id.action_edit ->{
                val intent = Intent(this,PostingActivity::class.java)
                intent.putExtra("title",mtitle)
                intent.putExtra("description",noticedescription)
                intent.putExtra("id",id)
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
