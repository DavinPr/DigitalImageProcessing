package com.example.pengolahancitra.views.myphoto

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pengolahancitra.data.database.entity.Result
import com.example.pengolahancitra.data.repository.ProcessingRepository

class MyPhotoViewModel(application: Application) : ViewModel() {
    private val mRepository = ProcessingRepository(application)

    fun getAllDataResult() : LiveData<List<Result>>{
        return mRepository.getAllDataResult()
    }

    fun deleteImageResult(result: Result) {
        return mRepository.deleteFromResult(result)
    }

}