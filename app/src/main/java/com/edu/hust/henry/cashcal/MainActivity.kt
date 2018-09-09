package com.edu.hust.henry.cashcal

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.edu.hust.henry.cashcal.module.Info
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadLocale()

        val arrayInfo: ArrayList<Info> = ArrayList()
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

        // set creator
        list.setMenuCreator(creator)

        fab_add_order.setOnClickListener {
            val intent = Intent(this@MainActivity, AddOrderActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(toolbar_main)
    }

    /**
     * @description: inflate menu from xml file and add spinner
     * @param: menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Week Spinner
        val item: MenuItem = menu!!.findItem(R.id.spinner_week)
        val spinnerWeek: Spinner = item.actionView as Spinner
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.weeks, android.R.layout.simple_spinner_item)

        val c = Calendar.getInstance()
        val week = c.get(Calendar.WEEK_OF_YEAR)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWeek.adapter = adapter
        spinnerWeek.setSelection(week-1)
        spinnerWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@MainActivity, "Position is $position", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MainActivity, "Week is $week", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    /**
     * @description: handle when menu item is clicked
     * @param: menu item
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int? = item?.itemId

        when(id){
            R.id.menu_item_language -> onChangeLang()
            R.id.menu_item_about -> onAbout()
            R.id.menu_item_exit -> System.exit(0)
            else -> Toast.makeText(this, "Invalid Choose", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * @description: handle change language
     */
    private fun onChangeLang(){
        val items = arrayOf<CharSequence>("English", "Tiếng Việt")
        val dialog = AlertDialog.Builder(this).setTitle(getString(R.string.change_lang)).setItems(items
        ) { dialog, which ->
            run {
                when (which) {
                    0 -> {
                        setLocale("en")
                        recreate()
                    }
                    1 -> {
                        setLocale("vi")
                        recreate()
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    /**
     * @description: Change locale to change language
     * @param: language name
     */
    private fun setLocale(lang: String) {
        val locale  = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        baseContext.createConfigurationContext(configuration)

        // Save data to share preference
        val editor: SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    /**
     * @description: Load Language saved to shared preferences
     */
    fun loadLocale(){
        val preferences: SharedPreferences  = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language: String  = preferences.getString("My_Lang", "")
        setLocale(language)
    }

    /**
     * @return display about dialog
     */
    private fun onAbout(){
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        builder.setTitle(getString(R.string.about))

        // Display a message on alert dialog
        builder.setMessage(getString(R.string.about_msg))
        builder.setCancelable(true)
        builder.setNegativeButton(getString(R.string.cancel)){ _, _ -> }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                resources.displayMetrics).toInt()
    }
}
