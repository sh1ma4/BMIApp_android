package com.example.bmiapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.bmiapp.ui.history.HistoryFragment
import com.example.bmiapp.ui.input.InputFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_input -> {
                    title = R.string.title_input.toString()
                    supportFragmentManager.beginTransaction()
                                          .replace(R.id.nav_host_fragment, InputFragment())
                                          .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_history -> {
                    title = R.string.title_history.toString()
                    supportFragmentManager.beginTransaction()
                                          .replace(R.id.nav_host_fragment, HistoryFragment())
                                          .commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}
