package com.example.pengolahancitra.views.edit

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.repository.ProcessingRepository
import com.example.pengolahancitra.helper.BitmapHelper
import kotlinx.coroutines.*

class EditViewModel(application: Application) : ViewModel() {
    private val mRepository: ProcessingRepository = ProcessingRepository(application)
    private val bitmapHelper = BitmapHelper(application.applicationContext)

    fun getAllImageEditable(): LiveData<List<Edit>> = mRepository.getAllDataEdit()

    fun insertImageEditable(edit: Edit) {
        mRepository.insertIntoEdit(edit)
    }

    fun deleteImageEditable(edit: Edit) {
        mRepository.deleteFromEdit(edit)
    }

    fun bitmapToString(bitmap: Bitmap): String? {
        return bitmapHelper.bitmapToString(bitmap)
    }

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun getBitmapAsync(uri: Uri): Deferred<Bitmap> =
        coroutineScope.async(Dispatchers.Default) {
            bitmapHelper.uriToBitmap(uri)!!
        }

}