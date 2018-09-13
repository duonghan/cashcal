package com.edu.hust.henry.cashcal.helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.edu.hust.henry.cashcal.model.Member
import com.edu.hust.henry.cashcal.model.OrderData
import com.google.firebase.database.*

object FirebaseDBHelper{
    var db = FirebaseDatabase.getInstance().reference
    private const val ADD_NEW_MEMBER_TAG: String = "ADD_NEW_MEMBER"
    private const val FETCH_NEW_MEMBER_TAG: String = "FETCH_NEW_MEMBER"

    fun saveData(data: OrderData, orderDB: DatabaseReference, year: Int, week: Int, dayOfWeek: Int){
        data.uuid = orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().key
        orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().setValue(data)
    }

    fun addMember(db: DatabaseReference, name: String){
        val uuid = db.child("member").push().key
        db.child("member").push().setValue(Member(uuid, name))
                .addOnSuccessListener {
                    Log.d(ADD_NEW_MEMBER_TAG, "Successfully!")
                }
                .addOnFailureListener {
                    Log.d(ADD_NEW_MEMBER_TAG, "Failure!")
                }
    }

    fun fetchMemberData(db: DatabaseReference): MutableList<Member>{
        val memberList: MutableList<Member> = mutableListOf()

        db.child("member").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "postMessages:onCancelled", databaseError!!.toException())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A message has changed position
                val member = dataSnapshot.getValue(Member::class.java)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildChanged:" + dataSnapshot!!.key)

                // A message has changed
                val member = dataSnapshot.getValue(Member::class.java)
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                // A new member has been added
                // onChildAdded() will be called for each node at the first time
                val member = dataSnapshot!!.getValue(Member::class.java)
                memberList.add(member!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val member = dataSnapshot.getValue(Member::class.java)
            }
        })

        return memberList
    }

//    fun fetchMemberDataTest(db: DatabaseReference): MutableList<Member>{
//        val memberList: MutableList<Member> = mutableListOf()
//
//        db.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(databaseError: DatabaseError?) {
//                Log.e(FETCH_NEW_MEMBER_TAG, "postMessages:onCancelled", databaseError!!.toException())
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot?) {
//
//            }
//        })
//    }

    fun fetchOrderData(db: DatabaseReference, context: Context, year: Int, week: Int): MutableList<OrderData>{
        val orderList: MutableList<OrderData> = mutableListOf()

        db.child("order").child("$year").child("$week").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "postMessages:onCancelled", databaseError!!.toException())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A message has changed position
                val order = dataSnapshot.getValue(OrderData::class.java)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildChanged:" + dataSnapshot!!.key)

                // A message has changed
                val order = dataSnapshot.getValue(OrderData::class.java)
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                // A new member has been added
                // onChildAdded() will be called for each node at the first time
                val order = dataSnapshot!!.getValue(OrderData::class.java)
                orderList.add(order!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                Log.e(FETCH_NEW_MEMBER_TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val order = dataSnapshot.getValue(OrderData::class.java)
            }
        })

        return orderList
    }

}