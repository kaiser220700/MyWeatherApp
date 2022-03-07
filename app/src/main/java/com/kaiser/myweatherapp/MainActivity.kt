package com.kaiser.myweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    private var txtDegree: TextView? = null
    private var txtFeelsLike: TextView? = null
    private var imgIcon: ImageView? = null
    private var txtPOP: TextView? = null
    private var txtStatus: TextView? = null
    private var txtHumidity: TextView? = null
    private var txtUV: TextView? = null
    private var txtWindy: TextView? = null
    private var txtStatusHourlyForecast: TextView? = null
    private var txtDateHourlyForecast: TextView? = null
    private var hourlyForecast: RecyclerView? = null
    private var txtStatusDailyForecast: TextView? = null
    private var txtDateDailyForecast: TextView? = null
    private var dailyForecast: ListView? = null
    private val arrayHourlyForecast: ArrayList<HourlyForecast> = ArrayList()
    private val arrayDailyForecast: ArrayList<DailyForecast> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.hide()

        catchID()

        setActionBar()

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        getJsonData(lat,long)

    }

    private fun setActionBar() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.bg_splash_gradient))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.location)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_city -> {
                val intent = Intent(this,AddCity::class.java)
                startActivity(intent)
                return true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }


    private fun catchID() {
        txtDegree = findViewById(R.id.temp)
        txtFeelsLike = findViewById(R.id.feels_like)
        imgIcon = findViewById(R.id.icon)
        txtPOP = findViewById(R.id.pop)
        txtStatus = findViewById(R.id.status)
        txtHumidity = findViewById(R.id.humidity)
        txtUV = findViewById(R.id.uv)
        txtWindy = findViewById(R.id.windy)
        txtStatusHourlyForecast = findViewById(R.id.hourly_forecast_status)
        txtDateHourlyForecast = findViewById(R.id.hourly_forecast_date)
        hourlyForecast = findViewById(R.id.hourly_forecast_list)
        txtStatusDailyForecast = findViewById(R.id.daily_forecast_status)
        txtDateDailyForecast = findViewById(R.id.daily_forecast_date)
        dailyForecast = findViewById(R.id.daily_forecast_list)
    }

    private fun getJsonData(lat:String?, long:String?) {
        val apiKEY = "07477f5316bccf0a26594fcf75e042c1"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${long}&units=metric&appid=${apiKEY}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                setValues(response)
            },
            {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()})

        queue.add(jsonRequest)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setValues(response: JSONObject) {
        supportActionBar?.title = response.getString("timezone")
        //Set on current
        val current: JSONObject = response.getJSONObject("current")
        //set temp
        var temp = current.getString("temp")
        val convert: Long = temp.toDouble().roundToLong()
        txtDegree?.text = "$convert°C"
        //set feels like
        temp = current.getString("feels_like")
        txtFeelsLike?.text = "Feels like: $convert°C"
        //set image status
        var icon: String =
            current.getJSONArray("weather")
                .getJSONObject(0).getString("icon")
        Picasso.get()
            .load("https://openweathermap.org/img/wn/$icon@2x.png")
            .fit()
            .into(imgIcon)
        //set status description
        txtStatus?.text =
            current.getJSONArray("weather")
                .getJSONObject(0).getString("description")
        //set humidity
        txtHumidity?.text = current.getString("humidity") + "%"
        //set uv
        txtUV?.text = current.getString("uvi")
        // set wind speed
        txtWindy?.text = current.getString("wind_speed") + "m/s"

//        set forecast for hour
//        set status for hourly forecast
        txtStatusHourlyForecast?.text =
            response.getJSONObject("current").getJSONArray("weather")
                .getJSONObject(0).getString("description")
//        set time for hourly forecast
        val time: String = response.getJSONObject("current").getString("dt")
        val convertDay: Long = time.toLong()
        val date = Date(convertDay * 1000L)
        val dateFormat = SimpleDateFormat("MMMM, d yyyy")
        val getDay: String = dateFormat.format(date)
        txtDateHourlyForecast?.text = getDay
        val hour: JSONArray = response.getJSONArray("hourly")
        //set forecast chance of rain for current
        txtPOP?.text = "Chance of rain " + hour.getJSONObject(0).getString("pop") + "%"
        //execute  forecast for 24 hour in day
        for (i in 0..23) {
            val list: JSONObject = hour.getJSONObject(i)
            val date: String = list.getString("dt")
            val convertTime: Long = date.toLong()
            val convertDate = Date(convertTime * 1000L)
            val setFormat = SimpleDateFormat("h a")
            val getTime: String = setFormat.format(convertDate)
            temp = list.getString("temp")
            val tempFormat: Long = temp.toDouble().roundToLong()
            val getTemp = "$tempFormat°C"
            icon = list.getJSONArray("weather").getJSONObject(0).getString("icon")
            arrayHourlyForecast.add(HourlyForecast(getTime, icon, getTemp))
            hourlyForecast?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            hourlyForecast?.adapter = Hourly_Adapter(arrayHourlyForecast)
            }
        //set forecast for day of week
        txtStatusDailyForecast?.text =
            response.getJSONObject("current").getJSONArray("weather")
                .getJSONObject(0).getString("description")
        // set time daily forecast
        txtDateDailyForecast?.text = getDay
        val day: JSONArray = response.getJSONArray("daily")
        //execute forecast day in week
        for (i in 0..6) {
            val list: JSONObject = day.getJSONObject(i)
            icon = list.getJSONArray("weather").getJSONObject(0).getString("icon")
            val time: String = list.getString("dt")
            val convertTime: Long = time.toLong()
            val convertDate = Date(convertTime * 1000L)
            val setFormat = SimpleDateFormat("EEEE")
            val getTime: String = setFormat.format(convertDate)
            temp = list.getJSONObject("temp").getString("min")
            var tempFormat: Long = temp.toDouble().roundToLong()
            val getMinTemp = "$tempFormat°C"
            temp = list.getJSONObject("temp").getString("max")
            tempFormat = temp.toDouble().roundToLong()
            val getMaxTemp = "$tempFormat°C"
            arrayDailyForecast.add(DailyForecast(icon, getTime, getMinTemp, getMaxTemp))
            dailyForecast?.adapter = Daily_Adapter(this, arrayDailyForecast)
        }
    }
}