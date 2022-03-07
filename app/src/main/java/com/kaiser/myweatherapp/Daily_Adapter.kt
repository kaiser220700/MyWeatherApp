package com.kaiser.myweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class Daily_Adapter(private var context : Context, private var dailyArray : ArrayList<DailyForecast>) : BaseAdapter(){
    class ViewHolder(row : View){
        var imgStatus: ImageView = row.findViewById(R.id.forecast_icon)
        var txtTime: TextView = row.findViewById(R.id.forecast_time)
        var txtMinTemp: TextView = row.findViewById(R.id.forecast_min_temp)
        var txtMaxTemp: TextView = row.findViewById(R.id.forecast_max_temp)

    }
    override fun getCount(): Int {
        return dailyArray.size
    }

    override fun getItem(p0: Int): Any {
        return dailyArray[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View?
        val viewholder: ViewHolder
        if (p1 == null){
            val layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view  = layoutinflater.inflate(R.layout.daily_forecast_view,null)
            viewholder = ViewHolder(view)
            view.tag = viewholder
        }else{
            view = p1
            viewholder = p1.tag as ViewHolder
        }
        val forecast : DailyForecast = getItem(p0) as DailyForecast
        Picasso.get()
            .load("https://openweathermap.org/img/wn/"+forecast.image+"@2x.png")
            .into(viewholder.imgStatus)
        viewholder.txtTime.text = forecast.time
        viewholder.txtMinTemp.text = forecast.min_temp
        viewholder.txtMaxTemp.text = forecast.max_temp
//        viewholder.imgStatus.setImageResource(location.image)
        return view as View
    }
}