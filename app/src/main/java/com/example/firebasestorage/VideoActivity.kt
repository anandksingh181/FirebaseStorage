package com.example.firebasestorage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.example.firebasestorage.databinding.ActivityVideoBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoView.isVisible =false
        binding.videoBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "video/*"
            videoLauncher.launch(intent)
        }
    }
    val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            if (it.data!=null){
                val ref = Firebase.storage.reference.child("Video/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {

                        Firebase.database.reference.child("Video").push().setValue(it.toString())
                        Toast.makeText(this, "Video Uploaded", Toast.LENGTH_SHORT).show()
                        binding.videoBtn.isVisible = false
                        binding.videoView.isVisible = true
                        val mediaController = MediaController(this)
                        mediaController.setAnchorView(binding.videoView)

                        binding.videoView.setVideoURI(it)
                        binding.videoView.setMediaController(mediaController)
                        binding.videoView.start()
                    }
                }

            }
        }
    }
    private fun getFileType(data: Uri?): String? {
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(contentResolver.getType(data!!))
    }
}