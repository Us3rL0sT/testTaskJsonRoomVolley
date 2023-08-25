package test.zadanie.app.com

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.onesignal.OneSignal
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class Metrics : Application() {
    private val ONESIGNAL_APP_ID = "7b2c527c-65f0-4a9d-bc8d-526f24a99d4e"


    override fun onCreate() {
        super.onCreate()

        val config = YandexMetricaConfig.newConfigBuilder("b9d8e2d2-efdc-4c7b-8cac-3668fa0ff402").build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        checkNotificationPermissions()
    }


    private fun checkNotificationPermissions() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {

            val notificationPermissionIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            notificationPermissionIntent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            notificationPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(notificationPermissionIntent)
        }
    }


}
