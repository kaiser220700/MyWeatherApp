package com.kaiser.myweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Hourly_Adapter(private val mList: List<HourlyForecast>) : RecyclerView.Adapter<Hourly_Adapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_forecast_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val forecast= mList[position]

        holder.txtTime.text = forecast.time

        Picasso.get()
            .load("https://openweathermap.org/img/wn/"+forecast.image+"@2x.png")
            .into(holder.imgStatus)

        holder.txtTemp.text = forecast.temp
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var txtTime: TextView = row.findViewById(R.id.forecast_time)
        var imgStatus: ImageView = row.findViewById(R.id.forecast_icon)
        var txtTemp: TextView = row.findViewById(R.id.forecast_temp)
    }
}