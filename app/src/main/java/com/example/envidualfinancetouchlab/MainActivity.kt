package com.example.envidualfinancetouchlab

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.envidual.finance.touchlab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import fragments.FavouritesFragment
import fragments.SearchesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.title = "Favourites"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
    }
}
