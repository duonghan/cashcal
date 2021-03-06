package com.edu.hust.henry.cashcal

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.edu.hust.henry.cashcal.helpers.FirebaseDBHelper.fetchOrderData
import com.edu.hust.henry.cashcal.helpers.LocaleHelper
import com.edu.hust.henry.cashcal.helpers.Utils.dp2px
import com.edu.hust.henry.cashcal.model.Info
import com.edu.hust.henry.cashcal.modules.AddOrderActivity
import com.edu.hust.henry.cashcal.modules.MemberAcitivy
import com.edu.hust.henry.cashcal.modules.MyCustomAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


const val ADD_ORDER  = 12345

class MainActivity : AppCompatActivity() {

    val localeHelper: LocaleHelper = LocaleHelper()
    private var db: DatabaseReference = FirebaseDatabase.getInstance().reference

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
            openItem.width = dp2px(90, this@MainActivity)
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
            deleteItem.width = dp2px(90, this@MainActivity)
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
            startActivityForResult(intent, ADD_ORDER)
        }

        setSupportActionBar(toolbar_main)

        val orderList = fetchOrderData(db, this@MainActivity,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.WEEK_OF_YEAR))

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
            R.id.menu_item_setting -> onSetting()
            R.id.menu_options_add -> showMember()
            else -> Toast.makeText(this, "Invalid Choose", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * @description: Open member information activity
     */
    private fun showMember() {
        val intent = Intent(this@MainActivity, MemberAcitivy::class.java)
        startActivity(intent)
    }

    private fun onSetting() {
        Toast.makeText(this@MainActivity, "Settings", Toast.LENGTH_SHORT).show()
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
                        localeHelper.setLocale(this@MainActivity, "en")
                        this@MainActivity.recreate()
                    }
                    1 -> {
                        localeHelper.setLocale(this@MainActivity, "vi")
                        this@MainActivity.recreate()
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
        val view = layoutInflater.inflate(R.layout.dialog_about, null)
        val dialog = AlertDialog.Builder(this@MainActivity)
                .setTitle(getString(R.string.about))
                .setIcon(R.drawable.ic_about)
                .setView(view)
                .setCancelable(true)
                .setNegativeButton(getString(R.string.cancel)){ _, _ -> }

        view.setOnClickListener {
            when(view.id){
                R.id.ic_about_share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share subject")
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Cash manager application")
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.txt_about_share_title)))
                }
                R.id.ic_about_facebook -> {
                    var fb: Intent
                    try {
                        packageManager.getPackageInfo("com.facebook.katana", 0) //Checks if FB is even installed.
                        fb = Intent(Intent.ACTION_VIEW,
                                Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/0x0fa"))
                                //Try to make intent with FB's URI
                    } catch (e: Exception) {
                        fb = Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.facebook.com/0x0fa")) //catches and opens a url to the desired page
                    }

                    startActivity(fb)
                }
                R.id.ic_about_twitter -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW,
                                Uri.parse("twitter://user?screen_name=minh_nhat314"))
                        startActivity(intent)
                    } catch (e: Exception) {
                        startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://twitter.com/#!/minh_nhat314")))
                    }

                }
                R.id.ic_about_bbm -> {
                    val dialog = Dialog(this)
                    dialog.setTitle(R.string.txt_title_header_bbm)
                    dialog.setContentView(R.layout.dialog_show_bbm)
                    dialog.show()
                }
                R.id.ic_about_ggplus -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setClassName("com.google.android.apps.plus",
                                "com.google.android.apps.plus.phone.UrlGatewayActivity")
                        intent.putExtra("customAppUri", "102540245952570533629")
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/102540245952570533629/posts")))
                    }

                }
            }
        }

        // Display the alert dialog on app interface
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_ORDER && resultCode == Activity.RESULT_OK){
            Toast.makeText(this@MainActivity, "Saved succeed!! $data", Toast.LENGTH_SHORT).show()
        }
    }

}
