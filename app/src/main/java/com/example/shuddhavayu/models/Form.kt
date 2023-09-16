package com.example.shuddhavayu.models

data class Form (
    val user: Users = Users(),
    val name: String = "",
    val DOB: String = "",
    val location: String = "",
    val email: String = ""
    )
