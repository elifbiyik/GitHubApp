package com.ex.github.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Comment
import com.ex.github.Repository.MyCommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCommentViewModel @Inject constructor(var repository: MyCommentRepository) : ViewModel() {

    var currentUserMyCommentMutableLiveData = MutableLiveData<List<Comment>>()

    suspend fun getMyComment(login: String, commentToFavUser: String): ArrayList<Comment> {
        var list = repository.getMyComment(login, commentToFavUser)
        currentUserMyCommentMutableLiveData.value = list
        return list
    }

}