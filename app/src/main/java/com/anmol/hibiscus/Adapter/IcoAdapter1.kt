package com.anmol.hibiscus.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.util.Pair
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.hibiscus.Helpers.Dbbookshelper
import com.anmol.hibiscus.Helpers.Dbhelper
import com.anmol.hibiscus.Helpers.Dbstudentnoticebookshelper
import com.anmol.hibiscus.Helpers.Dbstudentnoticefirstopenhelper
import com.anmol.hibiscus.Interfaces.ItemClickListener
import com.anmol.hibiscus.Model.Notice
import com.anmol.hibiscus.NoticeDataActivity
import com.anmol.hibiscus.PostingActivity
import com.anmol.hibiscus.R
import com.anmol.hibiscus.StudentNoticeDataActivity

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by anmol on 2/27/2018.
 */
class IcoAdapter1(internal var c: Context, internal var notices: MutableList<Notice>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<IcoAdapter1.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.notice1,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return notices.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val noticedata = notices[position]
        val db = Dbstudentnoticebookshelper(c)
        val dbfo = Dbstudentnoticefirstopenhelper(c)
        val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            c.resources.getFont(R.font.lato_regular)
        } else{
            ResourcesCompat.getFont(c,R.font.lato_regular)
        }
        val typefacebold = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            c.resources.getFont(R.font.lato_black)
        } else{
            ResourcesCompat.getFont(c,R.font.lato_black)
        }
        val read = if(!noticedata.read){
            holder.mtitle?.typeface = typefacebold
            holder.dates?.typeface = typefacebold
            holder.pstdby?.typeface = typefacebold
            false
        }
        else{
            holder.mtitle?.typeface = typeface
            holder.dates?.typeface = typeface
            holder.pstdby?.typeface = typeface
            true
        }
        holder.mtitle?.text = noticedata.title
        holder.pstdby?.text = noticedata.posted_by
        holder.dates?.text = noticedata.date
        holder.viewBinderHelper!!.setOpenOnlyOne(true)
        holder.viewBinderHelper!!.bind(holder.swipereveallayout,noticedata.id)

        holder.deletenotice!!.visibility = View.GONE
        holder.editnotice!!.visibility = View.GONE
        holder.noticelayout?.setOnClickListener {
            if (!read){
                holder.mtitle?.typeface = typeface
                holder.dates?.typeface = typeface
                holder.pstdby?.typeface = typeface
                dbfo.insertBook(noticedata.id)
            }
            val intent2 = Intent(c, StudentNoticeDataActivity::class.java)
            intent2.putExtra("id",noticedata.id)
            intent2.putExtra("title",noticedata.title)
            intent2.putExtra("posted",noticedata.posted_by)
            intent2.putExtra("des",noticedata.attention)
            intent2.putExtra("date",noticedata.date)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val pair1:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.date),"mydate")
                val pair2:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.title),"mytext")
                val pair4:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.posted),"mypost")
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(c as Activity,pair1,pair2,pair4)
                c.startActivity(intent2,optionsCompat.toBundle())
            }else{
                c.startActivity(intent2)
            }
        }

        val book = if(!noticedata.bookmark){
            Glide.with(c).load(R.drawable.star1).into(holder.starnotice)
            false
        }
        else{
            Glide.with(c).load(R.drawable.stargolden).into(holder.starnotice)
            true
        }



        holder.starnotice?.setOnClickListener{
            if(!book){
                db.insertBook(noticedata.id)
                Glide.with(c).load(R.drawable.stargolden).into(holder.starnotice)
            }
            else{
                db.deletebook(noticedata.id)
                Glide.with(c).load(R.drawable.star1).into(holder.starnotice)
            }
        }
        holder.sharenotice?.setOnClickListener{
            val shareintent = Intent()
            shareintent.action = Intent.ACTION_SEND
            shareintent.type = "text/plain"
            shareintent.putExtra(Intent.EXTRA_TEXT,noticedata.title + " :\nhttps://canopydevelopers.com/sharedstudentnotice/" + noticedata.id)
            c.startActivity(Intent.createChooser(shareintent,"Share notice"))
        }
        val auth = FirebaseAuth.getInstance()
        val hibdatabase = FirebaseDatabase.getInstance().reference.child("Students").child(auth!!.currentUser!!.uid).child("hibiscus")
        hibdatabase.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val uid = p0.child("sid").value.toString()
                if(uid == noticedata.posted_by || uid == "b516008" || uid == "b216008"){
                    holder.deletenotice!!.visibility = View.VISIBLE
                    holder.editnotice!!.visibility = View.VISIBLE
                }
            }

        })
        holder.deletenotice!!.setOnClickListener{
//            val alertDialogBuilder = AlertDialog.Builder(c)
//
//            alertDialogBuilder.setTitle("Confirm")
//            alertDialogBuilder.setMessage("Are you sure you want to delete this notice?")
//            alertDialogBuilder.setPositiveButton("Delete") { p0, p1 ->
//                FirebaseDatabase.getInstance().reference.child("Studentnoticeboard").child(noticedata.id).removeValue()
//            }
//            alertDialogBuilder.setNegativeButton("Cancel") { p0, p1 ->
//
//            }
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()

            val dialog = AlertDialog.Builder(c)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to delete this notice?")
                    .setPositiveButton("Delete") { dialogInterface, i ->
                        val map = HashMap<String,Any>()
                        map["deleted"] = true
                        FirebaseDatabase.getInstance().reference.child("Studentnoticeboard").child(noticedata.id).updateChildren(map).addOnCompleteListener {
                            dialogInterface.dismiss()
                            Toast.makeText(c,"Deleted Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }.setNegativeButton("Cancel"){ dialogInterface, i ->
                        dialogInterface.dismiss()
                    }.create()

            dialog.show()
        }
        holder.editnotice!!.setOnClickListener{
            val intent = Intent(c,PostingActivity::class.java)
            intent.putExtra("title",noticedata.title)
            intent.putExtra("description",noticedata.attention)
            intent.putExtra("id",noticedata.id)
            c.startActivity(intent)
        }



    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var dates:TextView?=null
        var pstdby:TextView?=null
        var mtitle:TextView?=null
        var viewBinderHelper:ViewBinderHelper?=null
        var swipereveallayout:SwipeRevealLayout?=null
        var starnotice:ImageView?=null
        var sharenotice:Button?=null
        var editnotice:Button?=null
        var deletenotice:Button?=null
        var noticelayout:RelativeLayout?=null
        init {
            this.dates = itemView.findViewById(R.id.date)

            this.pstdby = itemView.findViewById(R.id.posted)
            this.mtitle = itemView.findViewById(R.id.title)
            this.noticelayout = itemView.findViewById(R.id.noticelayout)
            this.swipereveallayout = itemView.findViewById(R.id.swipereveallayout)
            this.sharenotice = itemView.findViewById(R.id.sharenotice)
            this.starnotice = itemView.findViewById(R.id.starnotice)
            this.editnotice = itemView.findViewById(R.id.editnotice)
            this.deletenotice = itemView.findViewById(R.id.deletenotice)
            viewBinderHelper = ViewBinderHelper()
            itemView.setOnClickListener(this)
        }

        override fun onClick(position: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }




    }




}