package com.example.pengolahancitra.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.pengolahancitra.data.database.entity.Edit

class EditDiffCallback (private val mOldList: List<Edit>, private val mNewList: List<Edit>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContent = mOldList[oldItemPosition]
        val newContent = mNewList[newItemPosition]
        return oldContent.image == newContent.image
    }

}