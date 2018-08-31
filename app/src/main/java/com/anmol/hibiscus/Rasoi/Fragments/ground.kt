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
import com.anmol.hibiscus.Rasoi.Model.mess1

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by anmol on 10/20/2017.
 */

class ground : Fragment() {
    internal lateinit var load: Button
    internal lateinit var list: ListView
    internal lateinit var mess1Adapter: Mess1Adapter
    internal var mess1s: MutableList<mess1> = ArrayList()
    internal var auth = FirebaseAuth.getInstance()
    internal lateinit var amt1: TextView
    internal lateinit var total: TextView
    internal lateinit var bookm1: Button
    internal var db = FirebaseDatabase.getInstance().reference.child("students").child(auth.currentUser!!.uid)
    internal var databaseReference = FirebaseDatabase.getInstance().reference.child("Students").child(auth.currentUser!!.uid).child("hibiscus")
    internal var messdb = FirebaseDatabase.getInstance().reference.child("Rasoi").child("menu").child("mess1")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val v = inflater.inflate(R.layout.ground, container, false)
        load = v.findViewById<View>(R.id.load) as Button
        list = v.findViewById<View>(R.id.menu) as ListView
        amt1 = v.findViewById<View>(R.id.amt1) as TextView
        total = v.findViewById<View>(R.id.total) as TextView
        val layoutInflater = activity.layoutInflater
        val footer = layoutInflater.inflate(R.layout.footer, list, false) as ViewGroup
        list.addFooterView(footer, null, false)
        bookm1 = footer.findViewById<View>(R.id.bookm1) as Button

        db.child("messStatus").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("amount1").getValue(String::class.java) != null) {
                    amt1.text = dataSnapshot.child("amount1").getValue(String::class.java)
                }
                if (dataSnapshot.child("total").getValue(String::class.java) != null) {
                    total.text = dataSnapshot.child("total").getValue(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
                                    var i = 0
                                    while (i<7){
                                        if(menu.child(i.toString()).exists()){
                                            val data = menu.child(i.toString())
                                            val brkfast = data.child("breakfast").value!!.toString()
                                            val lnch = data.child("lunch").value!!.toString()
                                            val dinnr = data.child("dinner").value!!.toString()
                                            val bp = data.child("bp").value!!
                                            val lp = data.child("lp").value!!
                                            val dp = data.child("dp").value!!
                                        }
                                        if(status.child(i.toString()).exists()){
                                            val data = status.child(i.toString())
                                            val bs = data.child("bs").value!!.toString()
                                            val ls = data.child("ls").value!!.toString()
                                            val ds = data.child("ds").value!!.toString()
                                        }
                                        i++
                                    }
                                }

                            })


                    }

                })
            }

        })
        db.child("messStatus").child("mess1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mess1s.clear()
                for (data in dataSnapshot.children) {
                    val day = data.child("date").value!!.toString()
                    val brkfast = data.child("brkfast").value!!.toString()
                    val lnch = data.child("lnch").value!!.toString()
                    val dinnr = data.child("dinnr").value!!.toString()
                    val bs = data.child("bs").value!!.toString()
                    val ls = data.child("ls").value!!.toString()
                    val ds = data.child("ds").value!!.toString()
                    val mess1 = mess1(day, brkfast, lnch, dinnr, bs, ls, ds)
                    mess1s.add(mess1)
                }
                if (activity != null) {
                    mess1Adapter = Mess1Adapter(activity, R.layout.menu, mess1s)
                    mess1Adapter.notifyDataSetChanged()
                    load.visibility = View.GONE
                    list.adapter = mess1Adapter

                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return v
    }
}
