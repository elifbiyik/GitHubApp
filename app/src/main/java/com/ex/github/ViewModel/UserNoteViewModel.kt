package com.ex.github.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Comment
import com.ex.github.Repository.UserNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserNoteViewModel @Inject constructor(private var repository : UserNoteRepository) : ViewModel() {

    var currentUserAddCommentMutableLiveData = MutableLiveData<Boolean>()

    suspend fun addComment(login: String,commentToUser : String, comment: String) : Boolean {

        var myComment = repository.addComment(login, commentToUser, comment)
        currentUserAddCommentMutableLiveData.value = myComment
        return myComment
    }
}
