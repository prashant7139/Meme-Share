package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

var currentImageUrl:String?=null
    // val memeImageView=findViewById(R.id.memeImageView)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        val progressBar=findViewById<ProgressBar>(R.id.progressBar)

        // Instantiate the RequestQueue.
        progressBar.visibility=View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageUrl = response.getString("url")
                val imageView = findViewById(R.id.memeImageView) as ImageView
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       progressBar.visibility=View.GONE
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }
                }).into(imageView)
            },
            Response.ErrorListener {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
            }
        )
      // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="plain/text"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme i got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"share this meme using...")
        startActivity(chooser)
    }
}