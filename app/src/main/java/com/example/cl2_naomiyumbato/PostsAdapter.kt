package com.example.cl2_naomiyumbato

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cl2_naomiyumbato.databinding.ItemPostsBinding
import com.example.cl2_naomiyumbato.models.Posts

class PostsAdapter(private var posts: List<Posts>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = posts[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
    fun setPosts(newPosts: List<Posts>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Posts) {
            binding.tvUserId.text = user.userId.toString()
            binding.tvId.text = user.id.toString()
            binding.tvTitle.text = user.title
            binding.tvBody.text = user.body
        }
    }
}
