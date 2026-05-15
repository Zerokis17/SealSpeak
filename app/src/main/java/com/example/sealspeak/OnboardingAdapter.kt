package com.example.sealspeak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class OnboardingAdapter(private val activity: OnboardingActivity) :
    RecyclerView.Adapter<OnboardingAdapter.PageViewHolder>() {

    private val pages = listOf(
        R.layout.onboarding_page_nickname,
        R.layout.onboarding_page_level,
        R.layout.onboarding_page_time,
        R.layout.onboarding_page_reason
    )

    inner class PageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(pages[viewType], parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        // Las vistas se configuran desde los layouts, no necesitamos lógica aquí
    }

    override fun getItemViewType(position: Int) = position
    override fun getItemCount() = pages.size

    fun isAnswered(position: Int): Boolean {
        return when (position) {
            0 -> {
                val et = activity.findViewById<TextInputEditText>(R.id.etNickname)
                val value = et?.text.toString().trim()
                if (value.isNotEmpty()) activity.nickname = value
                value.isNotEmpty()
            }

            1 -> {
                val rg = activity.findViewById<RadioGroup>(R.id.rgLevel)
                val checked = rg?.checkedRadioButtonId ?: -1
                if (checked != -1) {
                    activity.level = when (checked) {
                        R.id.rbLevel1 -> "Nunca he estudiado"
                        R.id.rbLevel2 -> "Conozco lo básico"
                        R.id.rbLevel3 -> "Puedo mantener una conversación"
                        R.id.rbLevel4 -> "Soy casi fluido"
                        else -> ""
                    }
                }
                checked != -1
            }

            2 -> {
                val rg = activity.findViewById<RadioGroup>(R.id.rgTime)
                val checked = rg?.checkedRadioButtonId ?: -1
                if (checked != -1) {
                    activity.dailyTime = when (checked) {
                        R.id.rbTime1 -> "5 min"
                        R.id.rbTime2 -> "10 min"
                        R.id.rbTime3 -> "15 min"
                        R.id.rbTime4 -> "30 min"
                        else -> ""
                    }
                }
                checked != -1
            }

            3 -> {
                val rg = activity.findViewById<RadioGroup>(R.id.rgReason)
                val checked = rg?.checkedRadioButtonId ?: -1
                if (checked != -1) {
                    activity.reason = when (checked) {
                        R.id.rbReason1 -> "Trabajo"
                        R.id.rbReason2 -> "Viajes"
                        R.id.rbReason3 -> "Estudios"
                        R.id.rbReason4 -> "Entretenimiento"
                        else -> ""
                    }
                }
                checked != -1
            }

            else -> false
        }
    }

    fun saveAnswer(position: Int) {}
}