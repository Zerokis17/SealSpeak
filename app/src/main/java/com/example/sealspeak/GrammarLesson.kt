package com.example.sealspeak

data class GrammarLesson(
    val id: String = "",
    val title: String = "",
    val level: String = "",
    val explanation: String = "",
    val structure: String = "",
    val examples: List<String> = emptyList()
)