package ir.sarmad.note.folder

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import ir.sarmad.note.database.DBManager
import ir.sarmad.note.database.DbListener

import ir.sarmad.note.databinding.FragmentFolderBinding


class FolderFragment : Fragment(), DbListener {


    private lateinit var folderAdabter: FolderAdabter
    private lateinit var dbManager: DBManager
    private var folder = mutableListOf<Folder>()
    private lateinit var binding: FragmentFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderBinding.inflate(inflater, container, false)


        
        dbManager = DBManager(requireContext(), this)
        folder = dbManager.getAllFolder()

        binding.folderAddButton.setOnClickListener {
            val intent = Intent(requireContext(), AddFolder::class.java)
            startActivity(intent)
        }

        folderAdabter = FolderAdabter(folder, dbManager)
        gridAdapterMode()


        return binding.root

    }


    private fun gridAdapterMode() {
        folderAdabter = FolderAdabter(folder, dbManager)
        binding.folderNotesRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.folderNotesRecyclerview.adapter = folderAdabter

    }

    override fun onAdded() {

    }

    override fun onResume() {
        super.onResume()

        folder = dbManager.getAllFolder()

        folderAdabter.refreshData(folder)
    }

}