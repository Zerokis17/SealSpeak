package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentVocabularyBinding
import com.google.firebase.firestore.FirebaseFirestore

class VocabularyFragment : Fragment() {

    private var _binding: FragmentVocabularyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = mutableListOf<VocabularyCategory>()
        val adapter = VocabularyAdapter(categories) { category ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFragment, VocabularyDetailFragment.newInstance(category))
                .addToBackStack(null)
                .commit()
        }

        binding.rvVocabulary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVocabulary.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("vocabulary")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val wordsList = doc.get("words") as? List<Map<String, String>> ?: emptyList()
                    val category = VocabularyCategory(
                        id = doc.id,
                        category = doc.getString("category") ?: "",
                        words = wordsList
                    )
                    categories.add(category)
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}