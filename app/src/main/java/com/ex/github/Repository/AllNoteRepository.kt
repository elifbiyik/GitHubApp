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

class AllNoteRepository @Inject constructor(private var database: FirebaseDatabase) {

    private val databaseReferenceNoteForUser = database.getReference("Note/Favorite User")

    private val databaseReferenceNoteForRepository = database.getReference("Note/Favorite Repository")

    suspend fun getAllNote(noteToFavUser: String, isUserOrRepository: String): ArrayList<Note> {
        return suspendCoroutine { continuation ->
            try {
                val noteAllList: ArrayList<Note> = ArrayList()
                val databaseReference: DatabaseReference  // databaseReference'ı burada tanımla
                if (isUserOrRepository == "User") {
                    databaseReference = databaseReferenceNoteForUser
                } else if (isUserOrRepository == "Repository") {
                    databaseReference = databaseReferenceNoteForRepository
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
                                for (j in i.children) {
                                    val note = j.child("note").getValue(String::class.java)
                                    val noteToUserOrRepository =
                                        j.child("noteToUserOrRepository").getValue(String::class.java)
                                    val login = j.child("login").getValue(String::class.java)

                                    if(noteToUserOrRepository == noteToFavUser) {
                                        noteAllList.add(Note(login, noteToUserOrRepository, note))
                                    }
                                }
                            }
// Yorum yapılanlar Note'un altında isim ile tutuluyor. Bu yüzden ; child(ivey) = ivey'se dedik
                            //key  = ivey
                        }
                        continuation.resume(noteAllList) // İşlem tamamlandığında listeyi döndür
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
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