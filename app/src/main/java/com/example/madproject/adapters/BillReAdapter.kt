package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.BillReminderModel
import java.util.Calendar

class BillReAdapter (private val remindersList:ArrayList<BillReminderModel>):RecyclerView.Adapter<BillReAdapter.ViewHolder>(){

    private lateinit var itemListner:onItemClickEventListner
    interface onItemClickEventListner{
        fun onItem(position: Int){

        }
    }
    fun setOnClickListner(clickListener: onItemClickEventListner){
        itemListner=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
val itemView=LayoutInflater.from(parent.context).inflate(R.layout.bill_card_item,parent,false)
    return ViewHolder(itemView,itemListner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReminder=remindersList[position]
        holder.fetchTitle.text=currentReminder.billTitle
        holder.fetchAmount.text=currentReminder.billAmount
        holder.fetchCategory.text=currentReminder.billCategory
        holder.fetchTitle.text=currentReminder.billTitle

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentReminder?.billDate ?: 0
        val convertedYear = calendar.get(Calendar.YEAR)
        val convertedMonth = calendar.get(Calendar.MONTH)+1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        holder.fetchDate.text=dayOfMonth.toString()
        holder.fetchMonth.text=convertedMonth.toString()
        holder.fetchYear.text= convertedYear.toString()

    }

    override fun getItemCount(): Int {
        return remindersList.size
    }
    class ViewHolder(itemView: View,clickListener:onItemClickEventListner):RecyclerView.ViewHolder(itemView){
        val fetchDate: TextView=itemView.findViewById(R.id.dateBill)
        val fetchMonth: TextView=itemView.findViewById(R.id.monthBill)
        val fetchYear: TextView=itemView.findViewById(R.id.yearBill)
        val fetchTitle: TextView=itemView.findViewById(R.id.titleBill)
        val fetchAmount: TextView=itemView.findViewById(R.id.amountBill)
        val fetchCategory: TextView=itemView.findViewById(R.id.categoryBill)
        init {
            itemView.setOnClickListener(){
                clickListener.onItem(adapterPosition)
            }
        }

    }
}