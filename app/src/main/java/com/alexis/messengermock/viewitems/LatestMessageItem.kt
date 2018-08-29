package com.alexis.messengermock.viewitems

import com.alexis.messengermock.R
import com.alexis.messengermock.dataclasses.Message
import com.alexis.messengermock.dataclasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_item_layout.view.*

class LatestMessageItem(val latestMessage: Message): Item<ViewHolder>() {

    var userReceivedMessageFrom: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_item_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.userMessageText.text = latestMessage.textMessage
        val userReceivedMessageFromId = latestMessage.senderId

        val dbRef = FirebaseDatabase.getInstance().getReference("/users/$userReceivedMessageFromId")
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                userReceivedMessageFrom = p0.getValue(User::class.java)
                if(userReceivedMessageFrom != null){
                    viewHolder.itemView.userText.text = userReceivedMessageFrom!!.email
                    val target = viewHolder.itemView.friendImage
                    Picasso.get().load(userReceivedMessageFrom!!.userImageUrl).into(target)
                }
            }

        })
    }
}