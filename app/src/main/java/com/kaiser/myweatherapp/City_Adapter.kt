package com.kaiser.myweatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class City_Adapter(var context : Context, var locationArray : ArrayList<City>) : BaseAdapter(){
    class ViewHolder(row : View){
        var imgStatus: ImageView
        var txtCity: TextView
        var txtCountry: TextView
       var txtTemp: TextView
        init {
            imgStatus = row.findViewById(R.id.status)
            txtCity = row.findViewById(R.id.city)
            txtCountry = row.findViewById(R.id.country)
            txtTemp = row.findViewById(R.id.temp)
        }

    }
    override fun getCount(): Int {
        return locationArray.size
    }

    override fun getItem(p0: Int): Any {
        return locationArray.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View?
        var viewholder: ViewHolder
        if (p1 == null){
            var layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view  = layoutinflater.inflate(R.layout.city_view,null)
            viewholder = ViewHolder(view)
            view.tag = viewholder
        }else{
            view = p1
            viewholder = p1.tag as ViewHolder
        }
        var city : City = getItem(p0) as City
        Picasso.get()
            .load("https://openweathermap.org/img/wn/"+city.image+"@2x.png")
            .into(viewholder.imgStatus)
        viewholder.txtCity.text = city.city
        viewholder.txtCountry.text = city.country
        viewholder.txtTemp.text = city.temp

        return view as View
    }
}