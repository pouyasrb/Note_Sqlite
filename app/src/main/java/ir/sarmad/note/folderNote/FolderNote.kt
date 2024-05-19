package ir.sarmad.note.folderNote

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener
import ir.sarmad.note.databinding.ActivityFolderNoteBinding
import ir.sarmad.note.note.Note
import ir.sarmad.note.note.NoteAdabter
import ir.sarmad.note.note.NotesGridAdabter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FolderNote : AppCompatActivity(), DbListener {
    private val DATE = "Date"
    private val AZ = "A-Z"


    private var sortType: String = ""

    private val DATE_POSITION = 0
    private val AZ_POSITION = 1


    private var viewMode: String = ""
    private val VIEW_MODE_GRID = "Grid"
    private val VIEW_MODE_LINEAR = "Linear"


    private var sortMode: String = ""
    private val SORT_MODE_DESC = "Descending"
    private val SORT_MODE_ASCE = "Ascending"

    private lateinit var binding: ActivityFolderNoteBinding
    private lateinit var dbManager: DBManager
    private lateinit var foldernotesAdapter: RecyclerView.Adapter<*>
    private lateinit var foldernotesGridAdapter: FolderNotesGridAdabter
    private lateinit var currentAdapter: RecyclerView.Adapter<*>

    private var notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DBManager(applicationContext, this)

        val folderId = intent.getIntExtra("folder_id", -1)
        val folderTitle = intent.getStringExtra("folder_title")
        binding.foldernote.text=folderTitle
        if (folderId != -1) {
            notes = dbManager.getNotesInFolder(folderId)

            foldernotesAdapter = FolderNoteAdabter(notes, dbManager,this)
            linearAdapterMode()
            setViewModeLinearImage()
        } else {
            Toast.makeText(this, "شناسه فولدر معتبر نیست", Toast.LENGTH_SHORT).show()
        }





        binding.btnChoosView.setOnClickListener {

            when (viewMode) {

                VIEW_MODE_LINEAR -> {


                    setViewModeGridImage()
                    gridAdapterMode()
                }

                VIEW_MODE_GRID -> {

                    setViewModeLinearImage()
                    linearAdapterMode()
                }
            }

        }


        val sortOptions = arrayOf(DATE, AZ)
        val sortAdapter =
            ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, sortOptions)

        binding.sortview.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Choose View").setAdapter(sortAdapter) { _, which ->
                when (which) {
                    DATE_POSITION -> {

                        setSortTypeDate()
                        setBtnSortDescendingImage()
                        setSortModeDescending()
                        sortNoteDescendingByDate()
                        checkAdapterAndRefreshData()

                    }


                    AZ_POSITION -> {

                        setSortTypeAZ()
                        setBtnSortDescendingImage()
                        setSortModeDescending()
                        sortNoteDescendingByAZ()
                        sortNoteDescendingByAZ()
                        checkAdapterAndRefreshData()
                    }
                }
            }.show()
        }


        binding.btnSort.setOnClickListener {

            when {
                sortType == DATE && sortMode == SORT_MODE_DESC -> {
                    setBtnSortAscendingImage()
                    sortNoteAscendingByDate()
                    setSortModeAscending()
                }

                sortType == DATE && sortMode == SORT_MODE_ASCE -> {
                    setBtnSortDescendingImage()
                    sortNoteDescendingByDate()
                    setSortModeDescending()
                }

                sortType == AZ && sortMode == SORT_MODE_DESC -> {
                    setBtnSortAscendingImage()
                    sortNoteAscendingByAZ()
                    setSortModeAscending()

                }

                sortType == AZ && sortMode == SORT_MODE_ASCE -> {
                    setBtnSortDescendingImage()
                    sortNoteDescendingByAZ()
                    setSortModeDescending()

                }
            }
        }


    }

    override fun onResume() {
        super.onResume()


        (currentAdapter as? FolderNoteAdabter)?.refreshData(notes)
        (currentAdapter as? FolderNotesGridAdabter)?.refreshData(notes)

    }

    override fun onAdded() {
    }


    private fun setSortTypeAZ() {
        sortType = AZ
    }


    private fun setSortTypeDate() {
        sortType = DATE
    }

    private fun setSortModeDescending() {
        sortMode = SORT_MODE_DESC
    }


    private fun setSortModeAscending() {
        sortMode = SORT_MODE_ASCE
    }

    private fun setBtnSortDescendingImage() {
        binding.btnSort.setImageResource(ir.sarmad.note.R.drawable.down)
    }


    private fun setBtnSortAscendingImage() {
        binding.btnSort.setImageResource(ir.sarmad.note.R.drawable.up)
    }


    private fun setViewModeLinearImage() {
        viewMode = VIEW_MODE_LINEAR
        binding.btnChoosView.setImageResource(ir.sarmad.note.R.drawable.noval)
    }


    private fun setViewModeGridImage() {
        viewMode = VIEW_MODE_GRID
        binding.btnChoosView.setImageResource(ir.sarmad.note.R.drawable.grid)
    }

    private fun checkAdapterAndRefreshData() {

        if (currentAdapter is FolderNoteAdabter) {
            (currentAdapter as FolderNoteAdabter).refreshData(notes)
        } else if (currentAdapter is FolderNotesGridAdabter) {
            (currentAdapter as FolderNotesGridAdabter).refreshData(notes)
        }

    }


    private fun gridAdapterMode() {
        val folderId = intent.getIntExtra("folder_id", -1)
        notes = dbManager.getNotesInFolder(folderId)

        foldernotesGridAdapter = FolderNotesGridAdabter (notes, dbManager)
        binding.foldernotesRecyclerview.layoutManager = GridLayoutManager(this, 3)
        binding.foldernotesRecyclerview.adapter = foldernotesGridAdapter
        currentAdapter = foldernotesGridAdapter
    }


    private fun linearAdapterMode() {
        binding.foldernotesRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.foldernotesRecyclerview.adapter = foldernotesAdapter
        currentAdapter = foldernotesAdapter
    }


    private fun sortNoteDescendingByDate() {
        notes = notes.sortedByDescending { note ->
            LocalDateTime.parse(
                note.notedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
        }.toMutableList()
        checkAdapterAndRefreshData()

    }

    private fun sortNoteAscendingByDate() {
        notes = notes.sortedBy { note ->
            LocalDateTime.parse(
                note.notedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
        }.toMutableList()

        checkAdapterAndRefreshData()
    }

    private fun sortNoteDescendingByAZ() {
        notes = notes.sortedByDescending { note ->
            note.notetitle.lowercase(Locale.getDefault())
        }.toMutableList()
        checkAdapterAndRefreshData()


    }

    private fun sortNoteAscendingByAZ() {
        notes = notes.sortedBy { note ->
            note.notetitle.lowercase(Locale.getDefault())
        }.toMutableList()
        checkAdapterAndRefreshData()

    }
}