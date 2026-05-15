# 🦭 SealSpeak

Aplicación móvil Android para el aprendizaje interactivo del inglés, desarrollada por estudiantes de la **Universidad de Cundinamarca, extensión Ubaté**.

> *"Soy LIBRE, AUTÓNOMO Y RESPONSABLE a través del diálogo y la construcción, como ideal regulativo; me dirijo, controlo y dicto mis propias leyes."*

---

## 📱 Descripción

SealSpeak es una app educativa inspirada en el modelo de aprendizaje gamificado, que combina lecciones de gramática, vocabulario, videos interactivos y un asistente de IA, todo alineado con el **MEDIT** (Modelo Educativo Digital Transmoderno) de la Universidad de Cundinamarca.

---

## ✨ Módulos funcionales

| # | Módulo | Descripción |
|---|--------|-------------|
| 1 | 🔐 **Autenticación** | Registro e inicio de sesión con email/contraseña o Google |
| 2 | 📋 **Onboarding** | Encuesta inicial para personalizar la experiencia del usuario |
| 3 | 🏠 **Home** | Pantalla principal con saludo personalizado |
| 4 | 📚 **Lecciones** | Gramática, vocabulario y videos interactivos por nivel |
| 5 | 🏆 **Ranking** | Tabla de posiciones entre usuarios por puntos acumulados |
| 6 | 👤 **Perfil** | Datos del usuario y cierre de sesión |
| 7 | 🤖 **Chatbot** | Asistente de IA con conocimiento del MEDIT y SealSpeak |

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Autenticación:** Firebase Authentication
- **Base de datos:** Cloud Firestore
- **Videos:** YouTube Player API
- **IA:** Groq API (Llama 3.3 70B)
- **Navegación:** Navigation Component + BottomNavigationView
- **Arquitectura:** MVVM (en progreso)
- **Imágenes:** Glide

---

## 🚀 Configuración del proyecto

### Requisitos
- Android Studio Hedgehog o superior
- JDK 11+
- Android SDK 24+
- Cuenta de Firebase

### Pasos

1. Clona el repositorio:
```bash
git clone https://github.com/Zerokis17/SealSpeak.git
```

2. Abre el proyecto en Android Studio.

3. Crea un archivo `local.properties` en la raíz del proyecto con las siguientes keys:
```
YOUTUBE_API_KEY=tu_youtube_api_key
GROQ_API_KEY=tu_groq_api_key
GEMINI_API_KEY=tu_gemini_api_key
```

4. Descarga el archivo `google-services.json` desde Firebase Console y colócalo en la carpeta `app/`.

5. Habilita en Firebase Console:
   - Authentication → Email/Password y Google
   - Cloud Firestore
   - Agrega el SHA-1 del keystore en la configuración de la app

6. Haz **Sync Now** en Android Studio y ejecuta la app.

---

## 🗄️ Estructura de Firestore

```
users/
  └── {userId}/
        ├── nickname: String
        ├── level: String
        ├── dailyTime: String
        ├── reason: String
        ├── points: Number
        └── onboardingCompleted: Boolean

grammar/
  └── {lessonId}/
        ├── title: String
        ├── level: String
        ├── explanation: String
        ├── structure: String
        └── examples: Array

vocabulary/
  └── {categoryId}/
        ├── category: String
        └── words: Array<Map>

lessons/
  └── {lessonId}/
        ├── title: String
        ├── level: String
        ├── videoId: String
        ├── question: String
        └── options: Array<Map>
```

---

## 🎓 MEDIT - Modelo Educativo Digital Transmoderno

SealSpeak está desarrollado bajo los principios del MEDIT de la Universidad de Cundinamarca, que busca:

- Formar personas **transhumanas** para la vida y los valores democráticos
- Pasar de una educación para el **hacer** a una educación para el **ser**
- Promover la **construcción dialógica** del conocimiento
- Defender la **identidad cultural** local frente a la globalización
- Fomentar la **autonomía**, la **libertad** y la **responsabilidad social**

---

## 📊 Sistema de puntos

| Acción | Puntos |
|--------|--------|
| Completar lección de gramática | +10 pts |
| Completar categoría de vocabulario | +5 pts |
| Ver video interactivo completo | +15 pts |

---

## 🔒 Seguridad

- Las API keys se almacenan en `local.properties` (no incluido en el repositorio)
- El archivo `google-services.json` no se incluye en el repositorio
- Las reglas de Firestore garantizan que cada usuario solo puede modificar sus propios datos

---

## 👨‍💻 Autor

**Sebastián Velásquez**  
Universidad de Cundinamarca — Extensión Ubaté  
Ingeniería de Sistemas

**Bryan Sierra**  
Universidad de Cundinamarca — Extensión Ubaté  
Ingeniería de Sistemas

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos para la Universidad de Cundinamarca.
