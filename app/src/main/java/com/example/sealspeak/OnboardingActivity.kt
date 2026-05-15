package com.example.sealspeak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sealspeak.databinding.ActivityOnboardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var nickname = ""
    var level = ""
    var dailyTime = ""
    var reason = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // deshabilita deslizar con dedo

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            val total = adapter.itemCount

            // Validar que respondió antes de avanzar
            if (!adapter.isAnswered(current)) {
                Toast.makeText(this, "Por favor responde antes de continuar", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Guardar respuesta
            adapter.saveAnswer(current)

            if (current < total - 1) {
                binding.viewPager.currentItem = current + 1
                // Cambiar texto del botón en la última pregunta
                if (current + 1 == total - 1) {
                    binding.btnNext.text = "¡Empecemos!"
                }
            } else {
                saveToFirestore()
            }
        }
    }

    private fun saveToFirestore() {
        val userId = auth.currentUser?.uid ?: return

        val userProfile = hashMapOf(
            "nickname" to nickname,
            "level" to level,
            "dailyTime" to dailyTime,
            "reason" to reason,
            "onboardingCompleted" to true,
            "points" to 0
        )

        db.collection("users").document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}