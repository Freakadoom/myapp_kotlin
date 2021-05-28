package com.example.myapp.uitel

import android.app.Activity
import android.app.AlertDialog
import com.example.myapp.R

class LoadingItems(val sActivity: Activity) {
    private lateinit var isItems: AlertDialog
    fun startLoading() {
//        set View
        val inflater = sActivity.layoutInflater
        val itemsView = inflater.inflate(R.layout.progress_item, null)
//        set Items
        val builder = AlertDialog.Builder(sActivity)
        builder.setView(itemsView)
        builder.setCancelable(false)
        isItems = builder.create()
        isItems.show()
    }
    fun isDismiss() {
        isItems.dismiss()
    }
}