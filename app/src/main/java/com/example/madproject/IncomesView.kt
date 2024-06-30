package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madproject.models.IncomesData
import com.example.madproject.databinding.ActivityIncomesViewBinding
import com.google.firebase.database.*

class IncomesView : AppCompatActivity() {

    private lateinit var binding: ActivityIncomesViewBinding
    private lateinit var dataList: ArrayList<IncomesData>
    private lateinit var adapter: IncomesAdapter
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout from activity_incomes_view.xml and set as view
        binding = ActivityIncomesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@IncomesView, 1)
        binding.recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this@IncomesView)
        builder.setCancelable(false)
//        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        // Initialize the data list and adapter for the RecyclerView
        dataList = ArrayList()
        adapter = IncomesAdapter(this@IncomesView, dataList)
        binding.recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("BudgetAK")
        dialog.show()

        // Set up the ValueEventListener to listen for changes in the database
        eventListener = databaseReference!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the data list
                dataList.clear()
                for (itemSnapShot in snapshot.children) {
                    val incomeClass = itemSnapShot.getValue(IncomesData::class.java)
                    if (incomeClass != null){
                        dataList.add(incomeClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })

        // Set click listener for the FloatingActionButton to open the create activity
        binding.fab.setOnClickListener {
            val intent = Intent(this, IncomesCreate::class.java)
            startActivity(intent)
        }
    }
}