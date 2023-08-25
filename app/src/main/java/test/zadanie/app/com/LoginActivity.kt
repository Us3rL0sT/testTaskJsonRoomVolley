package test.zadanie.app.com


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import test.zadanie.app.com.bd.AppDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-db").build()
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)

        val loginButton = findViewById<Button>(R.id.signInLoginPage)
        loginButton.setOnClickListener {
            performLogin()
        }

    }

    private fun performLogin() {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        if (username.isBlank() || password.isBlank()) {
            showFieldsErrorDialog()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val user = db.userDao().getUser(username, password)

            withContext(Dispatchers.Main) {
                if (user != null) {
                    val intent = Intent(this@LoginActivity, User::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("password", password)
                    startActivity(intent)
                } else {
                    showAuthenticationErrorDialog()
                }
            }
        }
    }


    private fun showAuthenticationErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Authorization error")
        alertDialogBuilder.setMessage("Invalid username or password.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showFieldsErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Authorization error")
        alertDialogBuilder.setMessage("Please fill in all the fields.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}