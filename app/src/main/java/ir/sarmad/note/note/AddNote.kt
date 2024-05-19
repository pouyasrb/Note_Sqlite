package ir.sarmad.note.note

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener
import ir.sarmad.note.databinding.ActivityAddFolderBinding
import ir.sarmad.note.databinding.ActivityAddNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNote : AppCompatActivity(),DbListener {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var dbManager: DBManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbManager = DBManager(this, this)
        binding.notesaveButton.setOnClickListener {
            val title = binding.notetitletext.text.toString()
            val content = binding.contentedittext.text.toString()
            if (title.isNotEmpty()) {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val note = Note(0, title, content, formattedDateTime)
            dbManager.insertNoteIfNotExists(note)

            finish()
            }
            else{
                Toast.makeText(this,"title is empty",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onAdded() {


    }

}

