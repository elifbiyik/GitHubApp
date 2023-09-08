package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Note
import com.ex.github.Repository.AccountMyNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountMyNoteViewModel @Inject constructor(var repository: AccountMyNoteRepository) :
    ViewModel() {

    var currentUserAccountMyNoteMutableLiveData = MutableLiveData<List<Note>>()

    suspend fun getMyNote(loginUser: String): ArrayList<Note> {
        var list = repository.getMyNote(loginUser)
        currentUserAccountMyNoteMutableLiveData.value = list
        return list
    }

    suspend fun currentUser (): List<String> {
        return repository.currentUser()
    }
}