package com.nandaadisaputra.crud.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.crud.activity.DetailLoginActivity
import com.nandaadisaputra.crud.databinding.ItemsDataBinding
import com.nandaadisaputra.crud.room.LoginModel

class LoginAdapter(private val item: List<LoginModel>)
: RecyclerView.Adapter<LoginAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemsDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postResponse: LoginModel) {
            binding.tvName.text = postResponse.name
            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, DetailLoginActivity::class.java)
                    .putExtra("KEY_ID", postResponse.id)
                    .putExtra("KEY_USERNAME", postResponse.name)
                    .putExtra("KEY_PASSWORD", postResponse.password)
                    .putExtra("KEY_PIN", postResponse.pin)
                itemView.context.startActivity(moveToDetail)
            }
            Log.d("Test2", postResponse.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }
}