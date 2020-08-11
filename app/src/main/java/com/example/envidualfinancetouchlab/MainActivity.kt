package com.example.envidualfinancetouchlab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.envidual.finance.touchlab.R
import viewmodel.StockDataViewModel

class MainActivity : AppCompatActivity() {

    lateinit var stockDataViewModel: StockDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stockDataViewModel = ViewModelProviders.of(this).get(StockDataViewModel::class.java)
        stockDataViewModel.getIntraDay()
    }
}
