package com.example.hikingapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.hikingapp.Model.Run
import com.example.hikingapp.databinding.RecyclerRowBinding
import org.osmdroid.util.GeoPoint


class RunAdapter(private var listOfRun:ArrayList<Run>,private val listener:OnItemClickListener):RecyclerView.Adapter<RunAdapter.RunHolder>(){

    interface OnItemClickListener {
        //val id:String,val date:String,val distance: Double,val time:String,val route:ArrayList<GeoPoint>
        fun onItemClickButton(
            //id:String,
            date:String,distance: Double,time:String,route:ArrayList<GeoPoint>)
    }


    inner class RunHolder(private val itemBinding: RecyclerRowBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int){
            val item = listOfRun[position]
            item.apply {
                itemBinding.apply {
                    dateTextView.text = date
                    distanceTextView.text = distance.toString()
                    timeTextview.text = time

                }
            }


        }
        init {
            itemBinding.activityHistoryButton.setOnClickListener {
                listener.onItemClickButton(
                    //listOfRun[bindingAdapterPosition].id!!,
                    listOfRun[bindingAdapterPosition].date,
                    listOfRun[bindingAdapterPosition].distance,
                    listOfRun[bindingAdapterPosition].time,
                    listOfRun[bindingAdapterPosition].route
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  RunHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfRun.size
    }

    override fun onBindViewHolder(holder: RunHolder, position: Int) {
        holder.bind(position)
    }


}