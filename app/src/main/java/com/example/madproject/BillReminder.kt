package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.adapters.BillReAdapter
import com.example.madproject.models.BillReminderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BillReminder : AppCompatActivity() {
    private lateinit var fetchRecycler:RecyclerView
    private lateinit var cardList:ArrayList<BillReminderModel>
    private lateinit var dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fetchRecycler=findViewById(R.id.dataRecycler)
        fetchRecycler.layoutManager=LinearLayoutManager(this)
        val emptyAdapter = BillReAdapter(arrayListOf<BillReminderModel>())
        fetchRecycler.adapter = emptyAdapter

        cardList= arrayListOf<BillReminderModel>()

        getReminderData()
    }
    private fun getReminderData() {
        dbRef = FirebaseDatabase.getInstance().getReference("BillReminder")

        val query = dbRef.orderByChild("userId").equalTo("2232")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cardList.clear()

                if (snapshot.exists()) {
                    for (billSnap in snapshot.children) {
                        val billData = billSnap.getValue(BillReminderModel::class.java)
                        cardList.add(billData!!)
                    }

                    if (cardList.isNotEmpty()) {
                        val billAdapter = BillReAdapter(cardList)
                        fetchRecycler.adapter = billAdapter
                        billAdapter.setOnClickListner(object : BillReAdapter.onItemClickEventListner {
                            override fun onItem(position: Int) {
                                val intent = Intent(this@BillReminder, ViewbillReminder::class.java)
                                intent.putExtra("billId", cardList[position].billId)
                                intent.putExtra("billTitle", cardList[position].billTitle)
                                intent.putExtra("billDate", cardList[position].billDate)
                                intent.putExtra("billAmount", cardList[position].billAmount)
                                intent.putExtra("billCategory", cardList[position].billCategory)
                                intent.putExtra("billNote", cardList[position].billNote)
                                intent.putExtra("billType", cardList[position].billType)
                                startActivity(intent)
                            }
                        })

                        // Get count of child lists
                        val childCount = snapshot.childrenCount.toInt()
                        val textViewChildCount = findViewById<TextView>(R.id.bill_RemainingValue)
                        textViewChildCount.text = "$childCount"

                        // Get nearest date and its amount
                        val (nearestDate, nearestDateAmount) = getDateAmount(cardList)
                        val textViewNearestDate = findViewById<TextView>(R.id.npheadingValue)
                        textViewNearestDate.text = "$nearestDate"
                        val overallnAmount = findViewById<TextView>(R.id.overallnAmount)
                        overallnAmount.text = "$nearestDateAmount"

                        fetchRecycler.visibility = View.VISIBLE
                    } else {
                    }
                } else {
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getDateAmount(cardList: ArrayList<BillReminderModel>): Pair<String, String> {
        // Get today's date
        val today = Calendar.getInstance()
        val todayDate = today.timeInMillis

        // Find the nearest date and its corresponding amount
        var nearestDate = Long.MAX_VALUE
        var nearestDateBillAmount = ""

        for (billData in cardList) {
            if (billData.billDate < nearestDate && billData.billDate >= todayDate) {
                nearestDate = billData.billDate
                nearestDateBillAmount = billData.billAmount ?: ""
            }
        }

        // Format the nearest date
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = nearestDate
        val formattedDate = dateFormat.format(calendar.time)

        return Pair(formattedDate, nearestDateBillAmount)
    }


    fun redirectAddBill(view: View) {
        val intent = Intent(this, AddbillReminder::class.java)
        startActivity(intent)
    }
    fun redirectViewBill(view: View) {
        val intent = Intent(this, ViewbillReminder::class.java)
        startActivity(intent)
    }
}