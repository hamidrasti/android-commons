package ir.hamraa.androidcommons.core

import android.app.Activity
import android.content.Intent
import android.net.Uri

object Linking {

    var initialUri: Uri? = null

    fun openURL(activity: Activity, url: String): Boolean {

        if (url.isEmpty()) {
            return false
        }

        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url).normalizeScheme())
            activity.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun openSettings(activity: Activity): Boolean {
        return try {
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.parse("package:${activity.packageName}")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            activity.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}