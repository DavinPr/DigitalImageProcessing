package com.example.pengolahancitra.views.processing

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pengolahancitra.R
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.database.entity.Result
import com.example.pengolahancitra.helper.ViewModelFactory
import kotlinx.android.synthetic.main.activity_processing.*
import kotlinx.android.synthetic.main.processing_options.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class ProcessingActivity : AppCompatActivity(), DiscreteSeekBar.OnProgressChangeListener,
    View.OnClickListener {

    companion object {
        const val EDIT_DATA = "edit_data"
        const val ACTIVITY_CODE = "activity_code"
    }

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    private lateinit var processingViewModel: ProcessingViewModel
    private lateinit var result: Result
    private lateinit var edit: Edit
    private lateinit var rotateForward : Animation
    private lateinit var rotateBackward : Animation
    private var activityCode = 0
    private var itemId: Int? = null
    private var adjustValue: Int = 0
    private var kernel: Int = 0
    private var currentAdjustValue: Int = 0
    private var storedEncodedString: String? = null
    private var isOpen = false
    private var optionSelected = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        Akhir Hide Status Bar

        setContentView(R.layout.activity_processing)

//        Animation
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotation_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotation_backward)

        // Toolbar Konfigurasi
        val toolbar = findViewById<Toolbar>(R.id.processing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.icon_close)
        // Akhir Toolbar Konfigurasi

        itemId = intent.getIntExtra(EDIT_DATA, 0)
        activityCode = intent.getIntExtra(ACTIVITY_CODE, 0)

        val factory = ViewModelFactory.getInstance(this.application)
        processingViewModel = ViewModelProvider(this, factory)[ProcessingViewModel::class.java]

//        Load Image
        when (activityCode) {
            1 -> {
                processingViewModel.getDataEdit(itemId!!).observe(this, Observer { image ->
                    this.storedEncodedString = image.image
                    itemId?.let { loadImage(image.image!!, 0, 0) }
                    edit = image
                })
                result = Result()
            }
            2 -> {
                processingViewModel.getDataResult(itemId!!).observe(this, Observer { image ->
                    if (image != null) {
                        this.currentAdjustValue = image.resultAdjustment!!
                        this.storedEncodedString = image.resultImage
                        itemId?.let {
                            loadImage(
                                image.resultImage!!,
                                0 + image.resultAdjustment!!,
                                image.filterKernel!!
                            )
                        }
                        seekbar_processing.progress = image.resultAdjustment!!
                        result = image
                    }
                })
            }
        }
//        Akhir Load Image

        when (kernel) {
            1 -> {
                fab_3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.bluey_grey)
                tv_fab3.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
            }
            2 -> {
                fab_5.backgroundTintList = ContextCompat.getColorStateList(this, R.color.bluey_grey)
                tv_fab5.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
            }
            3 -> {
                fab_7.backgroundTintList = ContextCompat.getColorStateList(this, R.color.bluey_grey)
                tv_fab7.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
            }
        }

        fab_menu.setOnClickListener(this)
        fab_brightness.setOnClickListener(this)
        fab_blur.setOnClickListener(this)
        fab_3.setOnClickListener(this)
        fab_5.setOnClickListener(this)
        fab_7.setOnClickListener(this)

        seekbar_processing.setOnProgressChangeListener(this)
    }

    //    Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.actionbar_processing_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                val newBitmap = (img_process.drawable as BitmapDrawable).bitmap
                result.filteredImage = processingViewModel.bitmapToString(newBitmap)
                result.resultImage = storedEncodedString
                result.resultAdjustment = adjustValue
                if (activityCode == 1) {
                    processingViewModel.insertData(result)
                } else if (activityCode == 2) {
                    processingViewModel.updateData(result)
                }
                finish()
            }
            R.id.reset_menu -> {
                if (activityCode == 1) {
                    kernel = 0
                    seekbar_processing.progress = 0
                    loadImage(edit.image!!, seekbar_processing.progress, kernel)
                } else if (activityCode == 2) {
                    kernel = 0
                    seekbar_processing.progress = 0
                    loadImage(result.resultImage!!, seekbar_processing.progress, kernel)
                }
            }

            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    Fungsi Load Image
    private fun loadImage(string: String, adjustValue: Int, kernel: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            val originalBitmap = processingViewModel.stringToBitmap(string)
            when (optionSelected) {
                1 -> {
                    val filteredBitmap = originalBitmap?.let {
                        processingViewModel.getBrightnessAsync(
                            it,
                            adjustValue
                        ).await()
                    }
                    img_process.setImageBitmap(filteredBitmap)
                }
                2 -> {
                    val filteredBitmap = originalBitmap?.let {
                        processingViewModel.getBlurAsync(
                            it,
                            kernel
                        ).await()
                    }
                    img_process.setImageBitmap(filteredBitmap)
                }
                else -> {
                    when (activityCode) {
                        1 -> img_process.setImageBitmap(originalBitmap)
                        2 -> {
                            processingViewModel.getDataResult(itemId!!)
                                .observe(this@ProcessingActivity, Observer { image ->
                                    val oldFilteredBitmap = image.filteredImage
                                    img_process.setImageBitmap(oldFilteredBitmap?.let {
                                        processingViewModel.stringToBitmap(
                                            it
                                        )
                                    })
                                })
                        }
                    }
                }
            }
        }
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        if (seekBar != null) {
            adjustValue = seekBar.progress
            loadImage(storedEncodedString!!, seekBar.progress, this.kernel)
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {

    }

    private fun closeFab() {
        fab_menu.startAnimation(rotateBackward)
        when (optionSelected) {
            1 -> {
                seekbar_processing.visibility = View.GONE
            }
            2 -> {
                container_blur.visibility = View.GONE
            }
            else -> {
                container_operation.visibility = View.GONE
            }
        }
        fab_brightness.isClickable = false
        fab_blur.isClickable = false
        fab_3.isClickable = false
        fab_5.isClickable = false
        fab_7.isClickable = false
        optionSelected = 0
        isOpen = false
        filter_title.text = null
    }

    private fun openFab() {
        container_operation.visibility = View.VISIBLE
        fab_brightness.isClickable = true
        fab_blur.isClickable = true
        fab_3.isClickable = false
        fab_5.isClickable = false
        fab_7.isClickable = false
        isOpen = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_menu -> {
                if (isOpen) {
                    closeFab()
                } else {
                    fab_menu.startAnimation(rotateForward)
                    openFab()
                }
            }
            R.id.fab_brightness -> {
                container_operation.visibility = View.GONE
                seekbar_processing.visibility = View.VISIBLE
                fab_brightness.isClickable = false
                fab_blur.isClickable = false
                optionSelected = 1
                filter_title.text = getString(R.string.brightness)
            }
            R.id.fab_blur -> {
                container_operation.visibility = View.GONE
                container_blur.visibility = View.VISIBLE
                fab_brightness.isClickable = false
                fab_blur.isClickable = false
                fab_3.isClickable = true
                fab_5.isClickable = true
                fab_7.isClickable = true
                optionSelected = 2
                filter_title.text = getString(R.string.blur_effects)
            }
            R.id.fab_3 -> {
                kernel = 1
                loadImage(storedEncodedString!!, adjustValue, kernel)
                tv_fab3.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
                tv_fab5.setTextColor(ContextCompat.getColor(this, R.color.white))
                tv_fab7.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            R.id.fab_5 -> {
                kernel = 2
                loadImage(storedEncodedString!!, adjustValue, kernel)
                tv_fab5.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
                tv_fab3.setTextColor(ContextCompat.getColor(this, R.color.white))
                tv_fab7.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            R.id.fab_7 -> {
                kernel = 3
                loadImage(storedEncodedString!!, adjustValue, kernel)
                tv_fab3.setTextColor(ContextCompat.getColor(this, R.color.white))
                tv_fab5.setTextColor(ContextCompat.getColor(this, R.color.white))
                tv_fab7.setTextColor(ContextCompat.getColor(this, R.color.bluey_grey))
            }
        }
    }
}
