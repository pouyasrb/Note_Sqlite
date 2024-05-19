package ir.sarmad.note.folder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.folderNote.FolderNote
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.note.Note

class FolderAdabter(private var folder: List<Folder>, private val db: DBManager) :
    RecyclerView.Adapter<FolderAdabter.NotesviewHolder>() {

    class NotesviewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var tvtitle: TextView = itemview.findViewById(R.id.foldertitletextview)
        var updatebutton: ImageView = itemview.findViewById(R.id.folderbtnupdate)
        var deletbutton: ImageView = itemview.findViewById(R.id.foldrtbtndelet)
        var cardviewnote: CardView =itemview.findViewById(R.id.cardviewnote)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesviewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.foldergrid, parent, false)
        return NotesviewHolder(view)
    }

    override fun getItemCount(): Int = folder.size


    override fun onBindViewHolder(holder: NotesviewHolder, position: Int) {
        val folder = folder[position]
        holder.tvtitle.text = folder.foldertitle




        holder.updatebutton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateFolder::class.java).apply {
                putExtra("folder_id", folder.folderid)

            }
            holder.itemView.context.startActivity(intent)
        }

        holder.cardviewnote.setOnLongClickListener {
            val folderId = folder.folderid
            val noteList = db.getAllNotes()
            if (noteList.isNotEmpty()) {
                showNoteSelectionDialog(holder.itemView.context, noteList, folderId)
            } else {
                Toast.makeText(holder.itemView.context, "There are no notes to add", Toast.LENGTH_SHORT).show()
            }
            true
        }


        holder.itemView.setOnClickListener {
            val folderId = folder.folderid
            val folderTitle=folder.foldertitle
            val noteList = db.getNotesInFolder(folderId)
            if (noteList.isNotEmpty()) {
                val intent = Intent(holder.itemView.context, FolderNote::class.java).apply {
                    putExtra("folder_id", folderId)
                    putExtra("folder_title", folderTitle)
                }
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "There are no notes in this folder", Toast.LENGTH_SHORT).show()
            }
        }






        holder.deletbutton.setOnClickListener {
            val alertDialog= AlertDialog.Builder(holder.itemView.context)
                .setTitle("Are you sure you want to delete?")
                .setMessage("There is no going back after the change")
                .setPositiveButton("Yes"){_,_->
                    db.relationDeleteByFolderId(folder.folderid)
                    db.formDelete(folder.folderid)
                    refreshData(db.getAllFolder())

                }

                .setNegativeButton("No"){_,_->


                }
            alertDialog.show()



        }




    }




    private fun showNoteSelectionDialog(context: Context, noteList: List<Note>, folderId: Int) {
        val noteTitles = noteList.map { it.notetitle }.toTypedArray()

        val dialog = AlertDialog.Builder(context)
            .setTitle("Select the note to add to the folder")
            .setItems(noteTitles) { _, which ->
                val selectedNote = noteList[which]
                // افزودن نوت به فولدر
                db.addNoteToFolder(selectedNote.noteid, folderId)
                Toast.makeText(context, "The note was added to the folder", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(true)
            .create()

        dialog.show()



    }




    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newfolder: List<Folder>) {
        folder = newfolder
        notifyDataSetChanged()
    }








}