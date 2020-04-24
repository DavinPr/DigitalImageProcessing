package com.example.pengolahancitra.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pengolahancitra.views.edit.EditViewModel
import com.example.pengolahancitra.views.myphoto.MyPhotoViewModel
import com.example.pengolahancitra.views.processing.ProcessingViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditViewModel::class.java) -> {
                EditViewModel(mApplication) as T
            }
            modelClass.isAssignableFrom(ProcessingViewModel::class.java) -> {
                ProcessingViewModel(mApplication) as T
            }
            modelClass.isAssignableFrom(MyPhotoViewModel::class.java) -> {
                MyPhotoViewModel(mApplication) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }
}