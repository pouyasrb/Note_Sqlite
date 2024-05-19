package ir.sarmad.note.folder

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
import ir.sarmad.note.databinding.ActivityAddFolderBinding
import ir.sarmad.note.databinding.ActivityUpdateFolderBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateFolder : AppCompatActivity(),DbListener {
    private lateinit var binding: ActivityUpdateFolderBinding
    private lateinit var dbManager: DBManager
    private var folderid:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager= DBManager(this,this)
        folderid = intent.getIntExtra("folder_id", -1)
        if (folderid == -1) {
            finish()

            return
        }




        val folder = dbManager.getFolderById(folderid)
        binding.formupdatetitletext.setText(folder.foldertitle)
        binding.formupdatesaveButton.setOnClickListener {
            val newtitle = binding.formupdatetitletext.text.toString()
            val alertDialog = AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Update?")
                .setPositiveButton("Yes") { _, _ ->
                    val updatefolder = Folder(folderid, newtitle)
                    dbManager.folderUpdate(updatefolder)
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
