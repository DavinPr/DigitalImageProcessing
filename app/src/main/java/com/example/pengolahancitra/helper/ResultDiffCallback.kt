package com.example.pengolahancitra.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.pengolahancitra.data.database.entity.Result

class ResultDiffCallback (private val mOldList: List<Result>, private val mNewList: List<Result>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].resultId == mNewList[newItemPosition].resultId
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
        return oldContent.resultImage == newContent.resultImage && oldContent.resultAdjustment == newContent.resultAdjustment
    }

}