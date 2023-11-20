package com.example.tugas_room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_room.database.Note
import com.example.tugas_room.databinding.TodolistBinding

class ListToDoAdapter(private var ListToDo: List<Note>) :
    RecyclerView.Adapter<ListToDoAdapter.ItemToDoViewHolder>() {

    inner class ItemToDoViewHolder(private val binding: TodolistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set up click listeners for the edit and delete buttons
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Trigger the onEditClick listener with the clicked item's data
                    onEditClickListener?.onEditClick(ListToDo[position])
                }
            }

            binding.btnHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Trigger the onDeleteClick listener with the clicked item's data
                    onDeleteClickListener?.onDeleteClick(ListToDo[position])
                }
            }
        }

        // Bind data to the view
        fun bind(todo: Note) {
            with(binding) {
                namaTodo.text = todo.title
                tanggalTodo.text = todo.date
                DeskripsiToDo.text = todo.description
            }
        }
    }

    // Update the data and notify the adapter
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newToDo: List<Note>) {
        ListToDo = newToDo
        notifyDataSetChanged()
    }

    // Interface for the delete click listener
    interface OnDeleteClickListener {
        fun onDeleteClick(todo: Note)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    // Set the delete click listener
    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    // Interface for the edit click listener
    interface OnEditClickListener {
        fun onEditClick(todo: Note)
    }

    private var onEditClickListener: OnEditClickListener? = null

    // Set the edit click listener
    fun setOnEditClickListener(listener: OnEditClickListener) {
        onEditClickListener = listener
    }

    // Create and return a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemToDoViewHolder {
        val binding = TodolistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemToDoViewHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ItemToDoViewHolder, position: Int) {
        holder.bind(ListToDo[position])
    }

    // Return the number of items in the data set
    override fun getItemCount(): Int = ListToDo.size
}
