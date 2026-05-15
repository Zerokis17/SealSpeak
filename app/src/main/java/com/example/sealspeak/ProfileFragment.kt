package com.example.sealspeak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sealspeak.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        // Foto de perfil
        if (user?.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop()
                .into(binding.ivProfile)
        } else {
            binding.ivProfile.setImageResource(R.drawable.ic_profile)
        }

        // Email
        binding.tvEmail.text = user?.email ?: "Sin correo"

        // Datos de Firestore
        user?.uid?.let { uid ->
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    binding.tvNickname.text = document.getString("nickname") ?: "Usuario"
                    binding.tvLevel.text = "Nivel: ${document.getString("level") ?: ""}"
                    binding.tvDailyTime.text = "Meta diaria: ${document.getString("dailyTime") ?: ""}"
                    binding.tvReason.text = "Objetivo: ${document.getString("reason") ?: ""}"
                }
        }

        // Cerrar sesión
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), SplashActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}