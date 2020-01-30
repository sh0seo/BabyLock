package io.animal.monkey.util

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log

class PermissionHelper : ContextWrapper {

    val TAG = "PermissionHelper"

    constructor(c: Context) : super(c) {
    }

    fun hasSystemAlertWindowsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
           return true
        }
        if (!Settings.canDrawOverlays(baseContext)) {
            return false
        }
        return true
    }

    fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: SettingNotFoundException) { //accessibility is Enable
            Log.i(TAG, e.message)
        }
        if (accessibilityEnabled == 1) {
            val services = Settings.Secure.getString(this.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (services != null) {
                return services.toLowerCase().contains(this.packageName.toLowerCase())
            }
        }
        return false
    }
}