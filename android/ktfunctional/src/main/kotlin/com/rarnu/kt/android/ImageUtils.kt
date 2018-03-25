package com.rarnu.kt.android

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File
import java.io.FileOutputStream

fun getBitmapFromAssets(ctx: Context, path: String) = BitmapFactory.decodeStream(ctx.resources.assets.open(path))

fun Bitmap.toRoundCorner(radis: Float): Bitmap {
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, width, height)
    val rectF = RectF(rect)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = Color.WHITE
    canvas.drawRoundRect(rectF, radis, radis, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
}

fun Bitmap.toBlackWhite(): Bitmap = colorMatrix(floatArrayOf(
        0.308f, 0.609f, 0.082f, 0.0f, 0.0f,
        0.308f, 0.609f, 0.082f, 0.0f, 0.0f,
        0.308f, 0.609f, 0.082f, 0.0f, 0.0f,
        0.000f, 0.000f, 0.000f, 1.0f, 0.0f
))


fun Bitmap.rotate(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

enum class FlipMode { LEFTRIGHT, UPDOWN }

fun Bitmap.flip(mode: FlipMode = FlipMode.LEFTRIGHT): Bitmap {
    val matrix = Matrix()
    val temp = Matrix()
    temp.setValues(floatArrayOf(-1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f))
    matrix.postConcat(temp)
    if (mode == FlipMode.UPDOWN) {
        matrix.setRotate(180.0f, width * 1.0f / 2, height * 1.0f / 2)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}


fun Bitmap.colorMatrix(matrixSrc: FloatArray): Bitmap {
    val matrix = ColorMatrix()
    matrix.set(matrixSrc)
    val filter = ColorMatrixColorFilter(matrix)
    val p = Paint()
    p.colorFilter = filter
    val colorBmp = Bitmap.createBitmap(width, height, this.config)
    val c = Canvas(colorBmp)
    c.drawBitmap(this, 0.0f, 0.0f, p)
    return colorBmp
}

fun Bitmap.scale(newWidth: Float, newHeight: Float): Bitmap {
    val matrix = Matrix()
    val scaleWidth = newWidth * 1.0f / width
    val scaleHeight = newHeight * 1.0f / height
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.blur(level: Int): Bitmap {
    val pixels = IntArray(width * height)
    val pixelsRawSource = IntArray(width * height * 3)
    val pixelsRawNew = IntArray(width * height * 3)
    getPixels(pixels, 0, width, 0, 0, width, height)
    for (k in 1..level) {
        for (i in 0 until pixels.size) {
            pixelsRawSource[i * 3 + 0] = Color.red(pixels[i])
            pixelsRawSource[i * 3 + 1] = Color.green(pixels[i])
            pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i])
        }
        var currentPixel = width * 3 + 3
        for (i in 0 until height - 3) {
            for (j in 0 until width * 3) {
                currentPixel += 1
                val sumColor = pixelsRawSource[currentPixel - width * 3] + pixelsRawSource[currentPixel - 3] + pixelsRawSource[currentPixel + 3] + pixelsRawSource[currentPixel + width * 3]
                pixelsRawNew[currentPixel] = Math.round(sumColor * 1.0f / 4)
            }
        }
        for (i in 0 until pixels.size) {
            pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0], pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2])
        }
    }
    val bmpReturn = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bmpReturn.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmpReturn
}

fun Bitmap.reflect(refHeight: Float): Bitmap {
    val reflectionHeight = if (refHeight == 0.0f) height / 3 else (refHeight * height).toInt()
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    val reflectionBitmap = Bitmap.createBitmap(this, 0, height - reflectionHeight, width, reflectionHeight, matrix, false)
    val canvas = Canvas(reflectionBitmap)
    val paint = Paint()
    paint.isAntiAlias = true
    val shader = LinearGradient(0.0f, 0.0f, 0.0f, reflectionBitmap.height.toFloat(), 0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR)
    paint.shader = shader
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    canvas.drawRect(0.0f, 0.0f, reflectionBitmap.width.toFloat(), reflectionBitmap.height.toFloat(), paint)
    return reflectionBitmap
}

fun Bitmap.saveToFile(filename: String, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG) {
    val f = File(filename)
    f.createNewFile()
    val fOut = FileOutputStream(f)
    compress(format, 100, fOut)
    fOut.flush()
    fOut.close()
}


fun Drawable.toBitmap(): Bitmap {
    val config = if (opacity != PixelFormat.OPAQUE) {
        Bitmap.Config.ARGB_8888
    } else {
        Bitmap.Config.RGB_565
    }
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}

fun Drawable.scale(newWidth: Float, newHeight: Float): Drawable {
    val matrix = Matrix()
    val sx = newWidth / intrinsicWidth
    val sy = newHeight / intrinsicHeight
    matrix.postScale(sx, sy)
    @Suppress("DEPRECATION")
    return BitmapDrawable(Bitmap.createBitmap(toBitmap(), 0, 0, intrinsicWidth, intrinsicHeight, matrix, true))
}
