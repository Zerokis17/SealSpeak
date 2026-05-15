package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sealspeak.databinding.FragmentGrammarDetailBinding

class GrammarDetailFragment : Fragment() {

    companion object {
        fun newInstance(lesson: GrammarLesson): GrammarDetailFragment {
            val fragment = GrammarDetailFragment()
            val args = Bundle()
            args.putString("title", lesson.title)
            args.putString("level", lesson.level)
            args.putString("explanation", lesson.explanation)
            args.putString("structure", lesson.structure)
            args.putStringArrayList("examples", ArrayList(lesson.examples))
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentGrammarDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGrammarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PointsManager.addPoints(10)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvTitle.text = arguments?.getString("title")
        binding.tvLevel.text = arguments?.getString("level")
        binding.tvExplanation.text = arguments?.getString("explanation")
        binding.tvStructure.text = arguments?.getString("structure")

        val examples = arguments?.getStringArrayList("examples") ?: emptyList<String>()
        binding.tvExamples.text = examples.joinToString("\n") { "• $it" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}