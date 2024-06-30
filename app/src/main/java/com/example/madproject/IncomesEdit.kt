package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.madproject.IncomesView
import com.example.madproject.databinding.ActivityIncomesEditBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IncomesEdit : AppCompatActivity() {

    private lateinit var binding: ActivityIncomesEditBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout from activity_incomes_edit.xml and set as view
        binding = ActivityIncomesEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Get data from previous activity and set to corresponding views
        val bundle = intent.extras
        if (bundle != null){
            val imageUrl = bundle.getString("incomeImage")
            Glide.with(this)
                .load(imageUrl)
                .into(binding.detailImage)
            binding.detailDate.text = Editable.Factory.getInstance().newEditable(bundle.getString("incomeDate"))
            binding.detailAmount.text = Editable.Factory.getInstance().newEditable(bundle.getString("incomeAmount"))
            binding.detailCategory.text = Editable.Factory.getInstance().newEditable(bundle.getString("incomeCategory"))
            binding.detailDescription.text = Editable.Factory.getInstance().newEditable(bundle.getString("incomeDescription"))
            binding.id.text = Editable.Factory.getInstance().newEditable(bundle.getString("id"))
        }

        //set click listener for the delete button
        binding.btnDelete.setOnClickListener {

            val id = binding.id.text.toString()


            if (id.isNotEmpty()) {
                deleteData(id)
            } else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }


        //set click listener for the update button
        binding.btnUpdate.setOnClickListener {
            val updateId = binding.id.text.toString()
            val updateDate = binding.detailDate.text.toString()
            val updateAmount = binding.detailAmount.text.toString()
            val updateCategory = binding.detailCategory.text.toString()
            val updateDescription = binding.detailDescription.text.toString()

            if (updateDate.isNotEmpty() && updateAmount.isNotEmpty() && updateCategory.isNotEmpty() && updateDescription.isNotEmpty()) {
                updateData(updateId, updateDate, updateAmount, updateCategory, updateDescription)
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

    }



    // Function to delete plan data from Firebase database
    private fun deleteData(id: String){
        Log.d("id id",id)
        val db : DatabaseReference = FirebaseDatabase.getInstance().getReference("BudgetAK")
        val delete = db.child(id).removeValue()
        Log.d("deleted",delete.toString())
            delete.addOnSuccessListener {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@IncomesEdit, IncomesView::class.java)
            startActivity(intent)
            finish()
        }
        delete.addOnFailureListener {
            Toast.makeText(this, "Unable to delete", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to update plan data in Firebase database
    private fun updateData(id: String, updateDate: String, updateAmount: String, updateCategory: String, updateDescription: String) {
        val db = FirebaseDatabase.getInstance().getReference("BudgetAK")

        // Create map of updated plan data
        val incomes = mapOf(
            "id" to id,
            "date" to updateDate,
            "amount" to updateAmount,
            "category" to updateCategory,
            "description" to updateDescription

        )

        // Update date
        binding.detailDate.text = Editable.Factory.getInstance().newEditable(updateDate)

        // Update amount
        binding.detailAmount.text = Editable.Factory.getInstance().newEditable(updateAmount)

        // Update category
        binding.detailCategory.text = Editable.Factory.getInstance().newEditable(updateCategory)

        // Update description
        binding.detailDescription.text = Editable.Factory.getInstance().newEditable(updateDescription)

        db.child(id).updateChildren(incomes).addOnSuccessListener {


            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@IncomesEdit, IncomesView::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
        }
    }
}