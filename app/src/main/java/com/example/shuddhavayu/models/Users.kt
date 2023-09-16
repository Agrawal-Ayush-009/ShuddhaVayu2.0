package com.example.shuddhavayu.models

data class Users(
    val uID: String = "",
    val displayName: String = "",
    val imageUrl: String = "",
    val device_data: HashMap<String, Double> = HashMap<String, Double>(),
    val deviceID: String = "",
    val userType: String = ""
)
