package com.example.madproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.madproject.databinding.ActivityIncomesCreateBinding
import com.example.madproject.models.IncomesData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class IncomesCreate : AppCompatActivity() {

    lateinit var binding: ActivityIncomesCreateBinding
    var imageURL : String? = null
    var uri : Uri? = null
    private var dataBase = FirebaseDatabase.getInstance().getReference("BudgetAK")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout from activity_incomes_create.xml and set as view
        binding = ActivityIncomesCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){ result ->

            //Checking if the image is selected or not
            if (result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else{
                Toast.makeText(this@IncomesCreate, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the upload image button
        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        //set click listener for the save button
        binding.btnSave.setOnClickListener {
            saveData()
        }
    }

    fun saveData(){


        //validations for imageView
        if (uri == null) {
            Toast.makeText(this@IncomesCreate, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        //Validations for EditText s
        val date = binding.edDate.text.toString().trim()
        val amount = binding.edtAmount.text.toString().trim()
        val category = binding.edtCategory.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        if (date.isEmpty()) {
            binding.edDate.error = "Date is required"
            binding.edDate.requestFocus()
            return
        }

        if (amount.isEmpty()) {
            binding.edtAmount.error = "Amount is required"
            binding.edtAmount.requestFocus()
            return
        }

        if (category.isEmpty()) {
            binding.edtCategory.error = "Category is required"
            binding.edtCategory.requestFocus()
            return
        }

        if (description.isEmpty()) {
            binding.edtDescription.error = "Description is required"
            binding.edtDescription.requestFocus()
            return
        }

        val storageReference = FirebaseStorage.getInstance().reference.child("Task Image")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@IncomesCreate)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while(!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener{
            dialog.dismiss()
        }
    }

    fun validateInputs(): Boolean {
        var isValid = true

        // validate project name
        val date = binding.edDate.text.toString().trim()
        if (date.isEmpty()) {
            binding.edDate.error = "Date is required!"
            isValid = false
        }

        // validate project description
        val amount = binding.edtAmount.text.toString().trim()
        if (amount.isEmpty()) {
            binding.edtAmount.error = "Amount is required!"
            isValid = false
        }

        // validate time period
        val category = binding.edtCategory.text.toString().trim()
        if (category.isEmpty()) {
            binding.edtCategory.error = "Category is required!"
            isValid = false
        }

        // validate task
        val description = binding.edtDescription.text.toString().trim()
        if (description.isEmpty()) {
            binding.edtCategory.error = "Description is required!"
            isValid = false
        }

        return isValid
    }

    //function to upload data
    private fun uploadData(){
        val date = binding.edDate.text.toString()
        val amount = binding.edtAmount.text.toString()
        val category = binding.edtCategory.text.toString()
        val description = binding.edtDescription.text.toString()
        var id = dataBase.push().key!!


        val incomeData = IncomesData(id,date, amount, category, description, imageURL)


        dataBase.child(id)
            .setValue(incomeData).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this@IncomesCreate, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this@IncomesCreate, e.message.toString(), Toast.LENGTH_SHORT).show()
            }

    }
}