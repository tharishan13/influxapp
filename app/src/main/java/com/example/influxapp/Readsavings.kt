package com.example.influxapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase


class Readsavings : AppCompatActivity() {

    private lateinit var goalNameTextView: TextView
    private lateinit var goalTypeTextView: TextView
    private lateinit var goalAmountTextView: TextView
    private lateinit var goalDateTextView: TextView
    private lateinit var recordId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readsavings)

        goalNameTextView = findViewById(R.id.r_name)
        goalTypeTextView = findViewById(R.id.r_type)
        goalAmountTextView = findViewById(R.id.r_amount)
        goalDateTextView = findViewById(R.id.r_date)

        // Initialize Firebase Database reference
        val database = FirebaseDatabase.getInstance().getReference("savings/goals")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firstChild = dataSnapshot.children.last() // get the first child node
                recordId = firstChild.key.toString() // store the record ID as a class-level variable
                val name = firstChild.child("name").value?.toString()
                val type = firstChild.child("type").value?.toString()
                val amount = firstChild.child("amount").value?.toString()
                val date = firstChild.child("datePicker").value?.toString()

                goalNameTextView.text = name
                goalTypeTextView.text = type
                goalAmountTextView.text = amount
                goalDateTextView.text = date.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        val deleteButton: Button = findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("savings/goals")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val firstChild = dataSnapshot.children.lastOrNull() // get the first child node
                    val recordId = firstChild?.key // get the record ID

                    if (recordId != null) {
                        val recordReference = databaseReference.child(recordId)

                        Log.d("DeleteSavings", "Deleting record with ID: $recordId")

                        // Remove the record from Firebase
                        recordReference.removeValue()
                    }

                    val intent = Intent(this@Readsavings, Readsavings::class.java)
                    startActivity(intent)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException())
                }
            })
        }

        val editButton: Button = findViewById(R.id.edit)
        editButton.setOnClickListener {
            val intent = Intent(this@Readsavings, Edit_savings::class.java)
            intent.putExtra("recordId", recordId)
            startActivity(intent)
        }
    }
}
