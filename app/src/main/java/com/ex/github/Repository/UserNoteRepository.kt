package com.ex.github.Repository


import android.util.Log
import com.ex.github.Comment
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import javax.inject.Inject

class UserNoteRepository @Inject constructor(private var database: FirebaseDatabase) {


    private val databaseReferenceComment =
        database.getReference("Comment")


    suspend fun addComment(
        login: String,
        commentToUser : String,
        comment: String
    ): Boolean {
        try {
            val userComment = Comment(login, commentToUser, comment)
            databaseReferenceComment.child(login).child(commentToUser).setValue(userComment)
            return true
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
            return false
        }
    }
}