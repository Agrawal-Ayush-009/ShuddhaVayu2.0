package com.example.shuddhavayu.models

data class UserReports(
    val id: Int = 0,
    val image: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val user: Users = Users(),
    val text: String = "",
    val value: Int = 0,
    val address: String,
    val date: String,
    val time: String
)
