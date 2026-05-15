package com.example.sealspeak

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sealspeak.databinding.ActivityVideoLessonBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants

class VideoLessonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoLessonBinding
    private var currentQuestion: String = ""
    private var currentOptions: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val lessonId = intent.getStringExtra("lessonId")

        if (lessonId != null) {
            loadLesson(lessonId)
        } else {
            Log.e("VIDEO_DEBUG", "lessonId es null")
        }
    }

    private fun loadLesson(lessonId: String) {
        FirebaseFirestore.getInstance()
            .collection("lessons")
            .document(lessonId)
            .get()
            .addOnSuccessListener { document ->

                val videoId = document.getString("videoId")
                if (videoId.isNullOrEmpty()) {
                    Log.e("VIDEO_DEBUG", "videoId null")
                    return@addOnSuccessListener
                }

                currentQuestion = document.getString("question") ?: ""

                val rawOptions = document.get("options") as? List<*>
                currentOptions = rawOptions?.mapNotNull {
                    it as? Map<String, String>
                } ?: emptyList()

                binding.tvTitle.text = document.getString("title")

                setupPlayer(videoId)
            }
    }

    private fun setupPlayer(videoId: String) {
        val playerView = binding.youtubePlayerView
        lifecycle.addObserver(playerView)

        playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.ENDED) {
                    showOptions()
                }
            }
        })
    }

    private fun showOptions() {
        PointsManager.addPoints(15)

        binding.tvQuestion.visibility = View.VISIBLE
        binding.btnOption1.visibility = View.VISIBLE

        binding.tvQuestion.text = currentQuestion

        if (currentOptions.isNotEmpty()) {
            binding.btnOption1.text = currentOptions[0]["text"]
            binding.btnOption1.setOnClickListener {
                val nextId = currentOptions[0]["nextVideoId"] ?: return@setOnClickListener
                loadNextLesson(nextId)
            }
        }

        if (currentOptions.size > 1) {
            binding.btnOption2.visibility = View.VISIBLE
            binding.btnOption2.text = currentOptions[1]["text"]
            binding.btnOption2.setOnClickListener {
                val nextId = currentOptions[1]["nextVideoId"] ?: return@setOnClickListener
                loadNextLesson(nextId)
            }
        }
    }

    private fun loadNextLesson(nextVideoId: String) {
        binding.tvQuestion.visibility = View.GONE
        binding.btnOption1.visibility = View.GONE
        binding.btnOption2.visibility = View.GONE

        FirebaseFirestore.getInstance()
            .collection("lessons")
            .whereEqualTo("videoId", nextVideoId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    loadLesson(documents.documents[0].id)
                }
            }
    }
}