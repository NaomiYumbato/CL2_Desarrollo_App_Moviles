package com.example.cl2_naomiyumbato

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.cl2_naomiyumbato.intefaz.JsonPlaceHolderApi
import com.example.cl2_naomiyumbato.models.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cl2_naomiyumbato.databinding.UpdatePostBinding

class UpdatePost : AppCompatActivity() {
    private lateinit var binding: UpdatePostBinding
    private lateinit var apiService: JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root) // Usar View Binding para establecer la vista

        apiService = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceHolderApi::class.java)

        // Obtener datos del Intent
        val postId = intent.getIntExtra("id", -1)
        val userId = intent.getIntExtra("userId", -1)
        val title = intent.getStringExtra("title") ?: ""
        val body = intent.getStringExtra("body") ?: ""

        // Inicializar los EditTexts con los valores recibidos
        binding.etUserId.setText(userId.toString())
        binding.etId.setText(postId.toString()) // Aquí debería ser postId
        binding.etTitle.setText(title)
        binding.etBody.setText(body)

        // Configurar el botón de actualización
        binding.btnSubmit.setOnClickListener {
            updatePost(postId)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updatePost(postId: Int) {
        val userId = binding.etUserId.text.toString().toInt() // Obtener userId del EditText
        val title = binding.etTitle.text.toString()
        val body = binding.etBody.text.toString()

        val post = Posts().apply {
            setUserId(userId)
            setId(postId) // Aquí usamos postId
            setTitle(title)
            setBody(body)
        }

        apiService.updatePost(postId, post).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePost, "Post actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Regresar a la actividad anterior
                } else {
                    Toast.makeText(this@UpdatePost, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Toast.makeText(this@UpdatePost, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
