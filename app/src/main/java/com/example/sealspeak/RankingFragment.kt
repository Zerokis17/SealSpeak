package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentRankingBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val users = mutableListOf<RankingUser>()
        val adapter = RankingAdapter(users)

        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                for ((index, doc) in documents.withIndex()) {
                    users.add(
                        RankingUser(
                            position = index + 1,
                            nickname = doc.getString("nickname") ?: "Usuario",
                            points = (doc.getLong("points") ?: 0L).toInt()
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}