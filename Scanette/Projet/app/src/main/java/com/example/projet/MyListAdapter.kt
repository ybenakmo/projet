package com.example.projet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyListAdapter(var mCtx:Context , var resource:Int,var items:List<Model>)
    :ArrayAdapter<Model>( mCtx , resource , items ){




    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        val imageView :ImageView = view.findViewById(R.id.iconIv)
        var textView : TextView = view.findViewById(R.id.titleTv)
        var textView1 : TextView = view.findViewById(R.id.descTv)


        var person : Model = items[position]
        var x = person.photo
        if(x == 0)
            imageView.setImageDrawable(mCtx.resources.getDrawable(R.drawable.ufr))
        else if(x== 1)
            imageView.setImageDrawable(mCtx.resources.getDrawable(R.drawable.animaux))
        else if(x ==2)
            imageView.setImageDrawable(mCtx.resources.getDrawable(R.drawable.culture))
        else if(x==3)
            imageView.setImageDrawable(mCtx.resources.getDrawable(R.drawable.informatique))
        textView.text = person.title
        textView1.text = person.desc


        return view
    }

}