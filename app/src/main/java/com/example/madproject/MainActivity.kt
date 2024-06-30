package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

    }
   fun redirectBillReminder(view: View){
       val intent = Intent(this, BillReminder::class.java)
       startActivity(intent)
   }
    fun redirectIncome(view: View){
        val intent = Intent(this, IncomesView::class.java)
        startActivity(intent)
    }
    fun redirectFamilyShare(view: View){
        val intent = Intent(this, FamilyShareHome::class.java)
        startActivity(intent)
    }
}