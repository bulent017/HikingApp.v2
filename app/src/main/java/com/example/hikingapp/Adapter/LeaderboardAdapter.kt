package com.example.hikingapp.Adapter



import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.hikingapp.Model.Run
import com.example.hikingapp.databinding.RecyclerrowLeaderboardBinding
import org.osmdroid.util.GeoPoint


class LeaderboardAdapter(private var listOfRun:ArrayList<Run>):RecyclerView.Adapter<LeaderboardAdapter.LeaaderboardHolder>(){




    inner class LeaaderboardHolder(private val itemBinding: RecyclerrowLeaderboardBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int){
            val item = listOfRun[position]
            item.apply {
                itemBinding.apply {
                    orderTextview.text = date
                    distanceTextView.text = distance.toString()
                    nameSurnameTextview.text = time

                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaaderboardHolder {
        val binding = RecyclerrowLeaderboardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  LeaaderboardHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfRun.size
    }

    override fun onBindViewHolder(holder: LeaaderboardHolder, position: Int) {
        holder.bind(position)
    }


}