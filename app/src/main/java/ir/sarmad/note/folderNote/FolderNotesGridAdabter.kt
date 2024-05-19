package ir.sarmad.note.folderNote

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager
import ir.sarmad.note.note.Note
import ir.sarmad.note.note.UpdateNote

class FolderNotesGridAdabter(private var notes: List<Note>, private val db: DBManager) :
    RecyclerView.Adapter<FolderNotesGridAdabter.NotesviewHolder>() {
    class NotesviewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var tvtitle: TextView = itemview.findViewById(R.id.gridtitletextview)
        var tvcontent: TextView = itemview.findViewById(R.id.gridconenttextview)
        var updatebutton: ImageView = itemview.findViewById(R.id.gridbtnupdate)
        var deletbutton: ImageView = itemview.findViewById(R.id.gridbtndelet)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesviewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.notegrid, parent, false)
        return NotesviewHolder(view)
    }

    override fun getItemCount(): Int = notes.size


    override fun onBindViewHolder(holder: NotesviewHolder, position: Int) {
        var note = notes[position]
        holder.tvtitle.text = note.notetitle
        holder.tvcontent.text = note.notecontent
        holder.updatebutton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNote::class.java).apply {
                putExtra("note_id", note.noteid)

            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deletbutton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("ایا مطمعنید که میخواهید حذف کنید؟")
                .setMessage("بعد از تغییر دیگر راه بازگشتی نیست")
                .setPositiveButton("بله") { _, _ ->
                    db.noteDelete(note.noteid)


                }

                .setNegativeButton("خیر") { _, _ ->


                }
            alertDialog.show()


        }


    }


    fun refreshData(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }


}