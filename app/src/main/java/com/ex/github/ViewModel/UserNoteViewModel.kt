package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.UserNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserNoteViewModel @Inject constructor(private var repository : UserNoteRepository) : ViewModel() {

    var currentUserAddNoteMutableLiveData = MutableLiveData<Boolean>()

    suspend fun addNote(loginUser: String,noteToUserOrRepository : String, note: String, isUserOrRepository : String) : Boolean {

        var myNote = repository.addNote(loginUser, noteToUserOrRepository, note, isUserOrRepository)
        currentUserAddNoteMutableLiveData.value = myNote
        return myNote
    }
}
