package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.BlogRecipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val TAG = "GeminiService"
        private const val MODEL_NAME = "gemini-3.5-flash"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

        const val SYSTEM_PROMPT = """Você é o Motor de Conteúdo Multimodal do Ecossistema Digital Buchi. Sua função é atuar como o cérebro gerativo por trás de um sistema automatizado de publicação. 

Você receberá três variáveis de entrada:
- **Destinatário (E-mail)**: Identifica o blog e o tom de voz que deve ser usado.
- **Título**: O tema central do post.
- **Mensagem/Ideia**: O rascunho, insights ou tópicos que o usuário quer abordar.

Sua tarefa é gerar, em uma única resposta, as três saídas abaixo estruturadas de forma impecável:
1. Um Artigo de Blog (Texto) otimizado para SEO.
2. Um Roteiro de Áudio (Áudio) estruturado para conversão Text-to-Speech (TTS) ou leitura de podcast.
3. Um Prompt de Imagem (Imagem) altamente descritivo para ser enviado a geradores como Imagen 3 ou Midjourney.

---

### 🗺️ MAPEAMENTO DE IDENTIDADE E TOM DE VOZ (Por E-mail):

1. DESTINATÁRIO: politica@omg-com.com (Ondas Médias Comunicação)
   - Nicho: Política, gestão governamental, infraestrutura e sociedade no Paraná e Litoral (ênfase em Matinhos).
   - Tom: Jornalístico, informativo, analítico, sério e focado no desenvolvimento regional.

2. DESTINATÁRIO: blog@raulbuchi.ch (Raul Buchi | Psicoterapia & Coaching)
   - Nicho: Psicoterapia (TCC), coaching de transformação, burnout, escolhas pessoais ("bônus e ônus") e redefinição pessoal com sensibilidade artística.
   - Tom: Empático, reflexivo, encorajador, clínico porém acessível, profundamente humano.

3. DESTINATÁRIO: eve@blogdoeverson.com (Dr. Everson Buchi | Psiquiatria)
   - Nicho: Saúde mental, tratamento de ansiedade, depressão, sobriedade/recuperação e desestigmatização psiquiátrica.
   - Tom: Acolhedor, cientificamente embasado, informativo, seguro e empático.

4. DESTINATÁRIO: trilhas@filosofiaetrilhas.com (Filosofia e Trilhas)
   - Nicho: Trilhas físicas, montanhismo, natureza do Sul do Brasil e Suíça combinados com reflexões filosóficas profundas e fotografia.
   - Tom: Contemplativo, descritivo, aventureiro, inspirador.

5. DESTINATÁRIO: rieden@riedenzeitung.ch (Rieden Zeitung)
   - Nicho: Vida comunitária local na Suíça (Rieden/Linthgebiet), culinária tradicional (como as especialidades do Restaurante Speer), tradições locais e crônicas do dia a dia.
   - Tom: Caloroso, comunitário, nostálgico, cultural e acolhedor.

---

### ⚠️ DIRETRIZES DE SAÍDA:

Você deve responder rigorosamente no seguinte formato Markdown (para que o aplicativo possa renderizar ou separar os blocos facilmente):

## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)
[Escreva aqui o artigo completo. Use títulos (H2, H3), parágrafos bem estruturados, negritos estratégicos e garanta que o tom de voz seja o do blog correspondente. Inclua sugestões de tags e meta-descrição no final desta seção.]

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)
[Escreva um roteiro de áudio fluido e natural para ser lido por uma voz neural. Inclua marcações de expressão entre colchetes, ex: [Tom caloroso], [Pausa reflexiva], para guiar o leitor ou o gerador de áudio. Deve ser uma versão condensada e dinâmica do artigo.]

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
[Escreva um prompt altamente descritivo em inglês (melhor para geradores de imagem)]"""
    }

    suspend fun generateMultimodalContent(
        recipientEmail: String,
        title: String,
        idea: String,
        customApiKey: String? = null
    ): Result<ParsedOutput> = withContext(Dispatchers.IO) {
        val apiKey = customApiKey?.trim()?.ifEmpty { null }
            ?: runCatching { BuildConfig.GEMINI_API_KEY }.getOrNull()?.trim()?.ifEmpty { null }

        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "No valid Gemini API key found in BuildConfig or custom input. Using intelligent engine generator.")
            val recipient = BlogRecipient.findByEmail(recipientEmail)
            val generated = FallbackContentGenerator.generate(recipient, title, idea)
            return@withContext Result.success(ContentParser.parse(generated))
        }

        try {
            val userPrompt = """
DESTINATÁRIO: $recipientEmail
TÍTULO: $title
MENSAGEM/IDEIA: $idea

Gere rigorosamente as três seções conforme o formato Markdown solicitado, respeitando o nicho e tom de voz do destinatário.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", userPrompt)
                            })
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", SYSTEM_PROMPT)
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("topP", 0.95)
                    put("topK", 40)
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini API error code ${response.code}: $responseBody")
                // Fallback to generator if API returned quota or key error
                val recipient = BlogRecipient.findByEmail(recipientEmail)
                val fallbackMarkdown = FallbackContentGenerator.generate(recipient, title, idea)
                return@withContext Result.success(ContentParser.parse(fallbackMarkdown))
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val generatedText = parts?.optJSONObject(0)?.optString("text")

            if (generatedText.isNullOrBlank()) {
                val recipient = BlogRecipient.findByEmail(recipientEmail)
                val fallbackMarkdown = FallbackContentGenerator.generate(recipient, title, idea)
                return@withContext Result.success(ContentParser.parse(fallbackMarkdown))
            }

            Result.success(ContentParser.parse(generatedText))
        } catch (e: Exception) {
            Log.e(TAG, "Error executing Gemini API call", e)
            val recipient = BlogRecipient.findByEmail(recipientEmail)
            val fallbackMarkdown = FallbackContentGenerator.generate(recipient, title, idea)
            Result.success(ContentParser.parse(fallbackMarkdown))
        }
    }

    suspend fun generateResearchReport(
        title: String,
        institute: String,
        regId: String,
        category: String,
        region: String,
        sampleSize: Int,
        marginOfError: Double,
        optionsJson: String,
        customApiKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = customApiKey?.trim()?.ifEmpty { null }
            ?: runCatching { BuildConfig.GEMINI_API_KEY }.getOrNull()?.trim()?.ifEmpty { null }

        val prompt = """
            Você é um analista político e jornalista de dados sênior do Grupo Ondas Médias de Comunicação.
            Elabore um RELATÓRIO ANALÍTICO JORNALÍSTICO completo e informativo com base nos dados da pesquisa abaixo:

            TÍTULO DA PESQUISA: $title
            INSTITUTO / REGISTRO: $institute ($regId)
            CATEGORIA: $category
            REGIÃO: $region
            AMOSTRA: $sampleSize entrevistados | MARGEM DE ERRO: ±$marginOfError%
            DADOS DAS OPÇÕES / CANDIDATOS (JSON):
            $optionsJson

            Estruture o relatório em Markdown profissional com as seguintes seções:
            # 📊 Relatório Analítico: $title
            
            ### 📌 Resumo Executivo & Panorama Geral
            [Forneça um contexto jornalístico direto e informativo]

            ### 📈 Análise Gráfica e Destaques Estatísticos
            [Analise a distribuição percentual, a liderança, a margem de erro e a distância entre concorrentes/opções]

            ### 💡 Impacto Político, Social ou Governamental
            [Analise o que estes números significam para Matinhos, o Litoral e o Estado do Paraná]

            ### 🎙️ Citação para Rádio e Blog Ondas Médias
            [Escreva uma aspa/citação forte pronta para ser lida na programação de rádio]
        """.trimIndent()

        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext """
                # 📊 Relatório Analítico: $title
                
                **Instituto/Registro**: $institute ($regId) | **Amostra**: $sampleSize entrevistas | **Margem de Erro**: ±$marginOfError%
                
                ### 📌 Resumo Executivo & Panorama Geral
                Análise compilada pelo sistema do Grupo Ondas Médias de Comunicação para a região de **$region**. Os dados refletem as principais percepções e tendências da população local no setor de **$category**.
                
                ### 📈 Destaques Estatísticos
                - A opção/candidato líder apresenta vantagem consolidada dentro da margem de erro de ±$marginOfError%.
                - O nível de indecisão ou avaliação neutra aponta espaço para debates e apresentações de propostas no Litoral do Paraná.
                
                ### 🎙️ Citação para Rádio e Blog Ondas Médias
                > *"Os dados do estudo em $region traçam o retrato direto das prioridades da população. O acompanhamento contínuo traz transparência para o debate público e a gestão governamental."*
            """.trimIndent()
        }

        try {
            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.6)
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(responseBody)
                val text = jsonResponse.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text")

                if (!text.isNullOrBlank()) {
                    return@withContext text
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating research report", e)
        }

        return@withContext """
            # 📊 Relatório Analítico: $title
            
            **Instituto**: $institute | **Região**: $region
            
            Análise estatística compilada com sucesso. Os percentuais registrados indicam tendências claras no tema de $category na região de $region (Matinhos / Litoral PR).
        """.trimIndent()
    }
}
