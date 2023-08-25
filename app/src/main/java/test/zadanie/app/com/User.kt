package test.zadanie.app.com


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class User : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user)

        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        val textViewUsername = findViewById<TextView>(R.id.login)
        val textViewPassword = findViewById<TextView>(R.id.password)

        textViewUsername.text = "Username: $username"
        textViewPassword.text = "Password: $password"


        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {

            val intent = Intent(this, SecondAnswerActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("password", password)
            startActivity(intent)
        }
    }
}