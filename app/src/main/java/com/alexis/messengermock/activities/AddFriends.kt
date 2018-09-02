package com.alexis.messengermock.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alexis.messengermock.R
import com.alexis.messengermock.dataclasses.FriendList
import com.alexis.messengermock.dataclasses.User
import com.alexis.messengermock.viewitems.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriends : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()
    private val uid = FirebaseAuth.getInstance().uid
    private val userList = hashMapOf<String, User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        supportActionBar?.title = resources.getString(R.string.addFriend)
        fetchUsersFromDataBase()
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            addNewFriend(userItem.user)
        }
        usersList.adapter = adapter
    }


    private fun addNewFriend(newFriend: User){
        if(uid != null) {
            var friends = FriendList()
            val dbRef = FirebaseDatabase.getInstance().getReference("/friends/$uid")
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val aux = p0.getValue(FriendList::class.java)
                    if(aux != null) {
                        friends = aux
                    }
                    friends.friendList.add(newFriend.uid)
                    dbRef.setValue(friends)
                            .addOnSuccessListener {
                                userList.remove(newFriend.uid)
                                refreshRecyclerView()
                                Toast.makeText(applicationContext, "User added : ${newFriend.email}", Toast.LENGTH_SHORT).show()
                            }
                }
            })
        }
    }

    private fun fetchUsersFromDataBase(){
        val dbRef = FirebaseDatabase.getInstance().getReference("/users")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        if(user.uid != uid) {
                            userList[user.uid] = user
                        }
                    }
                }
                checkFriends()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun checkFriends(){
        val dbRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/friendList")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null) {
                    val list = p0.value as List<String>
                    list.forEach {
                        if (userList.contains(it)) {
                            userList.remove(it)
                        }
                    }
                }
                refreshRecyclerView()
            }
        })
    }

    private fun refreshRecyclerView(){
        adapter.clear()
        userList.values.forEach {
            adapter.add(UserItem(it))
        }
    }
}
