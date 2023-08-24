package com.ex.github.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Comment
import com.ex.github.Repository.AllCommentRepository
import com.ex.github.User
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCommentViewModel @Inject constructor(var repository: AllCommentRepository) : ViewModel() {

    var currentUserAllCommentMutableLiveData = MutableLiveData<List<Comment>>()

    suspend fun getAllComment(login: String, commentToFavUser: String): ArrayList<Comment> {
        var list = repository.getAllComment(login, commentToFavUser)
        currentUserAllCommentMutableLiveData.value = list
        return list
    }

}