package com.example.data.remote

data class ParsedOutput(
    val fullMarkdown: String,
    val articleText: String,
    val audioScript: String,
    val imagePrompt: String
)

object ContentParser {
    fun parse(rawMarkdown: String): ParsedOutput {
        val trimmed = rawMarkdown.trim()
        val textHeader = "## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)"
        val audioHeader = "## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)"
        val imageHeader = "## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)"

        var article = ""
        var audio = ""
        var image = ""

        val textIndex = trimmed.indexOf("## 📝 1.")
        val audioIndex = trimmed.indexOf("## 🎙️ 2.")
        val imageIndex = trimmed.indexOf("## 🎨 3.")

        if (textIndex != -1 && audioIndex != -1 && imageIndex != -1 && audioIndex > textIndex && imageIndex > audioIndex) {
            val textPart = trimmed.substring(textIndex, audioIndex).trim()
            val audioPart = trimmed.substring(audioIndex, imageIndex).trim()
            val imagePart = trimmed.substring(imageIndex).trim()

            article = textPart.substringAfter("\n").trim()
            audio = audioPart.substringAfter("\n").trim()
            image = imagePart.substringAfter("\n").trim()
        } else {
            // Fallback split if headers differ slightly
            val parts = trimmed.split(Regex("(?=## )"))
            for (part in parts) {
                when {
                    part.contains("1. TEXTO", ignoreCase = true) || part.contains("ARTIGO", ignoreCase = true) -> {
                        article = part.substringAfter("\n").trim()
                    }
                    part.contains("2. ÁUDIO", ignoreCase = true) || part.contains("AUDIO", ignoreCase = true) || part.contains("ROTEIRO", ignoreCase = true) -> {
                        audio = part.substringAfter("\n").trim()
                    }
                    part.contains("3. IMAGEM", ignoreCase = true) || part.contains("PROMPT", ignoreCase = true) -> {
                        image = part.substringAfter("\n").trim()
                    }
                }
            }
        }

        if (article.isBlank()) article = trimmed
        if (audio.isBlank()) audio = "Roteiro sintetizado do artigo para narração de podcast ou áudio institucional."
        if (image.isBlank()) image = "Photorealistic editorial cover image representing the article theme, cinematic natural lighting, 8k."

        return ParsedOutput(
            fullMarkdown = trimmed,
            articleText = article,
            audioScript = audio,
            imagePrompt = image
        )
    }

    /**
     * Cleans up the audio script for Text-to-Speech playback by removing emotion/tone instructions
     * like [Tom caloroso], [Pausa reflexiva], [Voz firme], etc.
     */
    fun cleanScriptForTts(script: String): String {
        return script.replace(Regex("\\[[^\\]]+\\]"), "")
            .replace(Regex("\\*\\*"), "")
            .replace(Regex("#+"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
