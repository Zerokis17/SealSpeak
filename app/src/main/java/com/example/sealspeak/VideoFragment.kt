package com.example.sealspeak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentVideoBinding
import com.google.firebase.firestore.FirebaseFirestore

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessons = mutableListOf<VideoLesson>()
        val adapter = VideoLessonAdapter(lessons) { lesson ->
            val intent = Intent(requireContext(), VideoLessonActivity::class.java)
            intent.putExtra("lessonId", lesson.id)
            startActivity(intent)
        }

        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideos.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("lessons")
            .whereEqualTo("level", "Básico")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val question = doc.get("question")?.toString() ?: ""
                    if (question.isNotEmpty()) {
                        lessons.add(
                            VideoLesson(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                level = doc.getString("level") ?: ""
                            )
                        )
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}