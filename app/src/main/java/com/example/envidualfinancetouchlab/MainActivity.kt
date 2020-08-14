package com.example.envidualfinancetouchlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.envidual.finance.touchlab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import fragments.FavouritesFragment
import fragments.SearchesFragment

class MainActivity : AppCompatActivity(){

    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FavouritesFragment()).commit()

        bottomNavigation = findViewById(R.id.bottomNavigationView)
        setupBottomNavigation()
    }


    private fun setupBottomNavigation(){
        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){
                R.id.search -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    SearchesFragment()
                ).commit()
                else -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    FavouritesFragment()
                ).commit()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }
}
