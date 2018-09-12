package com.edu.hust.henry.cashcal.helpers

import android.util.Log
import com.edu.hust.henry.cashcal.model.Member
import com.edu.hust.henry.cashcal.model.OrderData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDBHelper{
    var db = FirebaseDatabase.getInstance().reference

    fun saveData(data: OrderData, orderDB: DatabaseReference, year: Int, week: Int, dayOfWeek: Int){
        data.uuid = orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().key
        orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().setValue(data)
    }

    fun addMember(db: DatabaseReference, name: String){
        val uuid = db.child("member").push().key
        db.child("member").push().setValue(Member(uuid, name))
                .addOnSuccessListener {
                    Log.d("ADD_NEW_MEMBER", "Successfully!")
                }
                .addOnFailureListener {
                    Log.d("ADD_NEW_MEMBER", "Failure!")
                }
    }
}