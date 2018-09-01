package com.alexis.messengermock.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.alexis.messengermock.R
import com.alexis.messengermock.misc.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var fbAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fbAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener { loginUser() }

    }

    private fun loginUser(){
        val email = emailFieldLogin.text.toString()
        val password = passFieldLogin.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(applicationContext, "Please fill all fields as required", Toast.LENGTH_SHORT).show()
            return
        }
        val message = "Logging in ... "
        val dialog = CustomProgressDialog.createDialog(this, message)
        dialog.show()
        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful){
                        return@addOnCompleteListener
                    }
                    Log.d("login", "Success Login ")
                    dialog.dismiss()
                    val intentToMainMenu = Intent(this, MainMenu::class.java)
                    intentToMainMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intentToMainMenu)
                }.addOnFailureListener {
                    dialog.dismiss()
                    Log.d("login", "Fail to login: ${it.message}")
                    Toast.makeText(applicationContext, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

}