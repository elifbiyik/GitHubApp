package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import com.ex.github.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyNoteRepository @Inject constructor(private var database: FirebaseDatabase) {


    suspend fun getMyNote(loginUser: String, noteToFavUser: String, isUserOrRepo: String): ArrayList<Note> {
        return suspendCoroutine { continuation ->
            try {
                val noteList: ArrayList<Note> = ArrayList()
                val databaseReference: DatabaseReference?
                if (isUserOrRepo == "User") {
                    databaseReference = database.getReference("Note/User/${loginUser}")
                } else if (isUserOrRepo == "Repository") {
                    databaseReference = database.getReference("Note/Repository/${loginUser}")
                }else {
                    // Eğer isUserOrRepo ne "User" ne de "Repository" değilse, hata durumu
                    continuation.resumeWithException(IllegalArgumentException("Invalid value for isUserOrRepo"))
                    return@suspendCoroutine
                }
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val note = i.child("note").getValue(String::class.java)
                                val noteToUserOrRepository = i.child("noteToUserOrRepository").getValue(String::class.java)

                                if (noteToFavUser.equals(noteToUserOrRepository)) {
                                    noteList.add(Note(loginUser, noteToUserOrRepository, note))
                                }
// Login = mojombo
// noteToFavUser = ivey
// mojombonun ivey'e yaptığı yorumları alabilmek için if else yaptık ?????
                            }
                            if (noteList.isNotEmpty()) {
                                continuation.resume(noteList) // İşlem tamamlandığında listeyi döndür
                            } else {
                                continuation.resume(ArrayList()) // Veri yoksa boş liste döndür
                            }
                        } else {
                            continuation.resume(ArrayList()) // Veri yoksa boş liste döndür
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException()) // Hata durumunda istisna fırlat
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e) // Hata durumunda istisna fırlat
            }
        }
    }
}
