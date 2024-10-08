package com.example.cl2_naomiyumbato.intefaz;

import com.example.cl2_naomiyumbato.models.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @GET("posts")
    Call<List<Posts>> getPosts();
    // CREATE: Insertar un nuevo post (POST)
    @POST("posts")
    Call<Posts> createPost(@Body Posts post);

    // UPDATE: Actualizar un post completo (PUT)
    @PUT("posts/{id}")
    Call<Posts> updatePost(@Path("id") int id, @Body Posts post);

    // PATCH: Actualizar parcialmente un post (PATCH)
    @PATCH("posts/{id}")
    Call<Posts> patchPost(@Path("id") int id, @Body Posts post);

    // DELETE: Eliminar un post (DELETE)
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);

    // GET: Obtener un post por su ID
    @GET("posts/{id}")
    Call<Posts> getPostById(@Path("id") int id);

}

