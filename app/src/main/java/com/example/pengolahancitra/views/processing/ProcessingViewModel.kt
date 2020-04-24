package com.example.pengolahancitra.views.processing

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.database.entity.Result
import com.example.pengolahancitra.data.repository.ProcessingRepository
import com.example.pengolahancitra.helper.BitmapHelper
import com.example.pengolahancitra.helper.ImageProcessingHelper
import kotlinx.coroutines.*

class ProcessingViewModel(application: Application) : ViewModel() {
    private val mRepository = ProcessingRepository(application)
    private val bitmapHelper = BitmapHelper(application.applicationContext)
    private val imageProcessingHelper = ImageProcessingHelper()

    fun getDataEdit(id: Int): LiveData<Edit> {
        return mRepository.getDataEditById(id)
    }

    fun getDataResult(id: Int): LiveData<Result> {
        return mRepository.getDataResultById(id)
    }

    fun insertData(result: Result) {
        return mRepository.insertIntoResult(result)
    }

    fun updateData(result: Result){
        return mRepository.updateFromResult(result)
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        return bitmapHelper.stringToBitmap(encodedString)
    }

    fun bitmapToString(bitmap: Bitmap) : String? {
        return bitmapHelper.bitmapToString(bitmap)
    }

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun getBrightnessAsync(bitmap: Bitmap, adjustValue: Int): Deferred<Bitmap> =
        coroutineScope.async(Dispatchers.IO) {
            imageProcessingHelper.getBrightnessAdjusment(bitmap, adjustValue)
        }

    fun getBlurAsync(bitmap: Bitmap, kernel: Int): Deferred<Bitmap> =
        coroutineScope.async(Dispatchers.IO) {
            imageProcessingHelper.getBlurEffect(bitmap, kernel)
        }

}