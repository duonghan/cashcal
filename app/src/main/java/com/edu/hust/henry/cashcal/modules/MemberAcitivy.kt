package com.edu.hust.henry.cashcal.modules

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.edu.hust.henry.cashcal.R
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.addMember
import com.edu.hust.henry.cashcal.helpers.Utils.dp2px
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.dialog_new_member.*
import kotlinx.android.synthetic.main.dialog_new_member.view.*
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.util.Log
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.fetchMemberData
import com.edu.hust.henry.cashcal.model.Member
import com.google.firebase.database.*


class MemberAcitivy : AppCompatActivity() {

    private val TAG = "MEMBERACTIVITY"
    private var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mMemberReference: DatabaseReference? = null
    private var memberList: MutableList<Member> = mutableListOf()
    private var mMemberListener: ChildEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        initialTitle()

        mMemberReference = FirebaseDatabase.getInstance().getReference("member")
        firebaseListenerInit()
        showList(memberList)
        Log.d(TAG, ">>>>>>>>>>> MemberList: ${memberList.size}")

        fab_add_person.setOnClickListener {
            showAddMemberDialog(this@MemberAcitivy)
        }
    }

    private fun showList(memberList: MutableList<Member>){
        val creator = SwipeMenuCreator { menu ->
            // create "delete" item
            val deleteItem = SwipeMenuItem(
                    applicationContext)
            // set item background
            deleteItem.background = ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25))
            // set item width
            deleteItem.width = dp2px(90, this@MemberAcitivy)
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete)
            // add to menu
            menu.addMenuItem(deleteItem)
        }

        val nameList: MutableList<String> = memberList.map { it.name }.toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList)
        list_people.adapter = adapter

        list_people.setOnMenuItemClickListener { position, menu, index ->
            when (index) {
                0 -> Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
            }// open
            // delete
            // false : close the menu; true : not close the menu
            false
        }

        // set creator
        list_people.setMenuCreator(creator)
    }

    private fun showAddMemberDialog(context: Context) {

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_new_member, null)
        val dialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.add_member))
            .setIcon(R.drawable.ic_add_person_dialog).setView(view)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(android.R.string.ok){
                dialog, which ->
                    val newMember: String = view.edt_add_person.text.toString()
                    var isValid = true
                    if (newMember.isBlank()) {
                        edt_add_person.error = getString(R.string.validation_empty)
                        isValid = false
                    }

                    if (isValid) {
                        val db: DatabaseReference = FirebaseDatabase.getInstance().reference
                        addMember(db, newMember)
                    }

                    if (isValid) {
                        dialog.dismiss()
                    }
            }

        dialog.show()
    }

    /**
     * @description: setup title text and color in actionbar
     */
    private fun initialTitle(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = getString(R.string.member)
        val s = SpannableString(title)
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = s
    }

    private fun firebaseListenerInit() {

        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                // A new message has been added
                // onChildAdded() will be called for each node at the first time
                val member = dataSnapshot!!.getValue(Member::class.java)
                memberList.add(member!!)

                Log.e(TAG, "onChildAdded:" + member.name)

                val latest = memberList[memberList.size - 1]
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                Log.e(TAG, "onChildChanged:" + dataSnapshot!!.key)

                // A message has changed
                val member = dataSnapshot.getValue(Member::class.java)
                Toast.makeText(this@MemberAcitivy, "onChildChanged: " + member!!.name, Toast.LENGTH_SHORT).show()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val message = dataSnapshot.getValue(Member::class.java)
                Toast.makeText(this@MemberAcitivy, "onChildRemoved: " + message!!.name, Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A message has changed position
                val message = dataSnapshot.getValue(Member::class.java)
                Toast.makeText(this@MemberAcitivy, "onChildMoved: " + message!!.name, Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError?) {
                Log.e(TAG, "postMessages:onCancelled", databaseError!!.toException())
                Toast.makeText(this@MemberAcitivy, "Failed to load Message.", Toast.LENGTH_SHORT).show()
            }
        }

        mMemberReference!!.addChildEventListener(childEventListener)

        // copy for removing at onStop()
        mMemberListener = childEventListener

        Log.d(TAG, ">>>>>>>>>>> MemberList: ${memberList.size}")
    }

    override fun onStop() {
        super.onStop()

        if (db.child("member") != null) {
            db.child("member")!!.removeEventListener(mMemberListener)
        }

        for (message in memberList) {
            Log.e(TAG, "listItem: " + message.name)
        }
    }
}
