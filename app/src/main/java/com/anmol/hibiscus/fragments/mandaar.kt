package com.anmol.hibiscus.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.hibiscus.Model.Notice
import com.anmol.hibiscus.R
import com.github.ybq.android.spinkit.SpinKitView
import java.util.ArrayList

class mandaar: Fragment(){
    internal lateinit var progressBar: SpinKitView
    internal lateinit var title: String
    internal lateinit var date:String
    internal lateinit var postedby:String
    internal lateinit var id:String
    internal lateinit var attention:String
    internal var notices: MutableList<Notice> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.main, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}