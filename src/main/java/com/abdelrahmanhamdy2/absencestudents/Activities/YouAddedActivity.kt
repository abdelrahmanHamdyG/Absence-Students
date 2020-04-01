package com.abdelrahmanhamdy2.absencestudents.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.getTime
import com.abdelrahmanhamdy2.absencestudents.R
import kotlinx.android.synthetic.main.activity_you_added.*

class YouAddedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_added)


        textAddedActivity.text="You are added at ${getTime()}"


    }
}
