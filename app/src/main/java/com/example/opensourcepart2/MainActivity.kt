package com.example.opensourcepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rootView = findViewById<RelativeLayout>(R.id.homeLayout)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Filter fields and button
        val editFilterStart = findViewById<EditText>(R.id.editFilterStart)
        val editFilterEnd = findViewById<EditText>(R.id.editFilterEnd)
        val btnViewExpenses = findViewById<Button>(R.id.btnViewExpenses)
        val textFilteredResults = findViewById<TextView>(R.id.textFilteredResuls)

        btnViewExpenses.setOnClickListener {
            val startDate = editFilterStart.text.toString()
            val endDate = editFilterEnd.text.toString()

            // Logic to filter expenses based on the dates (you can customize this logic as needed)
            val filteredData = filterExpenses(startDate, endDate)

            // Update filtered results display
            textFilteredResults.visibility = TextView.VISIBLE
            textFilteredResults.text = filteredData
        }

        // Floating Action Button to open ExpenseManagerActivity
        val fab = findViewById<FloatingActionButton>(R.id.fabMain)
        fab.setOnClickListener {
            val intent = Intent(this, ExpenseManagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterExpenses(startDate: String, endDate: String): String {
        // Here, you'd implement your filtering logic based on the start and end date.
        // For now, this will just return a dummy result.
        return "Showing results for: $startDate to $endDate"
    }
}
