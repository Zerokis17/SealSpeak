package com.example.sealspeak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sealspeak.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Login con email y contraseña
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkOnboardingAndNavigate()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }

        // Login con Google
        binding.btnGoogle.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
            }
        }

        // Ir a Register
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            checkOnboardingAndNavigate()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${t.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In falló: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkOnboardingAndNavigate() {
        val userId = auth.currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users").document(userId)
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
    }
}