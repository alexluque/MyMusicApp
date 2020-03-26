package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchArtistViewModel : ViewModel() {

    sealed class UiModel {
        object Create : UiModel()
        object Search : UiModel()
        object Cancel : UiModel()
    }

    private val innerModel = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (innerModel.value == null) refresh()
            return innerModel
        }

    private fun refresh() {
        innerModel.value = UiModel.Create
    }

    fun onSearchClicked() {
        innerModel.value = UiModel.Search
    }

    fun onCancelClicked() {
        innerModel.value = UiModel.Cancel
    }
}





















