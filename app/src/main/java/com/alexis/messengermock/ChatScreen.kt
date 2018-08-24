package com.alexis.messengermock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_screen.*
import kotlinx.android.synthetic.main.message_sended_item_layout.*

class ChatScreen : AppCompatActivity() {

    lateinit var fbAuth: FirebaseAuth
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)
        fbAuth = FirebaseAuth.getInstance()
        val userToChat = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        supportActionBar?.title = userToChat.email
        sendMessageButton.setOnClickListener{
            sendMessage(userToChat.uid)
        }

        listenToMessages(userToChat)
        messageLogView.adapter = adapter

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
                        adapter.add(MessageReceivedItem(message.textMessage))
                    }
                    else if(message.senderId == fbAuth.uid && message.receiverId == userToChat.uid){
                        adapter.add(MessageSendedItem(message.textMessage))
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

    private fun dummy(){
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(MessageReceivedItem("\n" +
                "Ejemplo de Lorem ipsum\n" +
                "Lorem ipsum es el texto que se usa habitualmente en diseño gráfico en demostraciones de tipografías o de borradores de diseño para probar el diseño visual antes de insertar el texto final."))
        adapter.add(MessageSendedItem("\n" +
                "Ejemplo de Lorem ipsum\n" +
                "Lorem ipsum es el texto que se usa habitualmente en diseño gráfico en demostraciones de tipografías o de borradores de diseño para probar el diseño visual antes de insertar el texto final."))
        adapter.add(MessageReceivedItem("\n" +
                "Ejemplo de Lorem ipsum\n" +
                "Lorem ipsum es el texto que se usa habitualmente en diseño gráfico en demostraciones de tipografías o de borradores de diseño para probar el diseño visual antes de insertar el texto final."))
        adapter.add(MessageSendedItem("\n" +
                "Ejemplo de Lorem ipsum\n" +
                "Lorem ipsum es el texto que se usa habitualmente en diseño gráfico en demostraciones de tipografías o de borradores de diseño para probar el diseño visual antes de insertar el texto final."))
        messageLogView.adapter = adapter
    }
}
