package com.example.myapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item.view.*

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: Item){
        with(itemView){
            item.run {
                textView.text = text

                Glide.with(context).load(imageUrl).into(imgView)
            }
        }
    }

}