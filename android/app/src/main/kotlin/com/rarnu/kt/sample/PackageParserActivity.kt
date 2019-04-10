package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import com.rarnu.kt.android.showActionBack

class PackageParserActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packageparser)
        showActionBack()
        actionBar?.title = "PackageParser"


    }

}