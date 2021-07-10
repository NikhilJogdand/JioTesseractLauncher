package com.nik.mytesseractlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import org.greenrobot.eventbus.EventBus




class MyInstalledReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_PACKAGE_ADDED)) {        // install
            val packageName1: String? =
                intent.data?.encodedSchemeSpecificPart
            Toast.makeText(context, "App INSTALL\n: $packageName1", Toast.LENGTH_SHORT)
                .show()
            EventBus.getDefault().post(packageName1)
        }
        if (intent.action.equals(Intent.ACTION_PACKAGE_REMOVED)) {    // uninstall
            val packageName1: String? =
                intent.data?.encodedSchemeSpecificPart
            Toast.makeText(context, "App UNINSTALL\n: $packageName1", Toast.LENGTH_SHORT)
                .show()
            EventBus.getDefault().post(packageName1)
        }
    }
}