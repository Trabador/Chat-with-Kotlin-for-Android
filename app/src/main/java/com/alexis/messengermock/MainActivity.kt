package com.alexis.messengermock

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fbAuth = FirebaseAuth.getInstance()
        registerButton.setOnClickListener { registerNewUser() }
        alreadyHaveAccountText.setOnClickListener { goToLogin() }

    }

    private fun goToLogin(){
        val intentToLogin = Intent(this, Login::class.java)
        startActivity(intentToLogin)
    }

    private fun registerNewUser(){
        val email = emailField.text.toString()
        val password = passField.text.toString()
        Log.d("Main","$email , $password ")

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(applicationContext, "Please fill all fields as required", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful)
                            return@addOnCompleteListener
                        Log.d("Main", "User created: ${it.result.user.uid}")
                        Toast.makeText(applicationContext, "User Created", Toast.LENGTH_SHORT).show()
                        emailField.setText("")
                        passField.setText("")
                    }.addOnFailureListener {
                        Log.d("main", "Error creating user: ${it.message}")
                        Toast.makeText(applicationContext, "Error with email: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
        }
    }
}
