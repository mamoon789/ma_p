package com.myisolutions.quran.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myisolutions.quran.repository.Repository

class MainViewModelProviderFactory(
    val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}