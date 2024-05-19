package ir.sarmad.note.note

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import ir.sarmad.note.R
import ir.sarmad.note.database.DBManager

class NotesGridAdabter(private var notes: List<Note>, private val db: DBManager) :
    RecyclerView.Adapter<NotesGridAdabter.NotesviewHolder>() {
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
        val note = notes[position]
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
                .setTitle("Are you sure you want to delete?")
                .setMessage("There is no going back after the change")
                .setPositiveButton("Yes") { _, _ ->
                    db.noteDelete(note.noteid)
                    refreshData(db.getAllNotes())

                }

                .setNegativeButton("No") { _, _ ->


                }
            alertDialog.show()


        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }


}