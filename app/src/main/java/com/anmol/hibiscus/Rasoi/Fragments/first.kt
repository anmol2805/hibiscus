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
import com.anmol.hibiscus.Rasoi.Adapter.Mess2Adapter
import com.anmol.hibiscus.Rasoi.Model.mess2

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

        db.child("messStatus").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("amount2").getValue(String::class.java) != null) {
                    amt2.text = dataSnapshot.child("amount2").getValue(String::class.java)
                }
                if (dataSnapshot.child("total").getValue(String::class.java) != null) {
                    total.text = dataSnapshot.child("total").getValue(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        db.child("messStatus").child("mess2").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mess2s.clear()
                for (data in dataSnapshot.children) {
                    val day = data.child("date").value!!.toString()
                    val brkfast = data.child("brkfast").value!!.toString()
                    val lnch = data.child("lnch").value!!.toString()
                    val dinnr = data.child("dinnr").value!!.toString()
                    val bs = data.child("bs").value!!.toString()
                    val ls = data.child("ls").value!!.toString()
                    val ds = data.child("ds").value!!.toString()
                    val mess2 = mess2(day, brkfast, lnch, dinnr, bs, ls, ds)
                    mess2s.add(mess2)
                }
                if (activity != null) {
                    mess2Adapter = Mess2Adapter(activity, R.layout.menu, mess2s)
                    mess2Adapter.notifyDataSetChanged()
                    load.visibility = View.GONE
                    list.adapter = mess2Adapter

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return v
    }
}
