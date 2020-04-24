package com.example.pengolahancitra.views.myphoto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pengolahancitra.R
import com.example.pengolahancitra.data.database.entity.Result
import com.example.pengolahancitra.helper.BitmapHelper
import com.example.pengolahancitra.helper.ResultDiffCallback
import com.example.pengolahancitra.views.processing.ProcessingActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.home_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_edit.view.*

class MyPhotoAdapter internal constructor(
    private val activity: Activity,
    private val fm: FragmentManager
) :
    RecyclerView.Adapter<MyPhotoAdapter.MyPhotoViewHolder>() {
    private val listResult = ArrayList<Result>()
    fun setList(listResult: List<Result>) {
        val diffCallback = ResultDiffCallback(this.listResult, listResult)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listResult.clear()
        this.listResult.addAll(listResult)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit, parent, false)
        return MyPhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listResult.size
    }

    override fun onBindViewHolder(holder: MyPhotoViewHolder, position: Int) {
        holder.bind(listResult[position])
    }

    inner class MyPhotoViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val bitmapHelper = BitmapHelper(activity.applicationContext)
        fun bind(result: Result) {
            with(itemView) {
                img_edit.clipToOutline = true
                img_edit.setImageBitmap(bitmapHelper.stringToBitmap(result.filteredImage!!))
                img_edit.setOnClickListener {
                    val intent = Intent(activity, ProcessingActivity::class.java)
                    intent.putExtra(ProcessingActivity.ACTIVITY_CODE, 2)
                    intent.putExtra(ProcessingActivity.EDIT_DATA, result.resultId)
                    activity.startActivity(intent)
                }

                img_edit.setOnLongClickListener {
                    val bottomSheetDialog =
                        BottomSheetDialog(
                            result
                        )
                    bottomSheetDialog.show(fm, bottomSheetDialog.tag)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    class BottomSheetDialog(private val result: Result) : BottomSheetDialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.home_bottom_sheet, container, false)

            val myPhotoViewModel =
                MyPhotoViewModel(requireActivity().application)

            v.bottom_edit.setOnClickListener {
                val intent = Intent(context, ProcessingActivity::class.java)
                requireActivity().startActivity(intent)
                dismiss()
            }
            v.bottom_delete.setOnClickListener {
                myPhotoViewModel.deleteImageResult(result)
                dismiss()
            }

            return v
        }
    }
}