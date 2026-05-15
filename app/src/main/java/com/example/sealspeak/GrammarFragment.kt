package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentGrammarBinding
import com.google.firebase.firestore.FirebaseFirestore

class GrammarFragment : Fragment() {

    private var _binding: FragmentGrammarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGrammarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessons = mutableListOf<GrammarLesson>()
        val adapter = GrammarAdapter(lessons) { lesson ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFragment, GrammarDetailFragment.newInstance(lesson))
                .addToBackStack(null)
                .commit()
        }

        binding.rvGrammar.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGrammar.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("grammar")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val lesson = GrammarLesson(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        level = doc.getString("level") ?: "",
                        explanation = doc.getString("explanation") ?: "",
                        structure = doc.getString("structure") ?: "",
                        examples = doc.get("examples") as? List<String> ?: emptyList()
                    )
                    lessons.add(lesson)
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}