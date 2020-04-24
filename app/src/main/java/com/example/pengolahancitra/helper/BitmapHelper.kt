package com.example.pengolahancitra.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.lang.Exception

class BitmapHelper(private val context: Context) {
    fun bitmapToString(bitmap: Bitmap): String? {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            Base64.encodeToString(b, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("String:", e.message!!)
            null
        }

    }

    fun uriToBitmap(uri: Uri): Bitmap? {
        val bitmap =
            if (Build.VERSION.SDK_INT >= 29) {
                // Mengatasi getBitmap Deperecated di SDK 29 keatas
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                )
            } else {
                // SDK < 29
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            }
        return reduceBitmapSize(bitmap)
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        val options = BitmapFactory.Options()
        options.inMutable = true
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)
    }

    //Memperkecil ukuran bitmap
    private fun reduceBitmapSize(bitmap: Bitmap): Bitmap? {
        return BitmapFactory.Options().run {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val b = baos.toByteArray()
            inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(b, 0, b.size, this)

            inSampleSize = calculateInSampleSize(this)
            inJustDecodeBounds = false

            BitmapFactory.decodeByteArray(b, 0, b.size, this)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > 400 || width > 400) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= 400 && halfWidth / inSampleSize >= 400) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}