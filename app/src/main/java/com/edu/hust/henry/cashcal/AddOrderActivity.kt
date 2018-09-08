package com.edu.hust.henry.cashcal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_order.*

class AddOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        setSupportActionBar(toolbar_add_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int? = item?.itemId

        when(id){
            R.id.menu_item_save -> Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show()
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
