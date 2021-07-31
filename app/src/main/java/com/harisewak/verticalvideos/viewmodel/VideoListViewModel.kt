package com.harisewak.verticalvideos.viewmodel

import androidx.lifecycle.*
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.data.VideoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class VideoListViewModel(private val repository: VideoListRepository) : ViewModel() {

    private var _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> = _videoList


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getListOfVideos()
            _videoList.postValue(result)
        }
    }


    companion object {

        class VideoListViewModelFactory(
            private val repository: VideoListRepository
        ) : ViewModelProvider.Factory {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                VideoListViewModel(repository) as T

        }
    }

}