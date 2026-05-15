package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentVocabularyDetailBinding

class VocabularyDetailFragment : Fragment() {

    companion object {
        fun newInstance(category: VocabularyCategory): VocabularyDetailFragment {
            val fragment = VocabularyDetailFragment()
            val args = Bundle()
            args.putString("category", category.category)
            val enList = ArrayList(category.words.map { it["en"] ?: "" })
            val esList = ArrayList(category.words.map { it["es"] ?: "" })
            args.putStringArrayList("en", enList)
            args.putStringArrayList("es", esList)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentVocabularyDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVocabularyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvCategory.text = arguments?.getString("category")

        val enList = arguments?.getStringArrayList("en") ?: arrayListOf()
        val esList = arguments?.getStringArrayList("es") ?: arrayListOf()

        val words = enList.zip(esList).map { mapOf("en" to it.first, "es" to it.second) }

        val adapter = WordCardAdapter(words)
        binding.rvWords.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWords.adapter = adapter
        PointsManager.addPoints(5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}