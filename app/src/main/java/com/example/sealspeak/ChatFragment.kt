package com.example.sealspeak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sealspeak.databinding.FragmentChatBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter
    private val client = OkHttpClient()
    private val chatHistory = JSONArray()

    private val systemPrompt = """
        Eres el asistente virtual de SealSpeak, una aplicación móvil para aprender inglés desarrollada por estudiantes de la Universidad de Cundinamarca, extensión Ubaté.
        
        Tu filosofía orientadora es: "Soy LIBRE, AUTÓNOMO Y RESPONSABLE a través del diálogo y la construcción, como ideal regulativo; me dirijo, controlo y dicto mis propias leyes."
        
        SOBRE SEALSPEAK:
        - Es una app Android para aprender inglés de forma interactiva
        - Tiene lecciones de gramática (Present Simple, Past Simple, Future, Adjetivos y Adverbios)
        - Tiene vocabulario por categorías (Colores, Números, Animales, Saludos, Ropa, Comida)
        - Tiene videos interactivos donde el usuario elige el camino de la historia
        - Tiene un ranking de usuarios por puntos
        - Los puntos se ganan completando lecciones: Gramática +10pts, Vocabulario +5pts, Videos +15pts
        - El usuario puede ver su perfil con sus datos y progreso
        
        SOBRE EL MEDIT (Modelo Educativo Digital Transmoderno de la Universidad de Cundinamarca):
        - Es el modelo educativo de la Universidad de Cundinamarca
        - Busca formar una persona transhumana, para la vida, los valores democráticos, la civilidad y la libertad
        - Pasa de una educación para el hacer y el trabajo, a una educación para el ser
        - Se basa en la construcción dialógica y formativa del conocimiento
        - Asume la postura filosófica de la transmodernidad (Rodríguez Magda, 2004)
        - Defiende la justicia, la solidaridad y la libertad como utopías pendientes
        - Es translocal: equilibra lo global y lo local, defiende la identidad cultural y ancestral
        - Lo digital no se reduce a tecnología sino a una nueva manera de ser, pensar y hacer
        - Busca formar sujetos que transformen su realidad local en beneficio del desarrollo comunitario
        - Se contrapone al dualismo cartesiano y al pensamiento binario occidental
        
        VALORES QUE PROMUEVES:
        - Desarrollo humano integral
        - Ética y responsabilidad social
        - Autonomía y libertad
        - Transformación positiva del entorno
        - Bienestar personal y colectivo
        - Evolución personal continua
        
         IDIOMA: OBLIGATORIO - Detecta el idioma del mensaje del usuario y responde SIEMPRE en ese mismo idioma. Si el usuario escribe en inglés, tu respuesta debe ser completamente en inglés. 
         Si el usuario escribe en español, tu respuesta debe ser completamente en español. Nunca mezcles idiomas en una misma respuesta. 
        
        
        TONO: Amigable, motivador, educativo. Eres un compañero de aprendizaje, no solo un asistente.
        
        Mantén respuestas concisas y útiles. Si te preguntan algo fuera de tu contexto, responde brevemente y redirige la conversación hacia el aprendizaje del inglés o los valores del MEDIT.
    """.trimIndent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatAdapter(messages)
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.rvChat.adapter = adapter

        addMessage(
            "¡Hola! 👋 Soy tu asistente de SealSpeak.\n\n" +
                    "\"Soy LIBRE, AUTÓNOMO Y RESPONSABLE a través del diálogo y la construcción, " +
                    "como ideal regulativo; me dirijo, controlo y dicto mis propias leyes.\"\n\n" +
                    "Puedo ayudarte con:\n" +
                    "• 📚 Cómo usar SealSpeak\n" +
                    "• 🎓 El MEDIT de la UniCundinamarca\n" +
                    "• 🌱 Desarrollo personal y valores\n" +
                    "• 🇬🇧 Preguntas sobre inglés\n\n" +
                    "¿En qué te puedo ayudar hoy?",
            isUser = false
        )

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            binding.etMessage.setText("")
            sendMessage(text)
        }
    }

    private fun sendMessage(text: String) {
        addMessage(text, isUser = true)
        addMessage("...", isUser = false)

        chatHistory.put(JSONObject().apply {
            put("role", "user")
            put("content", text)
        })

        lifecycleScope.launch {
            try {
                val responseText = withContext(Dispatchers.IO) {
                    callGroqAPI()
                }

                chatHistory.put(JSONObject().apply {
                    put("role", "assistant")
                    put("content", responseText)
                })

                messages[messages.size - 1] = ChatMessage(responseText, isUser = false)
                adapter.notifyItemChanged(messages.size - 1)
                binding.rvChat.scrollToPosition(messages.size - 1)

            } catch (e: Exception) {
                android.util.Log.e("ChatFragment", "Error: ${e.message}", e)
                messages[messages.size - 1] = ChatMessage("Error: ${e.message}", isUser = false)
                adapter.notifyItemChanged(messages.size - 1)
            }
        }
    }

    private fun callGroqAPI(): String {
        val messagesArray = JSONArray()

        // System message
        messagesArray.put(JSONObject().apply {
            put("role", "system")
            put("content", systemPrompt)
        })

        // Historial de conversación
        for (i in 0 until chatHistory.length()) {
            messagesArray.put(chatHistory.getJSONObject(i))
        }

        val body = JSONObject().apply {
            put("model", "llama-3.3-70b-versatile")
            put("messages", messagesArray)
            put("max_tokens", 1024)
            put("temperature", 0.7)
        }

        val request = Request.Builder()
            .url("https://api.groq.com/openai/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${BuildConfig.GROQ_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .post(body.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Respuesta vacía")

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code}: $responseBody")
        }

        val json = JSONObject(responseBody)
        return json.getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(ChatMessage(text, isUser))
        adapter.notifyItemInserted(messages.size - 1)
        binding.rvChat.scrollToPosition(messages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}