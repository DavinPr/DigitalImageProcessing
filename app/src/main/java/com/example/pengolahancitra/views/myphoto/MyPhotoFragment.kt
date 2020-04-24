package com.example.pengolahancitra.views.myphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pengolahancitra.R
import com.example.pengolahancitra.helper.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_photo.view.*

/**
 * A simple [Fragment] subclass.
 */
class MyPhotoFragment : Fragment() {

    private lateinit var myPhotoViewModel : MyPhotoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_photo, container, false)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        myPhotoViewModel = ViewModelProvider(this, factory)[MyPhotoViewModel::class.java]

        val fm : FragmentManager = requireActivity().supportFragmentManager
        val adapter = MyPhotoAdapter(requireActivity(), fm)

        view.rv_myphoto.layoutManager = GridLayoutManager(requireContext(), 3)
        view.rv_myphoto.setHasFixedSize(true)
        view.rv_myphoto.adapter = adapter

        myPhotoViewModel.getAllDataResult().observe(viewLifecycleOwner, Observer {result ->
            if (result != null){
                adapter.setList(result)
            }
        })

        return view
    }

}
