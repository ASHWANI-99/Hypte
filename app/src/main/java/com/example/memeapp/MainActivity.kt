package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

     var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        // queue is not required as of now since we are making a Mysingleton class
       //val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")
                //for keeping image via url using glide
                // glide is actually the thing that downloads images and displays so progress bar is needed here
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        progressBar.visibility = View.GONE
                        return false
                    }
// just checking for github upload ignore this line
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImageView)
             },
            {

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        // queue is not required as of now since we are making a Mysingleton class
       // queue.add(JsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)
    }
    fun shareMeme(view: android.view.View) {
        val shhar = Intent(Intent.ACTION_SEND)
       shhar.type = "text/plain"
        shhar.putExtra(Intent.EXTRA_TEXT, "hello check this out $currentImageUrl")
        val chooser =Intent.createChooser(shhar, "share this url via - ")
        startActivity(chooser)
    }
    fun nextMeme(view: android.view.View) {
        loadMeme()
    }
}