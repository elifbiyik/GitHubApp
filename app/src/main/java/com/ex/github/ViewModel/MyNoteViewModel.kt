package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Note
import com.ex.github.Repository.MyNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyNoteViewModel @Inject constructor(var repository: MyNoteRepository) : ViewModel() {

    var currentUserMyNoteMutableLiveData = MutableLiveData<List<Note>>()

    suspend fun getMyNote(loginUser: String, noteToFavUser: String, isUserOrRepo : String): ArrayList<Note> {
        var list = repository.getMyNote(loginUser, noteToFavUser, isUserOrRepo)
        currentUserMyNoteMutableLiveData.value = list
        return list
    }

    suspend fun currentUser (): List<String> {
        return repository.currentUser()
    }
}