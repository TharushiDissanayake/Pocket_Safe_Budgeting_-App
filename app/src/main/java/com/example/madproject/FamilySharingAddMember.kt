package com.example.familyshare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.madproject.FamilyShareHome
import com.example.madproject.databinding.ActivityFamilySharingAddMemberBinding
import com.example.madproject.models.MembersData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class FamilySharingAddMember : AppCompatActivity() {

    private lateinit var binding: ActivityFamilySharingAddMemberBinding
    private lateinit var databasefs: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilySharingAddMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databasefs = FirebaseDatabase.getInstance().getReference("familyshare2")

        binding.saveAddBtn.setOnClickListener {
            val fid = databasefs.push().key!!
            val email = binding.textemailinput.text.toString()
            Log.d("FamilySharingAddMember", "fid=$fid, email=$email")
            val Member = MembersData(fid, email)
            Log.d("FamilySharingAddMember", "Member=$Member")
            databasefs.child(fid).setValue(Member).addOnSuccessListener {
                Log.d("email","email=$email")
                binding.textemailinput.text.clear()

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@FamilySharingAddMember, FamilyShareHome::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this@FamilySharingAddMember, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

