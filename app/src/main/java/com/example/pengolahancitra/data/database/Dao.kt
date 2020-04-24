package com.example.pengolahancitra.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.database.entity.Result

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEdit(edit: Edit)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertResult(result: Result)

    @Update
    fun updateEdit(edit: Edit)

    @Update
    fun updateResult(result: Result)

    @Delete
    fun deleteEdit(edit: Edit)

    @Delete
    fun deleteResult(result: Result)

    @Query("SELECT * FROM edit ORDER BY edit_id ASC")
    fun getAllDataEdit() : LiveData<List<Edit>>

    @Query("SELECT * FROM edit WHERE edit_id = :id")
    fun getDataEditById(id : Int) : LiveData<Edit>

    @Query("SELECT * FROM result ORDER BY result_id ASC")
    fun getAllDataResult() : LiveData<List<Result>>

    @Query("SELECT * FROM result WHERE result_id = :id")
    fun getDataResultById(id : Int) : LiveData<Result>
}