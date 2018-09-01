package com.alexis.messengermock.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alexis.messengermock.dataclasses.User
import com.alexis.messengermock.R
import com.alexis.messengermock.misc.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class Register : AppCompatActivity() {

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private val defaultImageUrl: String = "https://firebasestorage.googleapis.com/v0/b/messengermock.appspot.com/o/images%2Fdefault-user.png?alt=media&token=4645e0ff-29f9-4971-825f-c72df19603ec"

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
            val message = "Please wait ... "
            val dialog = CustomProgressDialog.createDialog(this,message)
            dialog.show()
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
                        dialog.dismiss()
                        val intentToMainMenu = Intent(this, MainMenu::class.java)
                        intentToMainMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intentToMainMenu)
                    }.addOnFailureListener {
                        dialog.dismiss()
                        Log.d("Main", "Error creating user: ${it.message}")
                        Toast.makeText(applicationContext, "Error with email: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun saveUserToFireBase(email: String){
        val uid = fbAuth.uid
        if(uid != null) {
            val refToNewUser = dbRef.child(uid)
            val newUser = User(uid, email, defaultImageUrl)
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
