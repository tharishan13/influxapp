package com.example.influxapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.influxapp.Model.Goal
import com.google.firebase.database.FirebaseDatabase

class addsavings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsavings)

        val datePicker = findViewById<DatePicker>(R.id.datePickerButton)
        datePicker.setOnClickListener {
            showDatePickerDialog(datePicker)
        }

        // Initialize Firebase Database reference
        val database = FirebaseDatabase.getInstance().getReference("savings")

        // Get references to EditText fields
        val gname = findViewById<EditText>(R.id.gname)
        val gtype = findViewById<EditText>(R.id.gtype)
        val amount = findViewById<EditText>(R.id.amount)

        findViewById<View>(R.id.submit).setOnClickListener {
            // Create a new goal object with data from EditText fields
            val goal = Goal(
                gname.text.toString(),
                gtype.text.toString(),
                amount.text.toString().toDouble(),
                datePicker.year.toString() + "-" + (datePicker.month + 1).toString() + "-" + datePicker.dayOfMonth.toString()
            )

            // Generate a unique key for the new goal
            val key = database.child("goals").push().key

            // Insert the new goal into the database
            if (key != null) {
                database.child("goals").child(key).setValue(goal)
                    .addOnSuccessListener {
                        // Insertion successful
                        Toast.makeText(this, "Goal added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Readsavings::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // Insertion failed
                        Toast.makeText(this, "Failed to add goal", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun showDatePickerDialog(datePicker: DatePicker) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                datePicker.updateDate(year, month, dayOfMonth)
            },  // Set the default date in the dialog
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth
        )
        datePickerDialog.show()
    }

}
