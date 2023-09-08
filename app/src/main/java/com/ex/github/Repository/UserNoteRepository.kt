package com.ex.github.Repository


import android.annotation.SuppressLint
import android.util.Log
import com.ex.github.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserNoteRepository @Inject constructor(private var database: FirebaseDatabase, var auth: FirebaseAuth) {

    private val databaseReferenceNoteForUser = database.getReference("Note/Favorite User")
    private val databaseReferenceNoteForRepository = database.getReference("Note/Favorite Repository")

    suspend fun addNote(
        loginUser: String,
        noteToUserOrRepository : String,
        note: String,
        isUserOrRepository : String
    ): Boolean {
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

    suspend fun currentUser(): List<String> {
        var currentUserPhone = auth.currentUser?.phoneNumber.toString()
        return suspendCoroutine { continuation ->
            try {
                val userInfoList: ArrayList<String> = ArrayList()
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("User").child(currentUserPhone)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentUserLogin =
                                snapshot.child("login").getValue(String::class.java).toString()
                            var currentUserPhone =
                                snapshot.child("phoneNumber").getValue(String::class.java).toString()

                            userInfoList.add(currentUserLogin)
                            userInfoList.add(currentUserPhone)
                            continuation.resume(userInfoList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }
}