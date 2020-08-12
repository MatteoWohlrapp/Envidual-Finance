package com.example.envidualfinancetouchlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.envidual.finance.touchlab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import fragments.ExploreFragment
import fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*
import viewmodel.CompanyDataViewModel

class MainActivity : AppCompatActivity(){

    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ExploreFragment()).commit()

        bottomNavigation = findViewById(R.id.bottomNavigationView)
        setupBottomNavigation()
    }


    private fun setupBottomNavigation(){
        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){
                R.id.search -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    SearchFragment()
                ).commit()
                else -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    ExploreFragment()
                ).commit()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }
}
