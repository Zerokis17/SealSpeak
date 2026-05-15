package com.example.sealspeak

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object PointsManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun addPoints(points: Int) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("points", FieldValue.increment(points.toLong()))
    }
}