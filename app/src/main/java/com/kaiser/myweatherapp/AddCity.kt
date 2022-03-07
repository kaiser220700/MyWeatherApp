package com.kaiser.myweatherapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.math.roundToLong

class AddCity : AppCompatActivity() {
    private var txtEnter: EditText? = null
    private var listLocation: ListView? = null
    private val arrayCity: ArrayList<City> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        assignID()
        setActionBar()

        txtEnter?.setOnKeyListener { _, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    getJsonData(txtEnter?.text.toString())

                    txtEnter?.text = null

                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun assignID() {
        txtEnter = findViewById(R.id.enter)
        listLocation = findViewById(R.id.city_list)
    }
    private fun setActionBar() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.bg_splash_gradient))
        supportActionBar?.title = "Add City"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getJsonData(data: String?) {
        val apiKey = "07477f5316bccf0a26594fcf75e042c1"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${data}&units=metric&appid=${apiKey}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                setValues(response)
            },
            {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()})
        queue.add(jsonRequest)
    }
    private fun setValues(response: JSONObject) {
        val icon: String =
            response.getJSONArray("weather").getJSONObject(0).getString("icon")
        val city: String = response.getString("name")
        val country: String = response.getJSONObject("sys").getString("country")
        val temp: String = response.getJSONObject("main").getString("temp")
        val convert: Long = temp.toDouble().roundToLong()
        val getTemp = "$convert°C"
        arrayCity.add(City(icon, city, country,getTemp))
        listLocation?.adapter = City_Adapter(this, arrayCity)
        listLocation?.setOnItemClickListener { _, _, _, _ ->
            val lat = response.getJSONObject("coord").getString("lat")
            val long = response.getJSONObject("coord").getString("lon")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("long", long)
            startActivity(intent)
        }
    }
}