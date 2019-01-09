package com.anmol.hibiscus

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_posting.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostingActivity : AppCompatActivity() {
    var auth:FirebaseAuth?=null
    var hibdatabase:DatabaseReference?=null
    var studentdatabase:DatabaseReference?=null
    var studentdatabaseid:DatabaseReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,R.color.colorAccent)
        }
        setContentView(R.layout.activity_posting)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        title = "Post Notice"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val noticedestext = intent.getStringExtra("description")
        val noticetitletext = intent.getStringExtra("title")
        val id = intent.getStringExtra("id")
        noticedes.setText(noticedestext)
        noticetitle.setText(noticetitletext)
        auth = FirebaseAuth.getInstance()
        studentdatabase = FirebaseDatabase.getInstance().reference.child("Studentnoticeboard")
        studentdatabaseid = FirebaseDatabase.getInstance().reference.child("Studentnoticeid")
        hibdatabase = FirebaseDatabase.getInstance().reference.child("Students").child(auth!!.currentUser!!.uid).child("hibiscus")
        submitnotice.setOnClickListener{
            submitnotice.visibility = View.GONE
            pgr.visibility = View.VISIBLE
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            val formattedDate = df.format(c)
            val noticetitlestring = noticetitle!!.text.toString()
            val noticedescription = noticedes!!.text.toString()
            if(!noticetitlestring.isEmpty() && !noticedescription.isEmpty()){
                hibdatabase!!.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        submitnotice.visibility = View.VISIBLE
                        pgr.visibility = View.GONE
                        Toast.makeText(this@PostingActivity,"Network Error",Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val uid = p0.child("sid").value.toString()
                        studentdatabase!!.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                submitnotice.visibility = View.VISIBLE
                                pgr.visibility = View.GONE
                                Toast.makeText(this@PostingActivity,"Network Error",Toast.LENGTH_SHORT).show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(id!=null){
                                    val map = HashMap<String,Any>()
                                    map["title"] = noticetitlestring
                                    map["description"] = noticedescription
                                    map["postedby"] = uid
                                    map["time"] = formattedDate
                                    map["deleted"] = false
                                    studentdatabase!!.child(id).updateChildren(map).addOnCompleteListener {
                                        finish()
                                    }
                                }
                                else{
                                    val size:Long = p0.childrenCount
                                    val map = HashMap<String,Any>()
                                    map["title"] = noticetitlestring
                                    map["description"] = noticedescription
                                    map["postedby"] = uid
                                    map["time"] = formattedDate
                                    map["deleted"] = false
                                    studentdatabase!!.child((size + 1).toString()).setValue(map).addOnCompleteListener {
                                        finish()
                                    }
                                }


                            }

                        })


                    }

                })
            }
            else{
                Toast.makeText(this,"fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
