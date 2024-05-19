package ir.sarmad.note.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class   NoteDataBaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {


    companion object {
        private const val DATABASE_NAME = "Noteapp.dp"
        private const val DATABASE_VERSION = 2


        const val TABLE_NOTENAME = "allnotes"
        const val TABLE_FOLDERNAME = "folder"
        const val TABLE_RELATIONSHIP = "Relationship"


        const val COLUMN_NOTEID = "noteid"
        const val COLUMN_NOTETITLE = "notetitle"
        const val COLUMN_NOTECONTENT = "notecontent"
        const val COLUMN_NOTEDATE="notedate"


        const val COLUMN_FOLDERID="folderid"
        const val COLUMN_FOLDERTITLE="foldertitle"


        const val COLUMN_RELATIONID="relationshipId"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NOTENAME($COLUMN_NOTEID INTEGER PRIMARY KEY,$COLUMN_NOTETITLE TEXT,$COLUMN_NOTECONTENT TEXT, $COLUMN_NOTEDATE TEXT)"
        db?.execSQL(createTableQuery)

        val formcreateTableQuery =
            "CREATE TABLE $TABLE_FOLDERNAME($COLUMN_FOLDERID INTEGER PRIMARY KEY,$COLUMN_FOLDERTITLE TEXT)"
        db?.execSQL(formcreateTableQuery)


        val createRelationTableQuery =
            "CREATE TABLE $TABLE_RELATIONSHIP($COLUMN_RELATIONID INTEGER PRIMARY KEY, $COLUMN_NOTEID INTEGER, $COLUMN_FOLDERID INTEGER, FOREIGN KEY($COLUMN_NOTEID) REFERENCES $TABLE_NOTENAME($COLUMN_NOTEID), FOREIGN KEY($COLUMN_FOLDERID) REFERENCES $TABLE_FOLDERNAME($COLUMN_FOLDERID))"
        db?.execSQL(createRelationTableQuery)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NOTENAME"
        db?.execSQL(dropTableQuery)

        val dropTableQuery2 = "DROP TABLE IF EXISTS $TABLE_FOLDERNAME"
        db?.execSQL(dropTableQuery2)



        val dropTableQuery3 = "DROP TABLE IF EXISTS $TABLE_RELATIONSHIP"
        db?.execSQL(dropTableQuery3)
        onCreate(db)
    }




}