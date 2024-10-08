package com.example.cl2_naomiyumbato

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cl2_naomiyumbato.databinding.ActivityMainBinding
import com.example.cl2_naomiyumbato.intefaz.JsonPlaceHolderApi
import com.example.cl2_naomiyumbato.models.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UsuarioAdapter
    private lateinit var mjsonTxtView: TextView
    private val ruta = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()
        searchUsuarios()
    }

    private fun setupRecyclerView() {
        userAdapter = UsuarioAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userAdapter
    }

    private fun setupButtons() {
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)

    }

    private fun searchUsuarios() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(JsonPlaceHolderApi::class.java)
        apiService.getPosts().enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                if (response.isSuccessful && response.body() != null) {
                    userAdapter.setUsuarios(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}