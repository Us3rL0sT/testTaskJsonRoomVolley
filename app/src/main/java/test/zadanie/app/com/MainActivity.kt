package test.zadanie.app.com

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException





class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val URL = "https://dat003.ru/clock.php?id=8hd6mmcpjgmxv7uk9dab"

    private lateinit var sharedPreferences: SharedPreferences
    private var lastLoadedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternetConnection()





        sharedPreferences = getSharedPreferences("WebViewPreferences", Context.MODE_PRIVATE)
        lastLoadedUrl = sharedPreferences.getString("lastLoadedUrl", "")

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, URL, null,
            { response ->
                var url = response.getString("url")

                try {
                    if (url == "null") {
                        showSecondAnswerScreen()

                    } else {
                        openWebView(url)
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Error: " + e.message)
                }
            }
        ) { error -> Log.e(TAG, "Volley Error: $error") }
        queue.add(jsonObjectRequest)


    }

    private fun checkInternetConnection() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo == null || !networkInfo.isConnected) {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage("Please, check your Internet connection " +
                "status and restart app")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->

            dialog.dismiss()
            finishAffinity()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun openWebView(url: String) {
        val webViewIntent = Intent(this, WebViewActivity::class.java)
        webViewIntent.putExtra("url", url)
        startActivity(webViewIntent)
    }

    private fun showSecondAnswerScreen() {
        val secondAnswerIntent = Intent(this, SecondAnswerActivity::class.java)
        startActivity(secondAnswerIntent)
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putString("lastLoadedUrl", lastLoadedUrl)
        editor.apply()
    }
}

