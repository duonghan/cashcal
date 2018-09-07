package com.edu.hust.henry.cashcal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.edu.hust.henry.cashcal.module.Info
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var subjectName: List<String> = listOf("IOS", "Android", "React", "PHP")
        var arrayInfo: ArrayList<Info> = ArrayList()
        arrayInfo.add(Info("Mon", 21000))
        arrayInfo.add(Info("Tue", 22000))
        arrayInfo.add(Info("Wed", 25000))
        arrayInfo.add(Info("Thu", 20000))
        arrayInfo.add(Info("Fri", 27000))
        arrayInfo.add(Info("Sat", 20000))
        arrayInfo.add(Info("Sun", 26000))

//        list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectName)
        list.adapter = MyCustomAdapter(this@MainActivity, arrayInfo)

        fab_add_order.setOnClickListener {
            var intent: Intent = Intent(this@MainActivity, AddOrderActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(toolbar_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int? = item?.itemId

        when(id){
            R.id.menu_item_language -> Toast.makeText(this, "Language", Toast.LENGTH_SHORT).show()
            R.id.menu_item_about -> Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
            R.id.menu_item_exit -> Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Invalid Choose", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}
