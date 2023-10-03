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

class AccountAllNoteRepository @Inject constructor(
    private var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {
    private val databaseReferenceNoteForUser = database.getReference("Note/Favorite User")
    suspend fun getAllNote(loginUser: String): ArrayList<Note> {
        return suspendCoroutine { continuation ->
            try {
                val noteAllList: ArrayList<Note> = ArrayList()
                val databaseReference = databaseReferenceNoteForUser
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (j in snapshot.children) {
                                for (i in j.children) {
                                    val note = i.child("note").getValue(String::class.java)
                                    val noteToUserOrRepository = i.child("noteToUserOrRepository").getValue(String::class.java)
                                    val login = i.child("login").getValue(String::class.java)

                                    if (noteToUserOrRepository == loginUser) {
                                        noteAllList.add(Note(login, noteToUserOrRepository, note))
                                    }
                                }
                            }
                        }
                        continuation.resume(noteAllList) // İşlem tamamlandığında listeyi dönd.
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
                                snapshot.child("phoneNumber").getValue(String::class.java)
                                    .toString()

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