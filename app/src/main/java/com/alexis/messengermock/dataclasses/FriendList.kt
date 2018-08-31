package com.alexis.messengermock.dataclasses

data class FriendList(val friendList: MutableList<String>) {
    constructor() : this(mutableListOf())
}