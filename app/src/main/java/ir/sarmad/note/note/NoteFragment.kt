package ir.sarmad.note.note

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener
import ir.sarmad.note.databinding.FragmentFolderBinding
import ir.sarmad.note.databinding.FragmentNoteBinding
import ir.sarmad.note.folder.Folder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class NoteFragment : Fragment(),DbListener {

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


    private lateinit var binding: FragmentNoteBinding
    private lateinit var dbManager: DBManager
    private lateinit var notesAdapter: RecyclerView.Adapter<*>
    private lateinit var notesGridAdapter: NotesGridAdabter
    private lateinit var currentAdapter: RecyclerView.Adapter<*>
    private var notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)



        dbManager = DBManager(requireContext(), this)
        notes = dbManager.getAllNotes()

        // مرتب‌سازی لیست یادداشت‌ها بر اساس تاریخ


        // ساخت adapter با استفاده از لیست مرتب‌شده
        notesAdapter = NoteAdabter(notes, dbManager)
        linearAdapterMode()
        setViewModeLinearImage()



        binding.noteaddButton.setOnClickListener {
            val intent = Intent(requireContext(), AddNote::class.java)
            startActivity(intent)
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
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sortOptions)

        binding.sortview.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Choose View").setAdapter(sortAdapter) { _, which ->
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
        return binding.root

    }









    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        notes = dbManager.getAllNotes()
        (currentAdapter as? NoteAdabter)?.refreshData(notes)
        (currentAdapter as? NotesGridAdabter)?.refreshData(notes)

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
        binding.btnSort.setImageResource(R.drawable.down)
    }


    private fun setBtnSortAscendingImage() {
        binding.btnSort.setImageResource(R.drawable.up)
    }


    private fun setViewModeLinearImage() {
        viewMode = VIEW_MODE_LINEAR
        binding.btnChoosView.setImageResource(R.drawable.noval)
    }


    private fun setViewModeGridImage() {
        viewMode = VIEW_MODE_GRID
        binding.btnChoosView.setImageResource(R.drawable.grid)
    }

    private fun checkAdapterAndRefreshData() {

        if (currentAdapter is NoteAdabter) {
            (currentAdapter as NoteAdabter).refreshData(notes)
        } else if (currentAdapter is NotesGridAdabter) {
            (currentAdapter as NotesGridAdabter).refreshData(notes)
        }

    }


    private fun gridAdapterMode() {
        notesGridAdapter = NotesGridAdabter(notes, dbManager)
        binding.notesRecyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.notesRecyclerview.adapter = notesGridAdapter
        currentAdapter = notesGridAdapter
    }


    private fun linearAdapterMode() {
        binding.notesRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.notesRecyclerview.adapter = notesAdapter
        currentAdapter = notesAdapter
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortNoteDescendingByDate() {
        notes = notes.sortedByDescending { note ->
            LocalDateTime.parse(
                note.notedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
        }.toMutableList()
        checkAdapterAndRefreshData()

    }

    @RequiresApi(Build.VERSION_CODES.O)
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