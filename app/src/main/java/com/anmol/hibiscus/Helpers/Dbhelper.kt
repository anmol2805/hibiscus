package com.anmol.hibiscus.Helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteReadOnlyDatabaseException
import com.anmol.hibiscus.Model.Notice


val DATABASE_NAME = "noticedb"
val TABLE_NAME = "notice_table"
val COL_TITLE = "title"
val COL_ATTENTION = "attention"
val COL_POSTED_BY = "posted_by"
val COL_ID = "notice_id"
val COL_DATES = "dates"
val COL_BOOK = "bookmark"
val COL_READ = "read"


class Dbhelper (context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,1){

    override fun onCreate(p0: SQLiteDatabase?) {
        val createtable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                COL_TITLE + " TEXT," +
                COL_ATTENTION + " TEXT," +
                COL_POSTED_BY + " VARCHAR(256)," +
                COL_BOOK + " INTEGER," +
                COL_READ + " INTEGER," +
                COL_DATES + " TEXT)"

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun insertData(notice: Notice){
        try{
        val db = this.writableDatabase
            db.enableWriteAheadLogging()
        val cv = ContentValues()
        cv.put(COL_ID,notice.id)
        cv.put(COL_TITLE,notice.title)
        cv.put(COL_ATTENTION,notice.attention)
        cv.put(COL_POSTED_BY,notice.posted_by)
        cv.put(COL_DATES,notice.date)
        cv.put(COL_BOOK,0)
        cv.put(COL_READ,0)
            val result = db.insert(TABLE_NAME,null,cv)
            if(result == (-1).toLong())
                System.out.println("sqlstatus is failed")
            else
                System.out.println("sqlstatus is successs")
        }catch (e:SQLiteCantOpenDatabaseException){

        }

    }
    fun readData(dataquery: String):MutableList<Notice>{
        val notices : MutableList<Notice> = ArrayList()
        try {
            val db = this.readableDatabase
            val query = dataquery
            val result = db.rawQuery(query,null)
            if(result.moveToFirst()){
                do{
                    val title = result.getString(result.getColumnIndex(COL_TITLE))
                    val attention = result.getString(result.getColumnIndex(COL_ATTENTION))
                    val posted = result.getString(result.getColumnIndex(COL_POSTED_BY))
                    val id = result.getString(result.getColumnIndex(COL_ID))
                    val dates = result.getString(result.getColumnIndex(COL_DATES))
                    val book = result.getInt(result.getColumnIndex(COL_BOOK))
                    val read = result.getInt(result.getColumnIndex(COL_READ))
                    var booked = false
                    if(book == 1){
                        booked = true
                    }
                    var readed = false
                    if(read == 1){
                        readed = true
                    }

                    val notice = Notice(title,dates,posted,attention,id,booked,readed)
                    notices.add(notice)
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }

                }while (result.moveToNext())
            }
            result.close()
            db.close()
            System.out.println("inside:$notices")

        }
        catch (e:SQLiteCantOpenDatabaseException){
            e.printStackTrace()
        }
        catch (e:SQLiteReadOnlyDatabaseException){
            e.printStackTrace()
        }
        System.out.println("outside:$notices")
        return notices
    }
//    fun readmyData():MutableList<Tweet>{
//        val tweets : MutableList<Tweet> = ArrayList()
//        try {
//            val db = this.readableDatabase
      //     val query = "Select * from " + TABLE_NAME + " where " + COL_COIN_SYMBOL + "=1 ORDER BY " + COL_ID + " DESC"
//            val result = db.rawQuery(query,null)
//            if(result.moveToFirst()){
//                do{
//                    val mcoin = result.getString(result.getColumnIndex(COL_COIN))
//                    val coin_symbol = result.getString(result.getColumnIndex(COL_COIN_SYMBOL))
//                    val mtweet = result.getString(result.getColumnIndex(COL_TWEET))
//                    val url = result.getString(result.getColumnIndex(COL_URL))
//                    val keyword = result.getString(result.getColumnIndex(COL_KEYWORD))
//                    val id = result.getString(result.getColumnIndex(COL_ID))
//                    val dates = result.getString(result.getColumnIndex(COL_DATES))
//                    val coinpage = result.getString(result.getColumnIndex(COL_COIN_HANDLE))
//                    val booked = result.getString(result.getColumnIndex(COL_BOOKMARK))
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                }while (result.moveToNext())
//            }
//            result.close()
//            db.close()
//            System.out.println("inside:$tweets")
//
//        }
//        catch (e:SQLiteCantOpenDatabaseException){
//            e.printStackTrace()
//        }
//        System.out.println("outside:$tweets")
//        return tweets
//    }
//    fun readBookmarks():MutableList<Tweet>{
//        val tweets : MutableList<Tweet> = ArrayList()
//        try {
//            val db = this.readableDatabase
//            val query = "Select * from $TABLE_NAME where $COL_BOOKMARK=1 ORDER BY $COL_ID DESC"
//            val result = db.rawQuery(query,null)
//            if(result.moveToFirst()){
//                do{
//                    val mcoin = result.getString(result.getColumnIndex(COL_COIN))
//                    val coin_symbol = result.getString(result.getColumnIndex(COL_COIN_SYMBOL))
//                    val mtweet = result.getString(result.getColumnIndex(COL_TWEET))
//                    val url = result.getString(result.getColumnIndex(COL_URL))
//                    val keyword = result.getString(result.getColumnIndex(COL_KEYWORD))
//                    val id = result.getString(result.getColumnIndex(COL_ID))
//                    val dates = result.getString(result.getColumnIndex(COL_DATES))
//                    val coinpage = result.getString(result.getColumnIndex(COL_COIN_HANDLE))
//                    val booked = result.getString(result.getColumnIndex(COL_BOOKMARK))
//                    if(booked.contains("0")){
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,false,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                    else{
//                        val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,true,dates,"mc",coinpage)
//                        tweets.add(tweet)
//                    }
//                }while (result.moveToNext())
//            }
//            result.close()
//            db.close()
//            System.out.println("inside:$tweets")
//
//        }
//        catch (e:SQLiteCantOpenDatabaseException){
//            e.printStackTrace()
//        }
//        System.out.println("outside:$tweets")
//        return tweets
//    }
//
    fun updatenotice(notice: Notice){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_TITLE,notice.title)
        cv.put(COL_ATTENTION,notice.attention)
        cv.put(COL_POSTED_BY,notice.posted_by)
        cv.put(COL_DATES,notice.date)
        db.update(TABLE_NAME,cv,"$COL_ID = ?", arrayOf(notice.id))
        db.close()
    }
    fun updatebooknotice(booked:Boolean,id:String){
        val db = this.writableDatabase
        val cv = ContentValues()
        if(booked){
            cv.put(COL_BOOK,1)
        }
        else{
            cv.put(COL_BOOK,0)
        }

        db.update(TABLE_NAME,cv,"$COL_ID = ?", arrayOf(id))
        db.close()
    }
    fun updatereadnotice(readed:Boolean,id:String){
        val db = this.writableDatabase
        val cv = ContentValues()
        if(readed){
            cv.put(COL_READ,1)
        }
        else{
            cv.put(COL_READ,0)
        }

        db.update(TABLE_NAME,cv,"$COL_ID = ?", arrayOf(id))
        db.close()
    }

}

