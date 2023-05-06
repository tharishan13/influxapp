package com.example.influxapp.Model

data class Goal(
    val name: String,
    val type: String,
    val amount: Double,
    var datePicker: String
    )
