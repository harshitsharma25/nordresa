package com.nordresa.travel.models

data class User(
    val id : String = "",
    val name : String = "",
    val email : String = "",
    val image : String? = "",  // user's image
    val mobile : Long? =  0,
)
