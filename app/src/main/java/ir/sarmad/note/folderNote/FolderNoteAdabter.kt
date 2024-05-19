package ir.sarmad.note.folderNote

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.folder.UpdateFolder
import ir.sarmad.note.note.Note
import ir.sarmad.note.note.UpdateNote

class FolderNoteAdabter(private var notes: List<Note>, private val db: DBManager, private val context: Context,):
    RecyclerView.Adapter<FolderNoteAdabter.NotesviewHolder>() {

    class NotesviewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var tvtitle: TextView = itemview.findViewById(R.id.titletextview)
        var tvcontent: TextView = itemview.findViewById(R.id.conenttextview)
        var tvdate: TextView = itemview.findViewById(R.id.datetextview)
        var updatebutton: ImageView = itemview.findViewById(R.id.btnupdate)
        var deletbutton: ImageView = itemview.findViewById(R.id.btndelet)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesviewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NotesviewHolder(view)
    }

    override fun getItemCount(): Int = notes.size


    override fun onBindViewHolder(holder: NotesviewHolder, position: Int) {
        var note = notes[position]
        holder.tvtitle.text = note.notetitle
        holder.tvcontent.text = note.notecontent
        holder.tvdate.text=note.notedate
        holder.updatebutton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNote::class.java).apply {
                putExtra("note_id", note.noteid)

            }
            holder.itemView.context.startActivity(intent)
        }






        holder.deletbutton.setOnClickListener {
            val alertDialog= AlertDialog.Builder(holder.itemView.context)
                .setTitle("Are you sure you want to delete?")
                .setMessage("There is no going back after the change")
                .setPositiveButton("Yes"){_,_->
                    val noteId =note.noteid
                    val folderId = (context as Activity).intent.getIntExtra("folder_id", 0)
                        db.deleteNoteInFolder(noteId, folderId)
                    refreshData(db.getNotesInFolder(folderId))


                }

                .setNegativeButton("No"){_,_->


                }
            alertDialog.show()



        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newnotes: List<Note>) {
        notes = newnotes
        notifyDataSetChanged()
    }






}





