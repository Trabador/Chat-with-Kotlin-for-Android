package com.alexis.messengermock

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_received_item_layout.view.*

class MessageReceivedItem(val messageText: String, val userToChatImageUrl: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_received_item_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.messageTextView.text = messageText
        if(!userToChatImageUrl.isEmpty()) {
            Picasso.get().load(userToChatImageUrl).into(viewHolder.itemView.avatarView)
        }
    }
}