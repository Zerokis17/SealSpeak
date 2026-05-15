package com.example.sealspeak

data class VocabularyCategory(
    val id: String = "",
    val category: String = "",
    val words: List<Map<String, String>> = emptyList()
)