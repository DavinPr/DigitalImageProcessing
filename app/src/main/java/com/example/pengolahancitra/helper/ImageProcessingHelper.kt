package com.example.pengolahancitra.helper

import android.graphics.Bitmap
import android.graphics.Color

class ImageProcessingHelper {

//    Brightness

    fun getBrightnessAdjusment(bitmap: Bitmap, adjustValue: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixel = IntArray(width * height)
        bitmap.getPixels(pixel, 0, width, 0, 0, width, height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val red = (pixel[index] shr 16) and 0xff
                val green = (pixel[index] shr 8) and 0xff
                val blue = pixel[index] and 0xff
                val newColor = Color.rgb(
                    valueState(red + adjustValue),
                    valueState(green + adjustValue),
                    valueState(blue + adjustValue)
                )
                pixel[index] = newColor
            }
        }
        bitmap.setPixels(pixel, 0, width, 0, 0, width, height)
        return bitmap
    }

    //    Blur
    fun getBlurEffect(bitmap: Bitmap, kernel: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixel = IntArray(width * height)
        bitmap.getPixels(pixel, 0, width, 0, 0, width, height)

        when (kernel) {
            1 -> {
                for (x in 1 until width - 1) {
                    for (y in 1 until height - 1) {
                        fun index(m: Int, n: Int): Int {
                            return m * width + n
                        }

                        fun average(rightShift: Int): Int {
                            var result = 0
                            for (i in -1 until 2) {
                                for (j in -1 until 2) {
                                    result += (pixel[index(x+i, y+j)] shr rightShift) and 0xff
                                }
                            }
                            return result / 9
                        }
                        val red = average(16)
                        val green = average(8)
                        val blue = average(0)
                        val newColor = Color.rgb(valueState(red), valueState(green), valueState(blue))
                        pixel[index(x, y)] = newColor
                    }
                }
            }
            2 -> {
                for (x in 2 until width - 2) {
                    for (y in 2 until height - 2) {
                        fun index(m: Int, n: Int): Int { return m * width + n }
                        fun average(rightShift: Int): Int {
                            var result = 0
                            for (i in -2 until 3) {
                                for (j in -2 until 3) {
                                    result += (pixel[index(x + i, y + j)] shr rightShift) and 0xff
                                }
                            }
                            return result / 25
                        }
                        val red = average(16)
                        val green = average(8)
                        val blue = average(0)
                        val newColor = Color.rgb(valueState(red), valueState(green), valueState(blue))
                        pixel[index(x, y)] = newColor
                    }
                }
            }
            3 -> {
                for (x in 3 until width - 3) {
                    for (y in 3 until height - 3) {
                        fun index(m: Int, n: Int): Int { return m * width + n }
                        fun average(rightShift: Int): Int {
                            var result = 0
                            for (i in -3 until 4) {
                                for (j in -3 until 4) {
                                    result += (pixel[index(x + i, y + j)] shr rightShift) and 0xff
                                }
                            }
                            return result / 49
                        }

                        val red = average(16)
                        val green = average(8)
                        val blue = average(0)

                        val newColor = Color.rgb(valueState(red), valueState(green), valueState(blue))
                        pixel[index(x, y)] = newColor
                    }
                }
            }
        }

        bitmap.setPixels(pixel, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun valueState(value: Int): Int {
        return when {
            value >= 256 -> {
                255
            }
            value < 0 -> {
                0
            }
            else -> {
                value
            }
        }
    }
}