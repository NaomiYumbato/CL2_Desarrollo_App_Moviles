package com.example.cl2_naomiyumbato

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cl2_naomiyumbato.intefaz.JsonPlaceHolderApi
import com.example.cl2_naomiyumbato.models.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreatePost : AppCompatActivity() {

    private lateinit var apiService: JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(JsonPlaceHolderApi::class.java)

        val btnGrabar = findViewById<Button>(R.id.btnSubmit)
        val btnVolver = findViewById<Button>(R.id.btnBack)

        btnGrabar.setOnClickListener {
            createPost()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun createPost() {
        val userId = findViewById<EditText>(R.id.etUserId).text.toString()
        val id = findViewById<EditText>(R.id.etId).text.toString()
        val title = findViewById<EditText>(R.id.etTitle).text.toString()
        val body = findViewById<EditText>(R.id.etBody).text.toString()

        if (userId.isEmpty() || id.isEmpty() ||  title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val post = Posts().apply {
            setUserId(userId.toInt())
            setUserId(id.toInt())
            setTitle(title)
            setBody(body)
        }
        sendData(post)
    }


    private fun sendData(post: Posts) {
        apiService.createPost(post).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreatePost, "Post creado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.e("CreatePost", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@CreatePost, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Log.e("CreatePost", "Error: ${t.message}")
                Toast.makeText(this@CreatePost, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}