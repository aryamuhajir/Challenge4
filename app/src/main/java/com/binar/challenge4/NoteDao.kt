package com.binar.challenge4

import androidx.room.*


@Dao
interface NoteDao {

    @Insert
    fun insertNote(student : Note) : Long
    @Query("SELECT * FROM Note")
    fun getAllNote() : List<Note>
    @Delete
    fun deleteNote(note : Note) : Int
    @Update
    fun updateNote(note : Note) : Int



}