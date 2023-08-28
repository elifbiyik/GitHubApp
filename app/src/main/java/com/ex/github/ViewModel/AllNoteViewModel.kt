package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Note
import com.ex.github.Repository.AllNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllNoteViewModel @Inject constructor(var repository: AllNoteRepository) : ViewModel() {

    var currentUserAllNoteMutableLiveData = MutableLiveData<List<Note>>()

    suspend fun getAllNote(noteToFavUser: String, isUserOrRepository : String): ArrayList<Note> {
        var list = repository.getAllNote(noteToFavUser, isUserOrRepository)
        currentUserAllNoteMutableLiveData.value = list
        return list
    }

}