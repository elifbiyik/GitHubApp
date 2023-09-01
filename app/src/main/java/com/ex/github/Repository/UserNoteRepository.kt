package com.ex.github.Repository


import android.util.Log
import com.ex.github.Note
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import javax.inject.Inject

class UserNoteRepository @Inject constructor(private var database: FirebaseDatabase) {

    private val databaseReferenceNoteForUser = database.getReference("Note/Favorite User")
    private val databaseReferenceNoteForRepository = database.getReference("Note/Favorite Repository")

    suspend fun addNote(
        loginUser: String,
        noteToUserOrRepository : String,
        note: String,
        isUserOrRepository : String
    ): Boolean {             // var key = databaseReferenceNote.push().getKey()
        try {
            val userNote = Note(loginUser, noteToUserOrRepository, note)
            if (isUserOrRepository == "User") {
                databaseReferenceNoteForUser.child(loginUser).child(noteToUserOrRepository).setValue(userNote)
            } else if (isUserOrRepository == "Repository") {
                databaseReferenceNoteForRepository.child(loginUser).child(noteToUserOrRepository).setValue(userNote)
            }
            return true
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
            return false
        }
    }
}