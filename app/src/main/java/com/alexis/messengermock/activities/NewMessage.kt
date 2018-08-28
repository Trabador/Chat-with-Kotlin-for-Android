package com.alexis.messengermock.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.alexis.messengermock.dataclasses.User
import com.alexis.messengermock.R
import com.alexis.messengermock.viewitems.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.abc_screen_toolbar.*
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Friend List"

        fetchUsersFromFireBase()
        usersList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    private fun fetchUsersFromFireBase(){
        val dbRef = FirebaseDatabase.getInstance().getReference("/users")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                //not used
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                val uid = FirebaseAuth.getInstance().uid
                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        if(user.uid != uid){
                            adapter.add(UserItem(user))
                        }
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intentToChatScreen = Intent(view.context, ChatScreen::class.java)
                    intentToChatScreen.putExtra(USER_KEY,userItem.user)
                    startActivity(intentToChatScreen)

                    finish()
                }
                usersList.adapter = adapter
            }

        })
    }
}
