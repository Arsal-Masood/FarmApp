package com.example.farmapp.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.farmapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var name: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerRedirect: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEdit = findViewById(R.id.emailEdt)
        passwordEdit = findViewById(R.id.passEdt)
        loginBtn = findViewById(R.id.loginBtn)
        registerRedirect = findViewById(R.id.registerbtn)
        name=findViewById(R.id.nameEt)
        // Check if already logged in
        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val username = prefs.getString("username", null)
        if (username != null) {
            // User already logged in, directly go to community
            startActivity(Intent(this, community::class.java))
            finish()
        }

        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val name = name.text.toString().trim()
            val password = passwordEdit.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putString("username", name)
                            editor.apply()

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, community::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        registerRedirect.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}
