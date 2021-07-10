package com.nik.mylibrarysdk

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build

class Singleton {

    companion object {
        @SuppressLint("QueryPermissionsNeeded")
        fun getAllInstalledApps (packageManager: PackageManager): MutableList<ResolveInfo> {
            return packageManager
                .queryIntentActivities(
                    Intent(Intent.ACTION_MAIN,null)
                    .addCategory(Intent.CATEGORY_LAUNCHER),0)
        }

        fun getPackageVersionName(packageManager: PackageManager, packageName: String): String {
            val packageInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        }

        fun getPackageVersionCode(packageManager: PackageManager, packageName: String): String {
            val packageInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        }
    }
}