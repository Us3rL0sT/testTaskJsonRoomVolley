package test.zadanie.app.com


import android.content.Intent
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
import test.zadanie.app.com.entity.UserEntity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-db").build()
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)

        val registerButton = findViewById<Button>(R.id.signUp)
        registerButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        // Получаем данные введенные пользователем (лучше всего через EditText)
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        if (username.isNotBlank() && password.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                val existingUser = db.userDao().getUserByUsername(username)

                if (existingUser == null) {
                    val user = UserEntity(username = username, password = password)

                    db.userDao().insertUser(user)

                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@RegistrationActivity, User::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("password", password)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        showRegistrationLoginErrorDialog()
                    }
                }
            }


        } else {
            showRegistrationErrorDialog()
        }
    }

    private fun showRegistrationErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Registration error")
        alertDialogBuilder.setMessage("Please fill in all the fields.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showRegistrationLoginErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Registration error")
        alertDialogBuilder.setMessage("The login is already taken.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
