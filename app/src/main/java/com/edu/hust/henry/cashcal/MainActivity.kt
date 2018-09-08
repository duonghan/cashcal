package com.edu.hust.henry.cashcal

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
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

        list.adapter = MyCustomAdapter(this@MainActivity, arrayInfo)

        val creator = SwipeMenuCreator { menu ->
            // create "open" item
            val openItem = SwipeMenuItem(
                    applicationContext)
            // set item background
            openItem.background = ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE))
            // set item width
            openItem.width = dp2px(90)
            // set item title
            openItem.title = "Open"
            // set item title fontsize
            openItem.titleSize = 18
            // set item title font color
            openItem.titleColor = Color.WHITE
            // add to menu
            menu.addMenuItem(openItem)

            // create "delete" item
            val deleteItem = SwipeMenuItem(
                    applicationContext)
            // set item background
            deleteItem.background = ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25))
            // set item width
            deleteItem.width = dp2px(90)
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete)
            // add to menu
            menu.addMenuItem(deleteItem)
        }

        list.setOnMenuItemClickListener { position, menu, index ->
            when (index) {
                0 -> Toast.makeText(this, "Open", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
            }// open
            // delete
            // false : close the menu; true : not close the menu
            false
        }

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

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                resources.displayMetrics).toInt()
    }
}
