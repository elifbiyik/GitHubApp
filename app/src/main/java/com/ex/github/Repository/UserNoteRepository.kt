package com.ex.github.Repository


import android.util.Log
import com.ex.github.Note
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import javax.inject.Inject

class UserNoteRepository @Inject constructor(private var database: FirebaseDatabase) {


    private val databaseReferenceNote =
        database.getReference("Note")


    suspend fun addNote(
        loginUser: String,
        noteToUserOrRepository : String,
        note: String,
        isUserOrRepository : String
    ): Boolean {             // var key = databaseReferenceNote.push().getKey()
        try {
            val userNote = Note(loginUser, noteToUserOrRepository, note)
            if (isUserOrRepository == "User") {
                databaseReferenceNote.child("User").child(loginUser).child(noteToUserOrRepository).setValue(userNote)
            } else if (isUserOrRepository == "Repository") {
                databaseReferenceNote.child("Repository").child(loginUser).child(noteToUserOrRepository).setValue(userNote)
            }
            return true
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
            return false
        }
    }
}