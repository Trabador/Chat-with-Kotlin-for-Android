package com.alexis.messengermock.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alexis.messengermock.R
import com.alexis.messengermock.misc.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_settings.*
import java.util.*

class UserSettings : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        supportActionBar?.title = resources.getString(R.string.settings)
        selectImageButton.setOnClickListener {
            selectPhotoImage()
        }
        updateButton.setOnClickListener{
            updateData()
        }
    }

    private companion object {
        const val REQUEST_CODE = 0
    }

    private fun selectPhotoImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            Picasso.get().load(selectedPhotoUri).into(selectedPhotoView)
            selectImageButton.alpha = 0f
        }
    }

    private fun updateData(){
        if(selectedPhotoUri == null){
            return
        }
        val messageText = resources.getString(R.string.updateMsg)
        val dialog = CustomProgressDialog.createDialog(this,messageText)
        dialog.show()
        val filename = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/images/$filename")
        storageRef.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("User", "Image uploaded ${it.metadata?.path}")
                    storageRef.downloadUrl.addOnSuccessListener {
                        if(fbAuth.uid != null) {
                            val dbRef = FirebaseDatabase.getInstance().getReference("/users").child(fbAuth.uid.toString())
                            val newData = mapOf<String, String>("userImageUrl" to it.toString())
                            dbRef.updateChildren(newData)
                                    .addOnSuccessListener {
                                        Log.d("User", "Success updating")
                                        dialog.dismiss()
                                        val msg = resources.getString(R.string.imageUpdateMsg)
                                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                        }
                    }
                }
    }
}
