package com.alexis.messengermock.misc

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.alexis.messengermock.R
import kotlinx.android.synthetic.main.progress_dialog.view.*

class CustomProgressDialog {
    companion object {
        fun createDialog(context: Context, messageText : String): AlertDialog {
            val builder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.progress_dialog,null)
            dialogView.dialogText.text = messageText
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }
    }
}