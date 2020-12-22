package com.example.breakingnewsappguardianapi

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingnewsappguardianapi.MVVM.Repository
import com.example.breakingnewsappguardianapi.MVVM.ViewModel
import com.example.breakingnewsappguardianapi.MVVM.ViewModelFactory
import com.example.breakingnewsappguardianapi.data.CONSTS
import com.example.breakingnewsappguardianapi.recyclerview.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = RecyclerAdapter()
    private var pageSize = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vmp by lazy { ViewModelProvider(this, ViewModelFactory(Repository())).get(ViewModel::class.java) }

        rvview.layoutManager = LinearLayoutManager(this)
        readDataVM(getSPrefs())


        var isScrolling = false

        val sclis = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val itemVisible = lm.findFirstVisibleItemPosition()
                val visibleItems = lm.childCount
                val allItems = lm.itemCount

                val atLastItem = itemVisible + visibleItems >= allItems
                val notatBgn = itemVisible >= 0
                val totalmore = allItems >= 10
                val shouldPaginate = atLastItem && notatBgn && totalmore && isScrolling
                if (shouldPaginate) {
                    pageSize += 10
                    readDataVM(getSPrefs())
                    isScrolling = false
                }
            }
        }

        rvview.addOnScrollListener(sclis)

        vmp.data.observe(this, Observer {
            val saved = (rvview.layoutManager as LinearLayoutManager).onSaveInstanceState()
            rvview.adapter = adapter
            adapter.submitData(it.response.results)
            pbar.visibility = View.GONE
            (rvview.layoutManager as LinearLayoutManager).onRestoreInstanceState(saved)
        })

    }

    fun readDataVM(category: Int){
        pbar.visibility = View.VISIBLE
        val vmp by lazy { ViewModelProviders.of(this, ViewModelFactory(Repository())).get(ViewModel::class.java) }

        adapter.submitData(emptyList())
        val internet = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (internet.activeNetworkInfo == null)
        {
            Toast.makeText(this, "No Internet Accessed!", Toast.LENGTH_LONG).show()
        }

        if (category < 1 || category > 11) vmp.readData(pageSize)
        else vmp.readDataCategory(CONSTS.categoryItems[category].replace("\\s".toRegex(), ""), pageSize)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            0 -> {
                BuildAD()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun BuildAD(){
        var selectedItem : Int = getSPrefs()
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Check category!")
            setSingleChoiceItems(CONSTS.categoryItems, getSPrefs()) { _, which ->
                selectedItem = which
            }
            setPositiveButton("Accept!") { _, _ ->
                pageSize = 10
                savedSPrefs(selectedItem)
                readDataVM(selectedItem)
            }
        }
        val builder = dialog.create()
        builder.show()
    }

    private fun savedSPrefs(selectedItem : Int){
        val prefs = getSharedPreferences("GuardianAPI", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("selectedItem", selectedItem)
        editor.apply()
    }

    private fun getSPrefs() : Int{
        val prefs = getSharedPreferences("GuardianAPI", MODE_PRIVATE)
        return prefs.getInt("selectedItem", 0)
    }
}