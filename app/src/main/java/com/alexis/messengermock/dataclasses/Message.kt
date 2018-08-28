package com.alexis.messengermock.dataclasses

data class Message(val id: String, val senderId: String, val receiverId : String,
                   val textMessage: String, val timestamp: String){
    constructor() : this("", "", "", "", "")
}