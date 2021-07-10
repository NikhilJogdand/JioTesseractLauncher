package com.nik.mytesseractlauncher

import android.graphics.drawable.Drawable

data class AppBlock (
    val appName:String,
    val icon: Drawable,
    val packageName:String,
    val activityName:String,
    val versionCode:String,
    val versionName:String,

)