package com.example.envidualfinancetouchlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.envidual.finance.touchlab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import fragments.FavouritesFragment
import fragments.SearchesFragment

class MainActivity : AppCompatActivity(){

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FavouritesFragment()).commit()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
    }


//    private fun setupBottomNavigation(){
//        bottomNavigation.setOnNavigationItemSelectedListener { item ->
//
//            when(item.itemId){
//                R.id.search -> supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,
//                    SearchesFragment()
//                )?.addToBackStack("root").commit()
//                else -> supportFragmentManager?.popBackStack()
//            }
//
//            return@setOnNavigationItemSelectedListener true
//        }
//    }
}
