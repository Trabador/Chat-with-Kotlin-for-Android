package com.alexis.messengermock

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fbAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")
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
                        if(!it.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        Log.d("Main", "User created: ${it.result.user.uid}")
                        saveUserToFireBase(email)
                        Toast.makeText(applicationContext, "User Created", Toast.LENGTH_SHORT).show()
                        emailField.setText("")
                        passField.setText("")
                    }.addOnFailureListener {
                        Log.d("Main", "Error creating user: ${it.message}")
                        Toast.makeText(applicationContext, "Error with email: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun saveUserToFireBase(email: String){
        val uid = fbAuth.uid
        if(uid != null) {
            val refToNewUser = dbRef.child(uid)
            val newUser = User(email, "")
            refToNewUser.setValue(newUser)
                    .addOnSuccessListener {
                        Log.d("Main", "User saved into database")
                    }.addOnFailureListener {
                        Log.d("Main", "Error saving user : ${it.message}")
                    }
        }else{
            Log.d("Main", "UID NULL")
        }
    }
}
