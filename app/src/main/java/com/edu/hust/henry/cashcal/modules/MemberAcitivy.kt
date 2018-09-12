package com.edu.hust.henry.cashcal.modules

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.dialog_new_member.*

class MemberAcitivy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)
        handleList()
        fab_add_person.setOnClickListener {
            showCreateCategoryDialog()
        }
    }

    private fun handleList(){
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

        val listItems = listOf("Huy", "Dương", "Khiêm", "Khánh", "Nguyên")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
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

    private fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_member))
        builder.setIcon(R.drawable.ic_add_person_dialog)

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_new_member, null)

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newMember = edt_add_person.text
            var isValid = true
            if (newMember.isBlank()) {
                edt_add_person.error = getString(R.string.validation_empty)
                isValid = false
            }

            if (isValid) {
                val db: DatabaseReference = FirebaseDatabase.getInstance().reference
                addMember(db, edt_add_person.text.toString())
            }

            if (isValid) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}
