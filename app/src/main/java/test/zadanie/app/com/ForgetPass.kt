package test.zadanie.app.com


import android.os.Bundle
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

class ForgetPass : AppCompatActivity() {

    private lateinit var db: AppDatabase

    private lateinit var editTextUsername: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pass)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-db").build()

        editTextUsername = findViewById(R.id.editTextUsername)


        val loginButton = findViewById<Button>(R.id.confirm)
        loginButton.setOnClickListener {
            restorePassword()
        }

    }

    private fun restorePassword() {
        val username = editTextUsername.text.toString()

        if (username.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                val user = db.userDao().getUserByUsername(username)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        val password = user.password
                        showPasswordDialog(password)
                    } else {
                        showUserNotFoundDialog()
                    }
                }
            }
        } else {
            showFieldsErrorDialog()
        }
    }

    private fun showPasswordDialog(password: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Password Recovery")
        alertDialogBuilder.setMessage("Your password: $password")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showUserNotFoundDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("User not found")
        alertDialogBuilder.setMessage("A user with this username does not exist.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showFieldsErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Restore error")
        alertDialogBuilder.setMessage("Please fill in all the fields.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }




}