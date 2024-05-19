package ir.sarmad.note.folder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener
import ir.sarmad.note.databinding.ActivityAddFolderBinding


class AddFolder : AppCompatActivity(),DbListener {

    private lateinit var binding: ActivityAddFolderBinding
    private lateinit var dbManager: DBManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager= DBManager(this,this)

        binding.foldersaveButton.setOnClickListener {
            val title = binding.foldertitletext.text.toString()
            val folder=Folder(0,title)
            dbManager.insertFolder(folder)
            finish()

        }

        }

    override fun onAdded() {

    }
}
