package com.nunuvero.adrian.laboratoriocalificado03nunuvero

data class ApiResponse(
    val teachers: List<Profesor>
)

data class Profesor(
    val name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val imageUrl: String
)