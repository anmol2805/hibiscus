package com.anmol.hibiscus.Rasoi

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.internal.NavigationMenu

import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast


import com.anmol.hibiscus.R
import com.anmol.hibiscus.Rasoi.Adapter.GridAdapter
import com.anmol.hibiscus.Rasoi.Adapter.ViewpageAdapter
import com.anmol.hibiscus.Rasoi.Model.Coupon
import com.anmol.hibiscus.Rasoi.Model.MessStatus

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.text.DateFormatSymbols

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class rosei : Fragment() {
    internal var auth = FirebaseAuth.getInstance()
    internal var databaseReference = FirebaseDatabase.getInstance().reference.child("Students").child(auth.currentUser!!.uid).child("hibiscus")
    var book:Button?=null
    var logout:Button?=null
    internal var messStatuses: MutableList<MessStatus> = ArrayList()
    var gridview:RecyclerView?=null
    var gridAdapter:GridAdapter?=null
    var viewPager:ViewPager?=null
    internal var coupons: MutableList<Coupon> = ArrayList()
    var viewpageAdapter:ViewpageAdapter?=null
    var cd:CardView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.activity_rosei, container, false)
        activity.title = "IIIT Rasoi"
        book = vi.findViewById<View>(R.id.book) as Button
        logout = vi.findViewById<View>(R.id.logout) as Button
        viewPager = vi.findViewById<View>(R.id.viewpager) as ViewPager
        gridview = vi.findViewById<View>(R.id.gridrecycler) as RecyclerView

        cd = vi.findViewById(R.id.nocoupons)
        cd!!.visibility = View.GONE
        gridview!!.setHasFixedSize(true)
        gridview!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        book!!.setOnClickListener { startActivity(Intent(activity, Book_Activity::class.java)) }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        val cdate = dateFormat.format(cal.time)
        val tmeFormat = SimpleDateFormat("HH:mm")
        val ctime = tmeFormat.format(cal.time)
        val hr = ctime[0].toString() + ctime[1].toString()
        val hour = Integer.parseInt(hr)
        val min = ctime[3].toString() + ctime[4].toString()
        val mn = Integer.parseInt(min)
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        if (dayOfTheWeek.contains("Sunday")) {

        }
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                if(activity!=null){
                    Toast.makeText(activity,"Something went wrong!!!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDataChange(p0: DataSnapshot) {
                val sid = p0.child("sid").value as String
                val currentdb =  FirebaseDatabase.getInstance().reference.child("Rasoi").child(sid).child("currentweek")
                val nextdb = FirebaseDatabase.getInstance().reference.child("Rasoi").child(sid).child("nextweek")
                currentdb.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            messStatuses.clear()
                            coupons.clear()
                            var i = 0
                            while (i<7) {
                                if(dataSnapshot.child(i.toString()).exists()){
                                    val bs = dataSnapshot.child(i.toString()).child("bs").getValue(String::class.java)
                                    val ls = dataSnapshot.child(i.toString()).child("ls").getValue(String::class.java)
                                    val ds = dataSnapshot.child(i.toString()).child("ds").getValue(String::class.java)
                                    val date = dataSnapshot.child(i.toString()).child("date").getValue(String::class.java)
                                    val day = date!!.substring(11, 14)
                                    val messStatus = MessStatus(bs, ls, ds, date, day)
                                    messStatuses.add(messStatus)
                                    val daydate = date.substring(0, 10)
                                    val breakfastdate = "$daydate 09:45:00"
                                    val lunchdate = "$daydate 14:45:00"
                                    val dinnerdate = "$daydate 21:45:00"
                                    val compdate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    try {
                                        val bfdate = compdate.parse(breakfastdate)
                                        val lnchdate = compdate.parse(lunchdate)
                                        val dindate = compdate.parse(dinnerdate)
                                        val todaydate = Calendar.getInstance().time
                                        var bmess = "notissued"
                                        var lmess = "notissued"
                                        var dmess = "notissued"
                                        if (bs!!.contains("1")) {
                                            bmess = "Ground floor Mess"
                                        } else if (bs.contains("2")) {
                                            bmess = "First floor Mess"
                                        }
                                        if (ls!!.contains("1")) {
                                            lmess = "Ground floor Mess"
                                        } else if (ls.contains("2")) {
                                            lmess = "First floor Mess"
                                        }
                                        if (ds!!.contains("1")) {
                                            dmess = "Ground floor Mess"
                                        } else if (ds.contains("2")) {
                                            dmess = "First floor Mess"
                                        }
                                        val bfcoupon = Coupon("Breakfast", bmess, day, daydate)
                                        val lnccoupon = Coupon("Lunch", lmess, day, daydate)
                                        val dincoupon = Coupon("Dinner", dmess, day, daydate)
                                        if (todaydate.before(bfdate)) {
                                            if (!bmess.contains("notissued")) {
                                                coupons.add(bfcoupon)
                                            }
                                            if (!lmess.contains("notissued")) {
                                                coupons.add(lnccoupon)
                                            }
                                            if (!dmess.contains("notissued")) {
                                                coupons.add(dincoupon)
                                            }

                                        } else if (todaydate.after(bfdate) && todaydate.before(lnchdate)) {
                                            if (!lmess.contains("notissued")) {
                                                coupons.add(lnccoupon)
                                            }
                                            if (!dmess.contains("notissued")) {
                                                coupons.add(dincoupon)
                                            }


                                        } else if (todaydate.after(lnchdate) && todaydate.before(dindate)) {
                                            if (!dmess.contains("notissued")) {
                                                coupons.add(dincoupon)
                                            }

                                        }

                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                    }
                                }

                                i++

                            }
                            if (!messStatuses.isEmpty()) {
                                gridAdapter = GridAdapter(activity, messStatuses)
                                gridview!!.adapter = gridAdapter
                            }
                            if (!coupons.isEmpty()) {
                                viewpageAdapter = ViewpageAdapter(activity, coupons)
                                viewpageAdapter!!.notifyDataSetChanged()
                                viewPager!!.adapter = viewpageAdapter
                            }
                        }
                        else{
                            viewPager!!.visibility=View.GONE
                            cd!!.visibility= View.VISIBLE
                            messStatuses.clear()
                            val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            var i = 0
                            val nil = "NotIssued"
                            var day: String?
                            var date: String?
                            val format = SimpleDateFormat("dd-MM-yyyy")
                            val calendar = Calendar.getInstance()
                            calendar.firstDayOfWeek = Calendar.MONDAY
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                            while (i<7){
                                date = format.format(calendar.time)
                                calendar.add(Calendar.DAY_OF_MONTH, 1)
                                day = days[i]

                                val messStatus = MessStatus(nil, nil, nil, date, day)
                                messStatuses.add(messStatus)
                                i++
                            }
                            if(activity!=null){
                                if (!messStatuses.isEmpty()) {
                                    gridAdapter = GridAdapter(activity, messStatuses)
                                    gridview!!.adapter = gridAdapter
                                }
                                Toast.makeText(activity,"You haven't booked any coupons for this week.",Toast.LENGTH_SHORT).show()
                            }

                        }

                    }


                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

                nextdb.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.child("0").exists() && dataSnapshot.child("0").child("date").exists()) {
                            var date = dataSnapshot.child("0").child("date").getValue(String::class.java)
                            date = date!!.substring(0, 10)
                            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                            try {
                                val changedate = simpleDateFormat.parse(date)
                                var onedaybefore = Date(changedate.time - 2)
                                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                var stringodf = sdf1.format(onedaybefore)
                                stringodf = stringodf.substring(0, 10)
                                stringodf = "$stringodf 21:45:00"
                                onedaybefore = sdf1.parse(stringodf)
                                val currenttime = Calendar.getInstance().time
                                if (currenttime.after(onedaybefore)) {
                                    for (i in 0..6) {
                                        movePres(nextdb.child(i.toString()), currentdb.child(i.toString()))
                                        nextdb.child(i.toString()).removeValue()
                                    }
                                }
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                        } else {

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

            }

        })


        return vi
    }
    private fun movePres(fromPath: DatabaseReference, toPath: DatabaseReference) {
        fromPath.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                toPath.setValue(dataSnapshot.value) { firebaseError, firebase ->
                    if (firebaseError != null) {
                        println("Copy failed")
                    } else {
                        println("Success")

                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


}
