package com.rarnu.kt.sample

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.rarnu.kt.android.*
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity: Activity(), View.OnClickListener {


    private lateinit var originPic: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        showActionBack()
        actionBar?.title = "Image"
        // image
        originPic = assetsBitmap("sample.png")
        img.setImageBitmap(originPic)

        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        val t = v.tag.toString().toInt()
        when(t) {
            0 -> img.setImageBitmap(originPic.roundCorner(25F))
            1 -> img.setImageBitmap(originPic.blackWhite())
            2 -> img.setImageBitmap(originPic.rotate(90F))
            3 -> img.setImageBitmap(originPic.rotate(180F))
            4 -> img.setImageBitmap(originPic.flip(FlipMode.LEFTRIGHT))
            5 -> img.setImageBitmap(originPic.flip(FlipMode.UPDOWN))
            6 -> img.setImageBitmap(originPic.blur(9))
            7 -> img.setImageBitmap(originPic)
        }

    }
}