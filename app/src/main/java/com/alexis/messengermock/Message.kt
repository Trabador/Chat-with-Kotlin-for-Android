package com.alexis.messengermock

import java.sql.Timestamp

data class Message(val id: String, val senderId: String, val receiverId : String,
                   val textMessage: String, val timestamp: String){
    constructor() : this("", "", "", "", "")
}