package com.alexis.messengermock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_screen.*
import kotlinx.android.synthetic.main.message_sended_item_layout.*

class ChatScreen : AppCompatActivity() {

    lateinit var fbAuth: FirebaseAuth
    val adapter = GroupAdapter<ViewHolder>()
    lateinit var myUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)
        fbAuth = FirebaseAuth.getInstance()
        val userToChat = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        supportActionBar?.title = userToChat.email

        sendMessageButton.setOnClickListener{
            sendMessage(userToChat.uid)
        }
        getUserData()
        listenToMessages(userToChat)
        messageLogView.adapter = adapter

    }

    private fun getUserData(){
        val uid = fbAuth.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("/users").child(uid!!)
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                myUser = p0.getValue(User::class.java)!!
            }

        })
    }

    private fun sendMessage(receiverId: String){
        val senderId = FirebaseAuth.getInstance().uid
        val textMessage = messageText.text.toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("/messages").push()
        val id = dbRef.key
        if(receiverId != null && senderId != null && id != null) {
            val newMessageRecord = Message(id, senderId, receiverId, textMessage, System.currentTimeMillis().toString())
            dbRef.setValue(newMessageRecord)
                    .addOnSuccessListener {
                        Log.d("ChatScreen", "Message sended")
                        messageText.setText("")
                    }
        }

    }

    private fun listenToMessages(userToChat : User){
        val dbRef = FirebaseDatabase.getInstance().getReference("/messages")
        dbRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                if(message != null) {
                    if (message.senderId == userToChat.uid && message.receiverId == fbAuth.uid) {
                        adapter.add(MessageReceivedItem(message.textMessage, userToChat.userImageUrl))
                    }
                    else if(message.senderId == fbAuth.uid && message.receiverId == userToChat.uid){
                        adapter.add(MessageSendedItem(message.textMessage, myUser.userImageUrl))
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }
}
