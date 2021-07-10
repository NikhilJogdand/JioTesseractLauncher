package com.nik.mytesseractlauncher

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nik.mylibrarysdk.Singleton
import com.nik.mytesseractlauncher.databinding.ActivityMainBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var installedReceiver: BroadcastReceiver
    private lateinit var adapter: Adapter
    private lateinit var resolvedAppList: List<ResolveInfo>
    private lateinit var mainBinding: ActivityMainBinding
    private val appList = ArrayList<AppBlock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        setUpLauncher()
    }

    private fun setUpLauncher() {
        getAllAppsData(appList)
        mainBinding.appList.layoutManager =
            LinearLayoutManager(this@MainActivity)
        adapter = Adapter(this).also {
            it.passAppList(appList.sortedWith(
                Comparator<AppBlock> { o1, o2 ->
                    o1?.appName?.compareTo(o2?.appName ?: "", true) ?: 0;
                }
            ))
        }
        mainBinding.appList.adapter = adapter
        mainBinding.editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
    }

    /**
     * get List of apps containing following information -
     * App name, Package name, Icon, Main Activity class name, Version code, and Version name.
     */
    private fun getAllAppsData(appList: java.util.ArrayList<AppBlock>) {
        resolvedAppList = Singleton.getAllInstalledApps(packageManager)
        for (ri in resolvedAppList) {
            if (ri.activityInfo.packageName != this.packageName) {
                val app = AppBlock(
                    ri.loadLabel(packageManager).toString(),
                    ri.activityInfo.loadIcon(packageManager),
                    ri.activityInfo.packageName,
                    ri.activityInfo.name.toString(),
                    Singleton.getPackageVersionName(packageManager, ri.activityInfo.packageName),
                    Singleton.getPackageVersionCode(packageManager, ri.activityInfo.packageName)
                )
                appList.add(app)
            }
        }
    }

    private fun filter(text: String?) {
        if (::adapter.isInitialized) {
            text?.let {
                val temp: MutableList<AppBlock> = ArrayList()
                for (d in appList) {
                    if (d.appName.contains(it, true)) {
                        temp.add(d)
                    }
                }
                //update recyclerview
                adapter.passAppList(temp)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        installedReceiver = MyInstalledReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addDataScheme("package")
        this.registerReceiver(installedReceiver, filter)
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(packageName :String) {
        appList.clear()
        setUpLauncher()
    }

    override fun onDestroy() {
        if (::installedReceiver.isInitialized) {
            unregisterReceiver(installedReceiver)
        }
        super.onDestroy()
    }
}
