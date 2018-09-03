package com.anmol.hibiscus.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.hibiscus.Helpers.Dbbookshelper
import com.anmol.hibiscus.Helpers.Dbhelper
import com.anmol.hibiscus.Interfaces.ItemClickListener
import com.anmol.hibiscus.Interfaces.NLoadMore
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
import kotlinx.android.synthetic.main.noticeloading.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 2/27/2018.
 */
internal class LoadingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    var progressbar = itemView.noticeload
}
internal class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var dates:TextView?=null
    var attent:TextView?=null
    var pstdby:TextView?=null
    var mtitle:TextView?=null
    var viewBinderHelper:ViewBinderHelper?=null
    var swipereveallayout:SwipeRevealLayout?=null
    var starnotice:ImageView?=null
    var sharenotice:Button?=null
    var noticelayout:RelativeLayout?=null
    init {
        this.dates = itemView.findViewById(R.id.date)
        this.attent = itemView.findViewById(R.id.attention)
        this.pstdby = itemView.findViewById(R.id.posted)
        this.mtitle = itemView.findViewById(R.id.title)
        this.noticelayout = itemView.findViewById(R.id.noticelayout)
        this.swipereveallayout = itemView.findViewById(R.id.swipereveallayout)
        this.sharenotice = itemView.findViewById(R.id.sharenotice)
        this.starnotice = itemView.findViewById(R.id.starnotice)
        viewBinderHelper = ViewBinderHelper()
        itemView.setOnClickListener(this)
    }

    override fun onClick(position: View?) {
        mitemClickListener.onItemClick(this.adapterPosition)
    }




}
class IcoAdapter(recyclerview:RecyclerView,var c: Context?=null, internal var notices: MutableList<Notice>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val VIEW_ITEMTYPE = 0
    val VIEW_LOADINGTYPE = 1
    internal var loadmore:NLoadMore?=null
    internal var isLoading:Boolean = false
    internal var visibleThreshold = 5
    internal var lastVisibleItem:Int = 0
    internal var totalItemCount:Int = 0
    init {
        val linearLayoutManager = recyclerview.layoutManager as LinearLayoutManager
        recyclerview.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount<=lastVisibleItem+visibleThreshold)
                    if(loadmore!=null)
                        loadmore!!.onLoadMore()
                isLoading=true
            }


        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_ITEMTYPE){
            val v = LayoutInflater.from(c).inflate(R.layout.notice,parent,false)
            MyViewHolder(v,mitemClickListener)
        }
        else{
            val v = LayoutInflater.from(c).inflate(R.layout.noticeloading,parent,false)
            LoadingViewHolder(v)
        }

    }

    override fun getItemCount(): Int {
        return notices.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
            val noticedata = notices[position]
            val db = Dbhelper(c!!)
            val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                c!!.resources.getFont(R.font.lato_regular)
            } else{
                ResourcesCompat.getFont(c!!,R.font.lato_regular)
            }
            val typefacebold = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                c!!.resources.getFont(R.font.lato_black)
            } else{
                ResourcesCompat.getFont(c!!,R.font.lato_black)
            }
            val read = if(!noticedata.read){
                holder.mtitle?.typeface = typefacebold
                holder.dates?.typeface = typefacebold
                holder.pstdby?.typeface = typefacebold
                holder.attent?.typeface = typefacebold

                false
            }
            else{
                holder.mtitle?.typeface = typeface
                holder.dates?.typeface = typeface
                holder.pstdby?.typeface = typeface
                holder.attent?.typeface = typeface
                true
            }
            holder.mtitle?.text = noticedata.title
            holder.pstdby?.text = noticedata.posted_by
            holder.attent?.text = noticedata.attention
            holder.dates?.text = noticedata.date
            holder.viewBinderHelper!!.setOpenOnlyOne(true)
            holder.viewBinderHelper!!.bind(holder.swipereveallayout,noticedata.id)


            holder.noticelayout?.setOnClickListener {

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
                    c!!.startActivity(intent2,optionsCompat.toBundle())
                }else{
                    c!!.startActivity(intent2)
                }
                if (!read){
                    holder.mtitle?.typeface = typeface
                    holder.dates?.typeface = typeface
                    holder.pstdby?.typeface = typeface
                    holder.attent?.typeface = typeface
                    db.updatereadnotice(true,noticedata.id)
                }
            }

            val book = if(!noticedata.bookmark){
                Glide.with(c!!).load(R.drawable.star1).into(holder.starnotice)
                false
            }
            else{
                Glide.with(c!!).load(R.drawable.stargolden).into(holder.starnotice)
                true
            }



            holder.starnotice?.setOnClickListener{
                if(!book){
                    db.updatebooknotice(true,noticedata.id)
                    Glide.with(c!!).load(R.drawable.stargolden).into(holder.starnotice)
                }
                else{
                    db.updatebooknotice(false,noticedata.id)
                    Glide.with(c!!).load(R.drawable.star1).into(holder.starnotice)
                }
            }
            holder.sharenotice?.setOnClickListener{
                val shareintent = Intent()
                shareintent.action = Intent.ACTION_SEND
                shareintent.type = "text/plain"
                shareintent.putExtra(Intent.EXTRA_TEXT,noticedata.title + " :\nhttps://canopydevelopers.com/sharednotice/" + noticedata.id)
                c!!.startActivity(Intent.createChooser(shareintent,"Share notice"))
            }
        }
        else if(holder is LoadingViewHolder){
            holder.progressbar.isIndeterminate = true
        }
    }
    fun setLoaded(){
        isLoading = false
    }





}