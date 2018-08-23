package com.alexis.messengermock

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_item.view.*

class UserItem(private val user : User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.activity_user_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.userItemText.text = user.email
    }

}