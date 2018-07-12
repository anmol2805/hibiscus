package com.anmol.hibiscus.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.anmol.hibiscus.Interfaces.ItemClickListener
import com.anmol.hibiscus.Model.Notice
import com.anmol.hibiscus.NoticeDataActivity
import com.anmol.hibiscus.R

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 2/27/2018.
 */
class IcoAdapter(internal var c: Context, internal var notices: MutableList<Notice>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<IcoAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.notice,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return notices.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val noticedata = notices[position]
        holder.mtitle?.text = noticedata.title
        holder.pstdby?.text = noticedata.posted_by
        holder.attent?.text = noticedata.attention
        holder.dates?.text = noticedata.date
        holder.viewBinderHelper!!.setOpenOnlyOne(true)
        holder.viewBinderHelper!!.bind(holder.swipereveallayout,noticedata.id)
        
        holder.noticelayout?.setOnClickListener {
            view ->
            val intent2 = Intent(c, NoticeDataActivity::class.java)
            intent2.putExtra("id",noticedata.id)
            intent2.putExtra("title",noticedata.title)
            intent2.putExtra("posted",noticedata.posted_by)
            intent2.putExtra("att",noticedata.attention)
            intent2.putExtra("date",noticedata.date)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val pair1:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.date),"mydate")
                val pair2:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.title),"mytext")
                val pair3:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.attention),"myatt")
                val pair4:Pair<View,String> = Pair.create(holder.itemView.findViewById(R.id.posted),"mypost")
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(c as Activity,pair1,pair2,pair3,pair4)
                c.startActivity(intent2,optionsCompat.toBundle())
            }else{
                c.startActivity(intent2)
            }
        }




    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var dates:TextView?=null
        var attent:TextView?=null
        var pstdby:TextView?=null
        var mtitle:TextView?=null
        var viewBinderHelper:ViewBinderHelper?=null
        var swipereveallayout:SwipeRevealLayout?=null

        //var sharebtn:Button?=null
        var noticelayout:RelativeLayout?=null
        init {
            this.dates = itemView.findViewById(R.id.date)
            this.attent = itemView.findViewById(R.id.attention)
            this.pstdby = itemView.findViewById(R.id.posted)
            this.mtitle = itemView.findViewById(R.id.title)
            this.noticelayout = itemView.findViewById(R.id.noticelayout)
            this.swipereveallayout = itemView.findViewById(R.id.swipereveallayout)
            viewBinderHelper = ViewBinderHelper()
            itemView.setOnClickListener(this)
        }

        override fun onClick(position: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }




    }




}