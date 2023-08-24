package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.ex.github.Comment
import com.ex.github.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AllCommentRepository @Inject constructor(private var database: FirebaseDatabase) {

    suspend fun getAllComment(login: String, commentToFavUser: String): ArrayList<Comment> {
        return suspendCoroutine { continuation ->
            try {
                val commentAllList: ArrayList<Comment> = ArrayList()
                val databaseReference = database.getReference("Comment")
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                for (j in i.children) {
                                    val comment = j.child("comment").getValue(String::class.java)!!
                                    val commentToUser =
                                        j.child("commentToUser").getValue(String::class.java)!!
                                    val login = j.child("login").getValue(String::class.java)!!

                                    if(commentToUser == commentToFavUser) {
                                        commentAllList.add(Comment(login, commentToUser, comment))
                                    }
                                }
                            }
// Yorum yapılanlar Commentin altında isim ile tutuluyor. Bu yüzden ; child(ivey) = ivey'se dedik
                            //key  = ivey
                        }
                        continuation.resume(commentAllList) // İşlem tamamlandığında listeyi döndür
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
                Log.d("xxxAllCom", commentAllList.toString())
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e) // Hata durumunda istisna fırlat
            }
        }
    }
}