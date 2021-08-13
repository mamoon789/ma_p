package com.myisolutions.quran.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myisolutions.quran.network.models.Quran
import com.myisolutions.quran.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class MainViewModel(val repository: Repository) : ViewModel() {

    val quran : MutableLiveData<Quran> = MutableLiveData()
    val quranImage : MutableLiveData<ResponseBody> = MutableLiveData()

    fun getQuran() = viewModelScope.async {
        quran.postValue(repository.getQuran())
    }

    fun getQuranImage(page: String) = viewModelScope.async {
        quranImage.postValue(repository.getQuranImage(page))
    }

}