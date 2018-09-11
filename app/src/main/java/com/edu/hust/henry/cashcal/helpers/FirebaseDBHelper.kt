package com.edu.hust.henry.cashcal.helpers

import com.edu.hust.henry.cashcal.model.OrderData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDBHelper{
    var db = FirebaseDatabase.getInstance().getReference("courses")

    fun saveData(data: OrderData, orderDB: DatabaseReference, year: Int, week: Int, dayOfWeek: Int){
        data.uuid = orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().key
        orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().setValue(data)
    }


}