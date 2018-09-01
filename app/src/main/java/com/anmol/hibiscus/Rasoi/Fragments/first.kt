package com.anmol.hibiscus.Rasoi.Fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest

import com.anmol.hibiscus.Mysingleton
import com.anmol.hibiscus.R
import com.anmol.hibiscus.Rasoi.Adapter.Mess1Adapter
import com.anmol.hibiscus.Rasoi.Adapter.Mess2Adapter
import com.anmol.hibiscus.Rasoi.Model.mess1
import com.anmol.hibiscus.Rasoi.Model.mess2

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by anmol on 10/20/2017.
 */

class first : Fragment() {
    internal lateinit var load: Button
    internal lateinit var list: ListView
    internal var auth = FirebaseAuth.getInstance()
    internal var db = FirebaseDatabase.getInstance().reference.child("students").child(auth.currentUser!!.uid)
    internal var mess2s: MutableList<mess2> = ArrayList()
    internal lateinit var mess2Adapter: Mess2Adapter
    internal lateinit var amt2: TextView
    internal lateinit var total: TextView
    internal lateinit var bookm2: Button
    internal var databaseReference = FirebaseDatabase.getInstance().reference.child("Students").child(auth.currentUser!!.uid).child("hibiscus")
    internal var messdb = FirebaseDatabase.getInstance().reference.child("Rasoi").child("menu").child("mess2")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val v = inflater.inflate(R.layout.first, container, false)
        load = v.findViewById<View>(R.id.load) as Button
        list = v.findViewById<View>(R.id.menu) as ListView
        amt2 = v.findViewById<View>(R.id.amt2) as TextView
        total = v.findViewById<View>(R.id.total) as TextView
        val layoutInflater = activity.layoutInflater
        val footer = layoutInflater.inflate(R.layout.footer2, list, false) as ViewGroup
        list.addFooterView(footer, null, false)
        bookm2 = footer.findViewById<View>(R.id.bookm2) as Button

//        db.child("messStatus").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.child("amount2").getValue(String::class.java) != null) {
//                    amt2.text = dataSnapshot.child("amount2").getValue(String::class.java)
//                }
//                if (dataSnapshot.child("total").getValue(String::class.java) != null) {
//                    total.text = dataSnapshot.child("total").getValue(String::class.java)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                if(activity!=null){
                    Toast.makeText(activity,"Something went wrong!!!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDataChange(p0: DataSnapshot) {
                val sid = p0.child("sid").value as String
                messdb.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(menu: DataSnapshot) {
                        val statusdb = FirebaseDatabase.getInstance().reference.child("Rasoi").child(sid).child("nextweek")
                        statusdb.addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(statuserror: DatabaseError) {

                            }

                            override fun onDataChange(status: DataSnapshot) {
                                mess2s.clear()
                                var dates:String?=null
                                val format = SimpleDateFormat("EEEE-dd-MM-yyyy")
                                val calendar = Calendar.getInstance()
                                calendar.firstDayOfWeek = Calendar.MONDAY
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                                calendar.add(Calendar.DAY_OF_MONTH,7)
                                var i = 0
                                while (i<7){
                                    dates = format.format(calendar.time)
                                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                                    var bs = "notissued"
                                    var ls = "notissued"
                                    var ds = "notissued"
                                    var brkfast:String?=null
                                    var lnch:String?=null
                                    var dinnr:String?=null
                                    var bp:Long?=null
                                    var lp:Long?=null
                                    var dp:Long?=null
                                    if(menu.child(i.toString()).exists()){
                                        val data = menu.child(i.toString())
                                        brkfast = data.child("breakfast").value!!.toString()
                                        lnch = data.child("lunch").value!!.toString()
                                        dinnr = data.child("dinner").value!!.toString()
                                        bp = data.child("bp").value!! as Long
                                        lp = data.child("lp").value!! as Long
                                        dp = data.child("dp").value!! as Long
                                    }
                                    if(status.child(i.toString()).exists()){
                                        val data = status.child(i.toString())
                                        bs = data.child("bs").value!!.toString()
                                        ls = data.child("ls").value!!.toString()
                                        ds = data.child("ds").value!!.toString()
                                    }
                                    else{

                                    }
                                    val mess2 = mess2(dates, brkfast, lnch, dinnr, bs, ls, ds)
                                    mess2s.add(mess2)
                                    i++
                                }
                                if (activity != null) {
                                    mess2Adapter = Mess2Adapter(activity, R.layout.menu, mess2s)
                                    mess2Adapter.notifyDataSetChanged()
                                    load.visibility = View.GONE
                                    list.adapter = mess2Adapter

                                }

                            }

                        })


                    }

                })
            }

        })


        return v
    }
}
