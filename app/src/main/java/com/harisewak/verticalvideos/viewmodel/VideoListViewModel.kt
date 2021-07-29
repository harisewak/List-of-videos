package com.harisewak.verticalvideos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.data.VideoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListViewModel @Inject constructor(private val repository: VideoListRepository): ViewModel() {

    private var _videoList = MutableLiveData<List<Video>>()
    val videoList : LiveData<List<Video>> = _videoList


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getListOfVideos()
            _videoList.postValue(result)
        }
    }

}