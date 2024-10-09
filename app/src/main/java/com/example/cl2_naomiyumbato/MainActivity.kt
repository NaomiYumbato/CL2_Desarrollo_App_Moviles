package com.example.cl2_naomiyumbato

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var postAdapter: PostsAdapter
    private val ruta = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupRecyclerView()
        //Listado de los Posts
        searchPosts()

        setupButtons()

    }

    private fun setupRecyclerView() {
        postAdapter = PostsAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postAdapter
    }

    private fun setupButtons() {
        // Encontrar los botones por su ID
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)


        btnBuscar.setOnClickListener {
            showSearchDialog()
        }

        btnInsertar.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            showDeleteInputDialog()
        }

        btnActualizar.setOnClickListener {
            showUpdateDialog()
        }


    }

    private fun searchPosts() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(JsonPlaceHolderApi::class.java)
        apiService.getPosts().enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                if (response.isSuccessful && response.body() != null) {
                    postAdapter.setPosts(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun getPostById(id: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi: JsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
        val call: Call<Posts> = jsonPlaceHolderApi.getPostById(id)

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_LONG).show()
                    return
                }
                else {
                    val post = response.body()
                    post?.let {
                        Toast.makeText(
                            this@MainActivity,
                            "Item encontrado: ${it.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                        postAdapter.setPosts(listOf(response.body()!!))
                    } ?: run {
                        Toast.makeText(this@MainActivity, "Item no encontrado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getPostByIdUpdate(postId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(JsonPlaceHolderApi::class.java)
        apiService.getPostById(postId).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    post?.let {
                        startUpdatePostActivity(it.getId(), it.getUserId(), it.getTitle(), it.getBody())
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startUpdatePostActivity(postId: Int, userId: Int, title: String, body: String) {
        val intent = Intent(this, UpdatePost::class.java).apply {
            putExtra("id", postId)
            putExtra("userId", userId)
            putExtra("title", title)
            putExtra("body", body)
        }
        startActivity(intent)
    }


    private fun deletePost(postId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        jsonPlaceHolderApi.deletePost(postId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.e("MainActivity", "Código: ${response.code()}")
                    return
                }

                Toast.makeText(this@MainActivity, "Post eliminado con éxito, ID: $postId", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun showDeleteInputDialog() {
        val input = EditText(this).apply { hint = "Ingrese el ID del post a eliminar" }

        AlertDialog.Builder(this)
            .setTitle("Eliminar Post")
            .setMessage("Ingrese el ID del post que desea eliminar:")
            .setView(input)
            .setPositiveButton("Eliminar") { _, _ ->
                val postId = input.text.toString().toIntOrNull()
                if (postId != null) {
                    deletePost(postId)
                } else {
                    Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }


    private fun showSearchDialog() {
        val input = EditText(this).apply { hint = "Ingrese el ID a buscar" }

        AlertDialog.Builder(this)
            .setTitle("Buscar Post por ID")
            .setMessage("Ingrese el ID del Post que desea buscar:")
            .setView(input)
            .setPositiveButton("Buscar") { _, _ ->
                val id = input.text.toString().toIntOrNull()
                if (id != null) {
                    getPostById(id)
                } else {
                    Toast.makeText(this, "Por favor, ingrese un número válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun showUpdateDialog() {
        val input = EditText(this).apply { hint = "Ingrese el ID del Post a actualizar" }

        AlertDialog.Builder(this)
            .setTitle("Buscar Post a Actualizar")
            .setMessage("Ingrese el ID del Post que desea actualizar:")
            .setView(input)
            .setPositiveButton("Buscar") { _, _ ->
                val id = input.text.toString().toIntOrNull()
                if (id != null) {
                    getPostByIdUpdate(id)
                } else {
                    Toast.makeText(this, "Por favor, ingrese un número válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }


}