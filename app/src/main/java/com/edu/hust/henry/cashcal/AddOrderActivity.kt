package com.edu.hust.henry.cashcal

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_order.*
import java.util.*

class AddOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        setSupportActionBar(toolbar_add_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.title_add_order))

        txt_date.text = setCurrentDate()

        txt_date.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                txt_date.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, year, month, day)

            dpd.show()
        }
    }

    private fun setCurrentDate(): String{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return "$day/${month + 1}/$year"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int? = item?.itemId

        when(id){
            R.id.menu_item_save -> {
                saveOrder()
            }

            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveOrder(): Boolean{
//        orderDB.child("date").setValue(txt_date.text)
        finish()
        return true
    }
}
