package com.example.influxapp

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class Edit_savings : AppCompatActivity() {

    private lateinit var eName: TextView
    private lateinit var eType: TextView
    private lateinit var eAmount: TextView
    private lateinit var eDate: DatePicker
    private var recordId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_savings)

        // Initialize the TextViews
        eName = findViewById(R.id.e_name)
        eType = findViewById(R.id.e_type)
        eAmount = findViewById(R.id.e_amount)
        eDate = findViewById(R.id.e_datePickerButton)

        // Initialize the Buttons
        val submitButton = findViewById<Button>(R.id.submit)
        val cancelButton = findViewById<Button>(R.id.cancel)

        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance().reference

        // Query the database to retrieve the last row of data
        database.child("savings/goals").orderByKey().limitToLast(1).get()
            .addOnSuccessListener { dataSnapshot ->
                // Update the values of that row with the new data
                val lastChild = dataSnapshot.children.iterator().next()

                recordId = lastChild.key
                // Get the values of the child nodes and convert them to strings
                val nameEdit = lastChild.child("name").value?.toString()
                val editType = lastChild.child("type").value?.toString()
                val amount = lastChild.child("amount").value?.toString()
                val date = lastChild.child("date").value?.toString()

                // Set the values of the TextViews
                eName.text = nameEdit
                eType.text = editType
                eAmount.text = amount


                // Set up the submit button onClick listener
                submitButton.setOnClickListener {
                    // Get the updated input values
                    val name = eName.text.toString()
                    val type = eType.text.toString()
                    val amount = eAmount.text.toString()
                    val date = "${eDate.month}/${eDate.dayOfMonth}/${eDate.year}"

                    // Update the income record with the new values
                    lastChild.ref.updateChildren(
                        mapOf(
                            "name" to name,
                            "type" to type,
                            "amount" to amount,
                            "date" to date
                        )
                    )
                    // Show a toast message indicating that the record was updated
                    Toast.makeText(this@Edit_savings, "Record updated successfully", Toast.LENGTH_SHORT).show()
                    // Finish the activity and return to the previous screen
                    finish()
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }
    }
}
