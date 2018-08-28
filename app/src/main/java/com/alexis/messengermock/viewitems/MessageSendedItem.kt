package com.alexis.messengermock.viewitems

import com.alexis.messengermock.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_sended_item_layout.view.*

class MessageSendedItem(val messageText: String, val myUserImageUrl: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_sended_item_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.messageSendedText.text = messageText
        Picasso.get().load(myUserImageUrl).into(viewHolder.itemView.myAvatar)
    }
}