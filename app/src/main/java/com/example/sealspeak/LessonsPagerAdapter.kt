package com.example.sealspeak

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LessonsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GrammarFragment()
            1 -> VocabularyFragment()
            2 -> VideoFragment()
            else -> GrammarFragment()
        }
    }
}