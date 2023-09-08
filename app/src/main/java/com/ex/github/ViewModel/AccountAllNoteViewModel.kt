package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Note
import com.ex.github.Repository.AccountAllNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountAllNoteViewModel @Inject constructor(var repository: AccountAllNoteRepository) :
    ViewModel() {

    var currentUserAccountAllNoteMutableLiveData = MutableLiveData<List<Note>>()

    suspend fun getAllNote(loginUser: String): ArrayList<Note> {
        var list = repository.getAllNote(loginUser)
        currentUserAccountAllNoteMutableLiveData.value = list
        return list
    }

    suspend fun currentUser (): List<String> {
        return repository.currentUser()
    }
}