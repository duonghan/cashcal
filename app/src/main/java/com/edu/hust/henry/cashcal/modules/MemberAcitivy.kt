package com.edu.hust.henry.cashcal.modules

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.edu.hust.henry.cashcal.R
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.addMember
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.db
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.deleteMember
import com.edu.hust.henry.cashcal.helpers.Utils.dp2px
import com.edu.hust.henry.cashcal.model.Member
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.dialog_new_member.*
import kotlinx.android.synthetic.main.dialog_new_member.view.*


class MemberAcitivy : AppCompatActivity() {

    private val TAG = "MEMBERACTIVITY"
    private var mMemberReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("member")
    private var memberList: MutableList<Member> = mutableListOf()
    private var mMemberListener: ChildEventListener? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        initialTitle()
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListOf())
        list_people.adapter = adapter
        firebaseListenerInit()

        showList()

        fab_add_person.setOnClickListener {
            showAddMemberDialog(this@MemberAcitivy)
        }
    }

    private fun showList(){
//        Log.d(TAG, ">>>>>>>>>>> MemberList: ${memberList.size}")
        val creator = SwipeMenuCreator { menu ->
            // create "delete" item
            val deleteItem = SwipeMenuItem(applicationContext)
            // set item background
            deleteItem.background = ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25))
            // set item width
            deleteItem.width = dp2px(60, this@MemberAcitivy)
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete)

            // create "esit" item
            val editItem = SwipeMenuItem(applicationContext)
            editItem.background = ColorDrawable(Color.parseColor("#00a968"))
            editItem.width = dp2px(60, this@MemberAcitivy)
            editItem.setIcon(R.drawable.ic_edit)

            // add to menu
            menu.addMenuItem(editItem)
            menu.addMenuItem(deleteItem)
        }

        list_people.setOnMenuItemClickListener { position, menu, index ->
            when (index) {
                0 -> showUpdateMemberDialog(this@MemberAcitivy, position)
                1 -> showDeleteMemberDialog(this@MemberAcitivy, position)
            }// open

            // false : close the menu; true : not close the menu
            false
        }

        // set creator
        list_people.setMenuCreator(creator)
    }

    private fun showDeleteMemberDialog(context: Context, position: Int) {
        var member = memberList[position]

        val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.delete_member_title))
                .setMessage(getString(R.string.delete_confirm_member) + ", ${member.name}")
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(android.R.string.yes){
                    dialog, which ->
                    deleteMember(mMemberReference!!, member.uuid)
                    dialog.dismiss()
                }

        dialog.show()
    }

    private fun showUpdateMemberDialog(context: Context, position: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_new_member, null)
        val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.add_member))
                .setIcon(R.drawable.ic_add_person_dialog)
                .setView(view)
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
                        addMember(db, newMember)
                    }

                    if (isValid) {
                        dialog.dismiss()
                    }
                }

        dialog.show()
    }

    private fun showAddMemberDialog(context: Context) {

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_new_member, null)
        val dialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.add_member))
            .setIcon(R.drawable.ic_add_person_dialog)
            .setView(view)
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
                adapter!!.add(member.name)

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
