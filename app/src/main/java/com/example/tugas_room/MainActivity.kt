package com.example.tugas_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_room.database.Note
import com.example.tugas_room.database.NoteRoomDatabase
import com.example.tugas_room.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listTodoAdapter: ListToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView
        setupRecyclerView()

        // Get data from the database
        val db = NoteRoomDatabase.getDatabase(this)
        val noteDao = db?.nodeDao()

        // Initialize LiveData to observe changes in the database
        val allNote: LiveData<List<Note>>? = noteDao?.allNotes

        // Observe changes in the database and update the adapter
        allNote?.observe(this) { notes ->
            notes?.let { listTodoAdapter.setData(it) }
        }

        // Set the delete click listener in the adapter
        listTodoAdapter.setOnDeleteClickListener(object : ListToDoAdapter.OnDeleteClickListener {
            override fun onDeleteClick(note: Note) {
                // Delete data in the background using Dao's delete function
                deleteNotesInBackground(note)
            }
        })

        // Set the edit click listener in the adapter
        listTodoAdapter.setOnEditClickListener(object : ListToDoAdapter.OnEditClickListener {
            override fun onEditClick(note: Note) {
                // Open FormActivity for editing, passing the data to be updated
                val intent = Intent(this@MainActivity, Form::class.java)
                startActivity(intent)
            }
        })

        // Set up the click listener for the "Tambah" button to open the Form activity
        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
        }



    }

    // Delete notes in the background using Dao's delete function
    private fun deleteNotesInBackground(note: Note) {
        val noteDao = NoteRoomDatabase.getDatabase(this)?.nodeDao()
        noteDao?.let {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // Access the database in the background
                    it.delete(note)
                }
            }
        }
    }

    // Set up the RecyclerView with an empty adapter
    private fun setupRecyclerView() {
        listTodoAdapter = ListToDoAdapter(emptyList())
        binding.rvTodo.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listTodoAdapter
        }
    }
}
