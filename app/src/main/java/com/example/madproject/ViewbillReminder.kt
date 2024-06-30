package com.example.madproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.madproject.models.BillReminderModel
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewbillReminder : ComponentActivity() {
    private lateinit var dateText: TextView
    private lateinit var titleViewHeading: TextView
    private lateinit var amountText: TextView
    private lateinit var categoryText: TextView
    private lateinit var noteText: TextView
    private lateinit var typeText: TextView
    private lateinit var billId: String

    private lateinit var btnEdit: Button
    public var userId="2232"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_bill)
        initView()
        setValuesView()
        btnEdit.setOnClickListener{
            openEdit(
                intent.getStringExtra("billId").toString(),
                intent.getStringExtra("billTitle").toString()
            )
        }
    }

    private fun initView() {
        dateText = findViewById(R.id.dateValueView)
        titleViewHeading = findViewById(R.id.titleView)
        amountText = findViewById(R.id.amountViewValue)
        categoryText = findViewById(R.id.categoryViewValue)
        noteText = findViewById(R.id.noteViewValue)
        typeText = findViewById(R.id.typeViewValue)

        btnEdit = findViewById(R.id.btnEdit)
    }

    private fun setValuesView(){
        val timeInMillis = intent.getLongExtra("billDate", 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        billId= intent.getStringExtra("billId").toString()
        titleViewHeading.text=intent.getStringExtra("billTitle")
        dateText.text = dateFormat.format(calendar.time)
        amountText.text="Rs " +intent.getStringExtra("billAmount")
        categoryText.text=intent.getStringExtra("billCategory")
        noteText.text=intent.getStringExtra("billNote")
        typeText.text=intent.getStringExtra("billType")
    }

    private fun openEdit(
        billId: String,
        billTitle: String
    ) {
        val mData = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDataView = inflater.inflate(R.layout.edit_reminder, null)

        mData.setView(mDataView)

        val setBillTitle = mDataView.findViewById<EditText>(R.id.updateBillTitleValue)
        val setBillDate = mDataView.findViewById<EditText>(R.id.updateBillDateValue)
        val setBillAmount = mDataView.findViewById<EditText>(R.id.updateBillAmountValue)
        val setBillCategory = mDataView.findViewById<EditText>(R.id.updateBillCategoryValue)
        val setBillType = mDataView.findViewById<EditText>(R.id.updateBillTypeValue)
        val setBillNote = mDataView.findViewById<EditText>(R.id.updateBillNoteValue)
        val btnUpdate = mDataView.findViewById<Button>(R.id.btnUpdate)
        val btnRemove = mDataView.findViewById<Button>(R.id.btnRemove)



        mData.setTitle("Updating $billTitle")
        val alertDialog = mData.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(true)

        // Set initial values in the EditText fields
        setBillTitle.setText(titleViewHeading.text)
        setBillDate.setText(dateText.text)
        setBillAmount.setText(amountText.text)
        setBillCategory.setText(categoryText.text)
        setBillType.setText(typeText.text)
        setBillNote.setText(noteText.text)

        btnUpdate.setOnClickListener {
            updateData(
                billId,
                setBillTitle.text.toString(),
                setBillDate.text.toString(),
                setBillAmount.text.toString(),
                setBillCategory.text.toString(),
                setBillNote.text.toString(),
                setBillType.text.toString()
            )
            Toast.makeText(applicationContext, "Data Updated", Toast.LENGTH_LONG).show()

            titleViewHeading.text = setBillTitle.text.toString()
            dateText.text = setBillDate.text.toString()
            amountText.text = setBillAmount.text.toString()
            categoryText.text = setBillCategory.text.toString()
            noteText.text = setBillNote.text.toString()
            typeText.text = setBillType.text.toString()

            alertDialog.dismiss()
            val intent = Intent(this, BillReminder::class.java)
            startActivity(intent)
        }
        btnRemove.setOnClickListener {
            deleteData(billId)
        }


    }
    private fun deleteData(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("BillReminder").child(id)
        dbRef.removeValue()
        Toast.makeText(applicationContext, "Data Removed", Toast.LENGTH_LONG).show()
        finish() // Optional: Close the current activity after removing the data
        val intent = Intent(this, BillReminder::class.java)
        startActivity(intent)
    }
    private fun updateData(
        id: String,
        title: String,
        date: String,
        amount: String,
        category: String,
        note: String,
        type: String
    ) {
        val inputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val parsedDate = inputDateFormat.parse(date)
        val timeInMillis = parsedDate?.time ?: 0
        val dbRef = FirebaseDatabase.getInstance().getReference("BillReminder").child(id)
        val billData = BillReminderModel(id, title, amount, timeInMillis, category, note,userId,type)
        dbRef.setValue(billData)
    }
    fun redirectAddBill(view: View) {
        val intent = Intent(this, AddbillReminder::class.java)
        startActivity(intent)
    }
    fun redirectUpdateBill(view: View) {
        val intent = Intent(this, UpdateBillReminder::class.java)
        startActivity(intent)
    }
}