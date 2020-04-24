package com.example.pengolahancitra.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.pengolahancitra.data.database.AppDatabase
import com.example.pengolahancitra.data.database.Dao
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.database.entity.Result
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProcessingRepository(application: Application) {
    private val mDao: Dao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AppDatabase.getDatabase(application)
        mDao = db.processingDao()
    }

    fun getAllDataEdit(): LiveData<List<Edit>> = mDao.getAllDataEdit()

    fun getDataEditById(id : Int) : LiveData<Edit> = mDao.getDataEditById(id)

    fun getAllDataResult(): LiveData<List<Result>> = mDao.getAllDataResult()

    fun getDataResultById(id : Int) : LiveData<Result> = mDao.getDataResultById(id)

    fun insertIntoEdit(edit: Edit) {
        executorService.execute { mDao.insertEdit(edit) }
    }

    fun insertIntoResult(result: Result) {
        executorService.execute { mDao.insertResult(result) }
    }

    fun updateFromResult(result: Result) {
        executorService.execute { mDao.updateResult(result) }
    }

    fun deleteFromEdit(edit: Edit) {
        executorService.submit { mDao.deleteEdit(edit) }
    }

    fun deleteFromResult(result: Result) {
        executorService.submit { mDao.deleteResult(result) }
    }
}