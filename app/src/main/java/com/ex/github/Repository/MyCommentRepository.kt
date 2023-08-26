package com.ex.github.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ex.github.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyCommentRepository @Inject constructor(private var database: FirebaseDatabase) {


    suspend fun getMyComment(login: String, commentToFavUser: String): ArrayList<Comment> {
        return suspendCoroutine { continuation ->
            try {
                val commentList: ArrayList<Comment> = ArrayList()
                val databaseReference = database.getReference("Comment/${login}")
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val comment = i.child("comment").getValue(String::class.java)
                                val commentToUser = i.child("commentToUser").getValue(String::class.java)

                                if (commentToFavUser.equals(commentToUser)) {
                                    commentList.add(Comment(login, commentToUser, comment))
                                }
// Login = mojombo
// commentToFavUser = ivey
// mojombonun ivey'e yaptığı yorumları alabilmek için if else yaptık ?????
                            }
                            if (commentList.isNotEmpty()) {
                                continuation.resume(commentList) // İşlem tamamlandığında listeyi döndür
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

/*
    suspend fun getMyComments(
        login: String,
        context: Context
    ): List<Comment> {


        return suspendCoroutine { continuation ->
            try {
                val commentList: ArrayList<String> = ArrayList()
                val databaseReference = database.getReference("Comment/$login")
                val getData = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {

                                val comment = i.child("comment").getValue(String::class.java)
                                comment?.let {
                                    commentList.add(it)
                                }
                            }
                        } else {
                            Toast.makeText(context, "No comments found!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT)
                            .show()
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }*/
