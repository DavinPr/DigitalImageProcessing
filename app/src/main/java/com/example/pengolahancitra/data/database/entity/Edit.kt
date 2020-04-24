package com.example.pengolahancitra.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Edit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "edit_id")
    var id: Int = 0,

    @ColumnInfo(name = "edit_image")
    var image: String? = null
) : Parcelable