package com.edu.hust.henry.cashcal

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.edu.hust.henry.cashcal.model.OrderData
import com.edu.hust.henry.cashcal.module.Info
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_order.*
import java.util.*
import kotlin.math.cos

class AddOrderActivity : AppCompatActivity() {

    private val HUY = 0
    private val DUONG = 1
    private val KHIEM = 2
    private val KHANH = 3
//    private val NGUYEN = 4

    private var orderDB: DatabaseReference

    private val c = Calendar.getInstance()
    private var year = c.get(Calendar.YEAR)
    private var month = c.get(Calendar.MONTH)
    private var day = c.get(Calendar.DAY_OF_MONTH)
    private var week = c.get(Calendar.WEEK_OF_YEAR)
    private var dayOfWeek = c.get(Calendar.DAY_OF_WEEK)

    init{
        orderDB = FirebaseDatabase.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        setSupportActionBar(toolbar_add_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_add_order)

        txt_date.text = setCurrentDate()

        txt_date.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                view, _year, monthOfYear, dayOfMonth ->
                run {
                    // Display Selected date in textbox
                    txt_date.text = "$dayOfMonth/${monthOfYear + 1}/$_year"
                    c.set(_year, monthOfYear, dayOfMonth)
                    dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
                    week = c.get(Calendar.WEEK_OF_YEAR)
                    year = _year
                }
            }, year, month, day)

            dpd.show()
        }
    }

    private fun setCurrentDate(): String{
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

    private fun saveOrder(){
        val cost: Int? = input_cost.text.toString().toIntOrNull()
        val agentId: Int = spinner_agent.selectedItemPosition
        val partnerId: List<Boolean> = listOf(chbHuy.isChecked, chbDuong.isChecked, chbKhiem.isChecked, chbKhanh.isChecked)
        val note: String = input_note.text.toString()
        val intent: Intent = Intent()

        if(cost == null) return

        val costs = calCost(cost, agentId, partnerId)
        val data = OrderData(dayOfWeek, costs, note)

        data.uuid = orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().key
        orderDB.child("order").child("$year").child("$week").child("$dayOfWeek").push().setValue(data)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun calCost(cost: Int, agentId: Int, partnerId: List<Boolean>): MutableList<Int>{
        val n: Int = partnerId.filter { b -> b }.size
        val avg: Int = cost/n
        val costs: MutableList<Int> = mutableListOf()

        for (i in 0..(partnerId.size - 1)){
            if(partnerId[i]){
                if(i == agentId){
                    costs.add(i, cost - avg)
                } else{
                    costs.add(i, -avg)
                }
            }else{
                costs.add(i, 0)
            }
        }

        return costs
    }
}
