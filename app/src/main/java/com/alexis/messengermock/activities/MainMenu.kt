package com.alexis.messengermock.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import com.alexis.messengermock.R
import com.alexis.messengermock.dataclasses.Message
import com.alexis.messengermock.dataclasses.User
import com.alexis.messengermock.misc.CustomProgressDialog
import com.alexis.messengermock.viewitems.LatestMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenu : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessageList : HashMap<String, Message> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyUserIsLogged()
        setContentView(R.layout.activity_mainmenu)
        adapter.setOnItemClickListener { item, view ->
            val latestItem = item as LatestMessageItem
            val intentToChatScreen = Intent(this, ChatScreen::class.java)
            intentToChatScreen.putExtra(NewMessage.USER_KEY,latestItem.userReceivedMessageFrom)
            startActivity(intentToChatScreen)
        }
        latestMessagesList.adapter = adapter
        latestMessagesList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun listenForLatestMessages(){
        val uid = FirebaseAuth.getInstance().uid
        val dbRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$uid")

        dbRef.addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                refreshLatestMessageList(p0)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                refreshLatestMessageList(p0)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun refreshLatestMessageList(p0: DataSnapshot){
        val data = p0.getValue(Message::class.java)
        latestMessageList[p0.key!!] = data!!
        adapter.clear()
        latestMessageList.values.forEach {
            adapter.add(LatestMessageItem(it))
        }
    }

    private fun verifyUserIsLogged() {
        if(FirebaseAuth.getInstance().uid == null){
            val intentToRegister = Intent(this, Register::class.java)
            intentToRegister.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentToRegister)
        }
        else{
            listenForLatestMessages()
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
            R.id.nav_user_settings -> {
                val intentToUserSettings = Intent(this, UserSettings::class.java)
                startActivity(intentToUserSettings)
            }
            R.id.nav_add_friend -> {
                val intentToAddFriends = Intent(this, AddFriends::class.java)
                startActivity(intentToAddFriends)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}