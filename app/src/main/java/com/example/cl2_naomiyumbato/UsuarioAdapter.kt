package com.example.cl2_naomiyumbato

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cl2_naomiyumbato.databinding.ItemUsuarioBinding
import com.example.cl2_naomiyumbato.models.Posts

class UsuarioAdapter(private var usuarios: List<Posts>) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usuarios[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }
    fun setUsuarios(newUsuarios: List<Posts>) {
        usuarios = newUsuarios
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Posts) {
            binding.tvUserId.text = user.userId.toString()
            binding.tvId.text = user.id.toString()
            binding.tvTitle.text = user.title
            binding.tvBody.text = user.body
        }
    }
}
