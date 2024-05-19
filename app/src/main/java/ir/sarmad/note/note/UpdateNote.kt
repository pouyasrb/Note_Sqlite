package ir.sarmad.note.note

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener
import ir.sarmad.note.databinding.ActivityAddNoteBinding
import ir.sarmad.note.databinding.ActivityUpdateNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateNote : AppCompatActivity(),DbListener {
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var dbManager: DBManager
    private var noteid: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbManager = DBManager(this, this)

        noteid = intent.getIntExtra("note_id", -1)
        if (noteid == -1) {
            finish()
            return
        }
        val note = dbManager.getNoteById(noteid)
        binding.updatetitletext.setText(note.notetitle)
        binding.updatecontentedittext.setText(note.notecontent)
        binding.updatesaveButton.setOnClickListener {
            val newtitle = binding.updatetitletext.text.toString()
            val newconetnt = binding.updatecontentedittext.text.toString()
            val alertDialog = AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes") { _, _ ->
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formattedDateTime = currentDateTime.format(formatter)
                    val updatenote = Note(noteid, newtitle, newconetnt, formattedDateTime)
                    dbManager.noteUpdate(updatenote)

                    Toast.makeText(this, "update", Toast.LENGTH_LONG).show()
                    finish()
                }


                .setNegativeButton("No") { _, _ ->

                }



            alertDialog.show()

        }





    }

    override fun onAdded() {

    }
}