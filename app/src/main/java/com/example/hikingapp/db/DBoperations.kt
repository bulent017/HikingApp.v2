package com.example.hikingapp.db

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.hikingapp.Adapter.RunAdapter
import com.example.hikingapp.Model.Run
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.osmdroid.util.GeoPoint
import java.util.*
import kotlin.collections.ArrayList

class DBoperations {
    public  lateinit var listOfRunActivity: ArrayList<Run>
    private lateinit var  database: DatabaseReference
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val contex: Context? = null

    fun saveData(date: String, distance: Double, time:String, route:ArrayList<GeoPoint>){
        database = Firebase.database.reference

        val run = Run("",date,distance,time,route)
        if (uid!=null){
            database.child("user").child(uid).child("Run").push().setValue(run)
        }

    }


    fun readData(adapter:RunAdapter){
        listOfRunActivity = ArrayList()
        if (uid!=null){
            database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Run")
            database.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        listOfRunActivity.clear()
                        for (runSnapshot in snapshot.children){
                            val run = runSnapshot.getValue<Run>()
                            if (run != null) {

                                run.id = runSnapshot.key.toString()
                                listOfRunActivity.add(run)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(contex,"Veriler gelemedi",Toast.LENGTH_SHORT).show()
                }

            })

        }
    }



}