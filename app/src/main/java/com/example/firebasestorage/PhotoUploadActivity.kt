package com.example.firebasestorage

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebasestorage.databinding.ActivityPhotoUploadBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class PhotoUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextPage.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
        }

        binding.uploadImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }
    }

    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            if (it.data!=null){
                val ref = Firebase.storage.reference.child("photo")
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        binding.imageView.setImageURI(it)
                        Toast.makeText(this, "Photo Uploaded", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(it.toString()).into(binding.imageView);
                    }
                }
            }
        }
    }
}