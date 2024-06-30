package com.example.madproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madproject.IncomesEdit
import com.example.madproject.models.IncomesData

class IncomesAdapter (private val context: android.content.Context, private val dataList: List<IncomesData>): RecyclerView.Adapter<IncomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return IncomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val currentIncome= dataList[position]
        Glide.with(context).load(dataList[position].dataImage).into(holder.recImage)
        holder.recCategory.text = dataList[position].category
        holder.recAmount.text = dataList[position].amount

        holder.recCard.setOnClickListener {

            val intent = Intent(holder.itemView.context, IncomesEdit::class.java)
            intent.putExtra("id",currentIncome.id)
            intent.putExtra("incomeImage", currentIncome.dataImage)
            intent.putExtra("incomeDate", currentIncome.date)
            intent.putExtra("incomeAmount", currentIncome.amount)
            intent.putExtra("incomeCategory", currentIncome.category)
            intent.putExtra("incomeDescription", currentIncome.description)



            // Start the IncomeDetails activity
            holder.itemView.context.startActivity(intent)
        }
    }

}

class IncomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recCategory: TextView = itemView.findViewById(R.id.recCategory)
    var recAmount: TextView = itemView.findViewById(R.id.recAmount)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}

