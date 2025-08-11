package com.abcly.swolf.lib_util.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NYHomeKeyBroadcastReceiver : BroadcastReceiver() {

    private val SYSTEM_DIALOG_REASON_KEY = "reason"
    private val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"
    private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
    private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    private val SYSTEM_DIALOG_REASON_ASSIST = "assist"

    override fun onReceive(context: Context, intent: Intent) {
        //按下Home键会发送ACTION_CLOSE_SYSTEM_DIALOGS的广播
        if (intent.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
            if (reason != null) {
                if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                    // 短按home键
                    val newIntent = Intent(Intent.ACTION_MAIN)
                    newIntent.addCategory(Intent.CATEGORY_HOME)
                    newIntent.setClassName("android", "com.android.internal.app.ResolverActivity")
                    context.startActivity(newIntent)
//                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                    // RECENT_APPS键
//                    Toast.makeText(context, "RECENT_APPS", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}