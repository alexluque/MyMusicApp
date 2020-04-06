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

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    private fun refresh() {
        _model.value = UiModel.Create
    }

    fun onSearchClicked() {
        _model.value = UiModel.Search
    }

    fun onCancelClicked() {
        _model.value = UiModel.Cancel
    }
}





















