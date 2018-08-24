package com.alexis.messengermock

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_received_item_layout.view.*

class MessageReceivedItem(val messageText: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_received_item_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.messageTextView.text = messageText
    }
}