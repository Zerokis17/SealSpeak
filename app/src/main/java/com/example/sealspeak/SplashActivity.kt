package com.example.sealspeak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        android.os.Handler(mainLooper).postDelayed({
            if (user != null) {
                // Verificar si ya completó el onboarding
                db.collection("users").document(user.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists() && document.getBoolean("onboardingCompleted") == true) {
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            startActivity(Intent(this, OnboardingActivity::class.java))
                        }
                        finish()
                    }
                    .addOnFailureListener {
                        startActivity(Intent(this, OnboardingActivity::class.java))
                        finish()
                    }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2500)
    }
}