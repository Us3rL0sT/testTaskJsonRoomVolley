package test.zadanie.app.com

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class NotificationServiceExtension : OSRemoteNotificationReceivedHandler {

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun remoteNotificationReceived(context: Context?,
                                            notificationReceiverEvent: OSNotificationReceivedEvent) {

        lateinit var data: JSONObject
        var notification = notificationReceiverEvent.notification

        val mutableNotification = notification.mutableCopy()
        mutableNotification.setExtender{
            builder: NotificationCompat.Builder ->
            data = notification.additionalData
            val mBigPicture: String
            if (!TextUtils.isEmpty(data.optString("big_picture", null))) {
                mBigPicture = data.optString("big_picture", null)
                if (mBigPicture != null) {
                    try {
                        val url = URL(mBigPicture)
                        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        if (image != null) {
                            builder.setStyle(
                                NotificationCompat.BigPictureStyle().bigPicture(image)
                            )
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            builder

        }

        notificationReceiverEvent?.complete(mutableNotification)


    }


}