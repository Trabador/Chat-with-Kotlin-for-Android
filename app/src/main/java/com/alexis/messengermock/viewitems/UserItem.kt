package com.alexis.messengermock.viewitems

import com.alexis.messengermock.dataclasses.User
import com.alexis.messengermock.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_item.view.*

class UserItem(val user : User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.activity_user_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.userItemText.text = user.email
        val target = viewHolder.itemView.friendImage
        Picasso.get().load(user.userImageUrl).into(target)
    }

}