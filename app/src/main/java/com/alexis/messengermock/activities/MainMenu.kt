package com.alexis.messengermock.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.alexis.messengermock.R
import com.google.firebase.auth.FirebaseAuth

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyUserIsLogged()

        setContentView(R.layout.activity_mainmenu)
    }

    private fun verifyUserIsLogged() {
        if(FirebaseAuth.getInstance().uid == null){
            val intentToRegister = Intent(this, Register::class.java)
            intentToRegister.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentToRegister)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.nav_new_message -> {
                val intentToNewMessage = Intent(this, NewMessage::class.java)
                startActivity(intentToNewMessage)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                verifyUserIsLogged()
            }
            R.id.nav_user_settings ->{
                val intentToUserSettings = Intent(this, UserSettings::class.java)
                startActivity(intentToUserSettings)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}