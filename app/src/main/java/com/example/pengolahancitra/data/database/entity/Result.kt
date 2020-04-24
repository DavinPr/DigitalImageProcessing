package com.example.pengolahancitra.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Result(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_id")
    var resultId : Int = 0,

    @ColumnInfo(name = "result_image")
    var resultImage : String? = null,

    @ColumnInfo(name = "filtered_image")
    var filteredImage : String? = null,

    @ColumnInfo(name="result_adjustment")
    var resultAdjustment: Int? = 0,

    @ColumnInfo(name = "filter_kernel")
    var  filterKernel: Int? = 0
) : Parcelable