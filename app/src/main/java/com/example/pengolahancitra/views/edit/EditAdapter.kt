package com.example.pengolahancitra.views.edit

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
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.helper.BitmapHelper
import com.example.pengolahancitra.helper.EditDiffCallback
import com.example.pengolahancitra.views.processing.ProcessingActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.home_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_edit.view.*

class EditAdapter internal constructor(
    private val activity: Activity,
    private val fm: FragmentManager
) : RecyclerView.Adapter<EditAdapter.EditViewHolder>() {

    private val listImageEditable = ArrayList<Edit>()

    fun setList(listImageEditable: List<Edit>) {
        val diffCallback = EditDiffCallback(this.listImageEditable, listImageEditable)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listImageEditable.clear()
        this.listImageEditable.addAll(listImageEditable)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit, parent, false)
        return EditViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listImageEditable.size
    }

    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        holder.bind(listImageEditable[position])
    }

    inner class EditViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val bitmapHelper = BitmapHelper(activity.applicationContext)
        fun bind(edit: Edit) {
            with(itemView) {
                img_edit.clipToOutline = true
                img_edit.setImageBitmap(bitmapHelper.stringToBitmap(edit.image!!))
                img_edit.setOnClickListener {
                    val intent = Intent(activity, ProcessingActivity::class.java)
                    intent.putExtra(ProcessingActivity.ACTIVITY_CODE, 1)
                    intent.putExtra(ProcessingActivity.EDIT_DATA, edit.id)
                    activity.startActivity(intent)
                }

                img_edit.setOnLongClickListener {
                    val bottomSheetDialog = BottomSheetDialog(edit)
                    bottomSheetDialog.show(fm, bottomSheetDialog.tag)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    class BottomSheetDialog(private val edit: Edit) : BottomSheetDialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.home_bottom_sheet, container, false)

            val editViewModel = EditViewModel(requireActivity().application)

            v.bottom_edit.setOnClickListener {
                val intent = Intent(context, ProcessingActivity::class.java)
                requireActivity().startActivity(intent)
                dismiss()
            }
            v.bottom_delete.setOnClickListener {
                editViewModel.deleteImageEditable(edit)
                dismiss()
            }

            return v
        }
    }
}