package ir.sarmad.note.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context

import android.widget.Toast

import ir.sarmad.note.folder.Folder
import ir.sarmad.note.note.Note
import java.sql.SQLException

@Suppress("UNREACHABLE_CODE")
class DBManager(private val context: Context, private val dbListener: DbListener) {

    private var dbHelper = NoteDataBaseHelper(context)
    private var database = dbHelper.writableDatabase

    @Throws(SQLException::class)
    fun open(): DBManager {
        dbHelper = NoteDataBaseHelper(context)
        database = dbHelper.writableDatabase
        return this
    }

     fun close() {
        dbHelper.close()
    }

     fun insertNote(note: Note) {
        val values = ContentValues().apply {
            put(
                NoteDataBaseHelper.COLUMN_NOTECONTENT,
                note.notecontent
            )//اولی نام ستون و دومی مقدار آن
            put(NoteDataBaseHelper.COLUMN_NOTETITLE, note.notetitle)
            put(NoteDataBaseHelper.COLUMN_NOTEDATE, note.notedate)
        }
        database.insert(NoteDataBaseHelper.TABLE_NOTENAME, null, values)

        dbListener.onAdded()
        close()
    }


    fun insertFolder(folder: Folder) {
        val values = ContentValues().apply {
            put(NoteDataBaseHelper.COLUMN_FOLDERTITLE, folder.foldertitle)
        }
        database.insert(NoteDataBaseHelper.TABLE_FOLDERNAME, null, values)
        dbListener.onAdded()
        close()
    }





    @SuppressLint("Range")
    fun getAllNotes(): MutableList<Note> {
        val notelis = mutableListOf<Note>()
        val db = dbHelper.readableDatabase
        val query = "SELECT*FROM ${NoteDataBaseHelper.TABLE_NOTENAME}"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEID))
            val title = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTETITLE))
            val content =
                cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTECONTENT))
            val date = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEDATE))
            val note = Note(id, title, content, date)
            notelis.add(note)

        }
        cursor.close()
        db.close()


        return notelis
    }


    @SuppressLint("Range")
    fun getAllFolder(): MutableList<Folder> {
        val folderList = mutableListOf<Folder>()
        val db = dbHelper.readableDatabase
        val query1 = "SELECT*FROM ${NoteDataBaseHelper.TABLE_FOLDERNAME}"
        val cursor = db.rawQuery(query1, null)

        while (cursor.moveToNext()) {
            val folderid = cursor.getInt(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_FOLDERID))
            val foldertitle =
                cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_FOLDERTITLE))
            val folder = Folder(folderid, foldertitle)
            folderList.add(folder)

        }
        cursor.close()
        db.close()


        return folderList
    }


    fun noteUpdate(note: Note) {
        val values = ContentValues().apply {
            put(NoteDataBaseHelper.COLUMN_NOTETITLE, note.notetitle)
            put(NoteDataBaseHelper.COLUMN_NOTECONTENT, note.notecontent)
        }
        val whereClause = "${NoteDataBaseHelper.COLUMN_NOTEID}=?"
        val whereArgs = arrayOf(note.noteid.toString())
        database.update(NoteDataBaseHelper.TABLE_NOTENAME, values, whereClause, whereArgs)
        database.close()

    }


    fun folderUpdate(folder: Folder) {
        val values = ContentValues().apply {
            put(NoteDataBaseHelper.COLUMN_FOLDERTITLE,folder.foldertitle)
        }
        val whereClause = "${NoteDataBaseHelper.COLUMN_FOLDERID}=?"
        val whereArgs = arrayOf(folder.folderid.toString())
        database.update(NoteDataBaseHelper.TABLE_FOLDERNAME, values, whereClause, whereArgs)
        database.close()

    }



    fun noteDelete(id: Int) {
        open()
        database.delete(
            NoteDataBaseHelper.TABLE_NOTENAME,
            "${NoteDataBaseHelper.COLUMN_NOTEID} = $id",
            null
        )
        close()
    }


    fun formDelete(id: Int) {
        open()
        database.delete(
            NoteDataBaseHelper.TABLE_FOLDERNAME,
            "${NoteDataBaseHelper.COLUMN_FOLDERID} = $id",
            null
        )
        close()
    }


    fun relationDeleteByFolderId(folderId: Int) {
        open()
        database.delete(
            NoteDataBaseHelper.TABLE_RELATIONSHIP,
            "${NoteDataBaseHelper.COLUMN_FOLDERID} = $folderId",
            null
        )
        close()
    }



    fun insertNoteIfNotExists(note: Note) {
        val title = note.notetitle
        val content = note.notecontent
        val db = dbHelper.readableDatabase
        val query =
            "SELECT COUNT(*) FROM ${NoteDataBaseHelper.TABLE_NOTENAME} WHERE ${NoteDataBaseHelper.COLUMN_NOTETITLE} = ? AND ${NoteDataBaseHelper.COLUMN_NOTECONTENT} = ?"
        val cursor = db.rawQuery(query, arrayOf(title, content))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()



        if (count == 0) {
            insertNote(note)
        } else {
            Toast.makeText(context, "You have already added", Toast.LENGTH_LONG).show()
        }
    }


    @SuppressLint("Range")
    fun getNoteById(noteid: Int): Note {
        val db = dbHelper.readableDatabase
        val query =
            "SELECT*FROM ${NoteDataBaseHelper.TABLE_NOTENAME} WHERE ${NoteDataBaseHelper.COLUMN_NOTEID}=$noteid"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEID))
        val title = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTETITLE))
        val content = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTECONTENT))
        val date = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEDATE))
        return Note(id, title, content, date)
        cursor.close()
        db.close()
    }


    @SuppressLint("Range")
    fun getFolderById(folderid: Int): Folder {
        val db = dbHelper.readableDatabase
        val query =
            "SELECT*FROM ${NoteDataBaseHelper.TABLE_FOLDERNAME} WHERE ${NoteDataBaseHelper.COLUMN_FOLDERID}=$folderid"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_FOLDERID))
        val title = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_FOLDERTITLE))
        return Folder(id, title)
        cursor.close()
        db.close()
    }

    fun addNoteToFolder(noteId: Int, folderId: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NoteDataBaseHelper.COLUMN_NOTEID, noteId)
            put(NoteDataBaseHelper.COLUMN_FOLDERID, folderId)
        }
        db.insert(NoteDataBaseHelper.TABLE_RELATIONSHIP, null, values)
        db.close()
    }



    @SuppressLint("Range")
    fun getNotesInFolder(folderId: Int): MutableList<Note> {
        val noteList = mutableListOf<Note>()
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${NoteDataBaseHelper.TABLE_NOTENAME} INNER JOIN ${NoteDataBaseHelper.TABLE_RELATIONSHIP} ON ${NoteDataBaseHelper.TABLE_NOTENAME}.${NoteDataBaseHelper.COLUMN_NOTEID} = ${NoteDataBaseHelper.TABLE_RELATIONSHIP}.${NoteDataBaseHelper.COLUMN_NOTEID} WHERE ${NoteDataBaseHelper.TABLE_RELATIONSHIP}.${NoteDataBaseHelper.COLUMN_FOLDERID} = $folderId"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEID))
            val title = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTETITLE))
            val content = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTECONTENT))
            val date = cursor.getString(cursor.getColumnIndex(NoteDataBaseHelper.COLUMN_NOTEDATE))
            val note = Note(id, title, content, date)
            noteList.add(note)
        }

        cursor.close()
        db.close()

        return noteList
    }


    fun deleteNoteInFolder(noteId: Int, folderId: Int) {
        open()
        // ابتدا باید رابطه مربوط به نوت مورد نظر را از جدول روابط حذف کنیم
        database.delete(
            NoteDataBaseHelper.TABLE_RELATIONSHIP,
            "${NoteDataBaseHelper.COLUMN_NOTEID} = $noteId AND ${NoteDataBaseHelper.COLUMN_FOLDERID} = $folderId",
            null
        )
        close()
    }



}