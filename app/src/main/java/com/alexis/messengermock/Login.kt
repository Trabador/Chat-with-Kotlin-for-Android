package com.alexis.messengermock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton.setOnClickListener { loginUser() }

    }

    private fun loginUser(){
        val email = emailFieldLogin.text.toString()
        val password = passFieldLogin.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(applicationContext, "Please fill all fields as required", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("login", "Se loggeo ")
    }

}