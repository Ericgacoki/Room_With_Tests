package com.ericg.groom.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ericg.groom.data.User
import com.ericg.groom.databinding.RowItemBinding

class ListAdapter(
    var list: List<User>,
    private val itemClicked: ItemClicked) :
    RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
       holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ItemViewHolder(private val item: RowItemBinding) : RecyclerView.ViewHolder(item.root),
        View.OnClickListener {
        init {
            item.root.setOnClickListener(this)
            item.btnDelete.setOnClickListener(this)
        }

        fun bind(user: User){
            item.apply {
                name.text = "${ user.firstName} ${user.lastName}"
                age.text = user.age.toString()
            }
        }

        override fun onClick(view: View?) {
            itemClicked.clicked(view!!, adapterPosition, view.id, list[adapterPosition].id)
        }
    }

    interface ItemClicked {
        fun clicked(view: View, position: Int, id:Int?, userID: Int)
    }
}
