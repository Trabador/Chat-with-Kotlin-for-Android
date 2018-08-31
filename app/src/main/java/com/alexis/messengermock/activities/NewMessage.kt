package com.alexis.messengermock.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
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

    private val uid = FirebaseAuth.getInstance().uid
    private var friendUidList = mutableListOf<String>()
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Friend List"

        fetchFriendsFromDataBase()

        usersList.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            onClickedFriend(userItem, view)
            finish()
        }
        
        usersList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    private fun fetchFriendsFromDataBase(){
        val dbRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/friendList")
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null) {
                    friendUidList = p0.value as MutableList<String>
                }
                addListToUI()
            }
        })
    }

    private fun addListToUI(){
        friendUidList.forEach {
            val dbRef = FirebaseDatabase.getInstance().getReference("/users/$it")
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }
            })
        }
    }

    private fun onClickedFriend(userItem: UserItem, view: View){
        val intentToChatScreen = Intent(view.context, ChatScreen::class.java)
        intentToChatScreen.putExtra(USER_KEY,userItem.user)
        startActivity(intentToChatScreen)
    }

}
