package com.nunuvero.adrian.laboratoriocalificado03nunuvero

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nunuvero.adrian.laboratoriocalificado03nunuvero.databinding.ActivityEjercicio01Binding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Ejercicio01 : AppCompatActivity() {

    private lateinit var binding: ActivityEjercicio01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjercicio01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        retrieveProfesoresData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun retrieveProfesoresData() {
        val apiService = createApiService()

        apiService.getProfesores().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                handleApiResponse(response)
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error de conexión: ${t.message}", t)
                showToast(getString(R.string.error_conexion, t.message ?: "Desconocido"))
            }
        })
    }

    private fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://private-effe28-tecsup1.apiary-mock.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun handleApiResponse(response: Response<ApiResponse>) {
        if (response.isSuccessful) {
            val profesores = response.body()?.teachers ?: emptyList()
            Log.d("API_RESPONSE", "Profesores recibidos: $profesores")

            if (profesores.isNotEmpty()) {
                binding.recyclerView.adapter = ProfesorAdapter(this@Ejercicio01, profesores)
            } else {
                showToast(getString(R.string.no_profesores))
            }
        } else {
            Log.e("API_ERROR", "Error al obtener los datos: ${response.code()}")
            showToast(getString(R.string.error_datos, response.code()))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

