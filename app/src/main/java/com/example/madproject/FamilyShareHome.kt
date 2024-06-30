package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.familyshare2.FamilySharingAddMember
import com.example.madproject.databinding.ActivityFamilyShareHomeBinding

class FamilyShareHome : AppCompatActivity() {

    private lateinit var binding: ActivityFamilyShareHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyShareHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addMemberCard.setOnClickListener {
            val intent = Intent(this, FamilySharingAddMember::class.java)
            startActivity(intent)

        }
        fun redirectAddMember(view: View){
            val intent = Intent(this, FamilySharingAddMember::class.java)
            startActivity(intent)
        }
    }
}