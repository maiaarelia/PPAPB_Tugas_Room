package com.example.tugas_room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_room.database.Note
import com.example.tugas_room.database.NoteDao
import com.example.tugas_room.database.NoteRoomDatabase
import com.example.tugas_room.databinding.FormActivityBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Form : AppCompatActivity() {
    private lateinit var binding: FormActivityBinding
    private lateinit var noteDao : NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()

        val db = NoteRoomDatabase.getDatabase(this)

        if (db != null) {
            noteDao = db.nodeDao()!!
        }

        // Get data passed from MainActivity for editing if available
        val noteToEdit = intent.getSerializableExtra("EXTRA_TODO") as Note?

        if (noteToEdit != null) {
            // Use data to fill the form if available
            binding.txtTitleTodo.setText(noteToEdit.title)
            binding.txtDescTodo.setText(noteToEdit.description)
            binding.txtTglTodo.setText(noteToEdit.date)
        }

        binding.buttonSave.setOnClickListener {
            // Get values from form fields
            val title = binding.txtTitleTodo.text.toString()
            val description = binding.txtDescTodo.text.toString()
            val date = binding.txtTglTodo.text.toString()

            // Check if the form is filled correctly
            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                // Create a Note object with form values
                val note = Note(
                    title = title,
                    description = description,
                    date = date
                )

                // Insert the note into the database
                insert(note)
            } else {
                // Show a toast message if the form is not filled correctly
                Toast.makeText(applicationContext, "Tolong Isi Semua Form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Insert a new note into the database
    private fun insert(todo: Note) {
        executorService.execute {
            noteDao.insert(todo)
            // Return to the Main Activity
            setResult(RESULT_OK)
            finish() // Close the form
        }
    }
}
