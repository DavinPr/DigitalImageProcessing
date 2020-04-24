package com.example.pengolahancitra.views.edit

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pengolahancitra.R
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.helper.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel
    private lateinit var adapter: EditAdapter

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    private val PERMISSION = 1001
    private val PICK = 1002

    private var edit: Edit? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        ImageViewCompat.setImageTintList(view.fab_add, ColorStateList.valueOf(Color.WHITE))
        edit = Edit()
        val fm : FragmentManager = requireActivity().supportFragmentManager
        adapter = EditAdapter(requireActivity(), fm)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        editViewModel = ViewModelProvider(this, factory)[EditViewModel::class.java]
        editViewModel.getAllImageEditable().observe(viewLifecycleOwner, Observer { edit ->
            if (edit != null){
                adapter.setList(edit)
            }
        })


        view.rv_edit.layoutManager = GridLayoutManager(view.context, 3)
        view.rv_edit.setHasFixedSize(true)
        view.rv_edit.adapter = adapter

        view.fab_add.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        view.context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                    requestPermissions(permission, PERMISSION)
                } else {
                    selectPicture()
                }
            } else {
                selectPicture()
            }
        }

        return view
    }

    //    Inisiasi Pengambilan Gambar
    private fun selectPicture() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPicture()
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK) {
            if (data != null) {
                coroutineScope.launch(Dispatchers.Main) {
                    val bitmap = editViewModel.getBitmapAsync(data.data!!).await()
                    edit!!.image = editViewModel.bitmapToString(bitmap)
                    editViewModel.insertImageEditable(edit as Edit)
                }
            }
        }
    }

}
