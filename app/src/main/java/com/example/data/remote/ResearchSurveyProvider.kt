package com.example.data.remote

import com.example.data.model.ResearchSurveyEntity
import org.json.JSONArray
import org.json.JSONObject

object ResearchSurveyProvider {

    fun getDefaultSurveys(): List<ResearchSurveyEntity> {
        val now = System.currentTimeMillis()

        val pollMatinhos = JSONArray().apply {
            put(JSONObject().apply {
                put("name", "Candidato A (Situação / Obras)")
                put("percentage", 39.4)
                put("colorHex", "#0061A4")
            })
            put(JSONObject().apply {
                put("name", "Candidato B (Oposição / Litoral)")
                put("percentage", 31.2)
                put("colorHex", "#00335A")
            })
            put(JSONObject().apply {
                put("name", "Candidato C (Independente)")
                put("percentage", 14.8)
                put("colorHex", "#8D5B18")
            })
            put(JSONObject().apply {
                put("name", "Brancos / Nulos")
                put("percentage", 8.1)
                put("colorHex", "#7A8490")
            })
            put(JSONObject().apply {
                put("name", "Indecisos / Não Sabem")
                put("percentage", 6.5)
                put("colorHex", "#B0B8C1")
            })
        }.toString()

        val surveyInfraOrla = JSONArray().apply {
            put(JSONObject().apply {
                put("name", "Ótimo / Bom (Aprovação das Obras)")
                put("percentage", 74.2)
                put("colorHex", "#0A5844")
            })
            put(JSONObject().apply {
                put("name", "Regular")
                put("percentage", 16.5)
                put("colorHex", "#0061A4")
            })
            put(JSONObject().apply {
                put("name", "Ruim / Péssimo")
                put("percentage", 6.8)
                put("colorHex", "#BA1A1A")
            })
            put(JSONObject().apply {
                put("name", "Não Opinaram")
                put("percentage", 2.5)
                put("colorHex", "#7A8490")
            })
        }.toString()

        val surveyPrioridadesLitoral = JSONArray().apply {
            put(JSONObject().apply {
                put("name", "Saúde Pública e UPA Matinhos")
                put("percentage", 32.5)
                put("colorHex", "#0A5844")
            })
            put(JSONObject().apply {
                put("name", "Drenagem e Saneamento Básico")
                put("percentage", 28.0)
                put("colorHex", "#0061A4")
            })
            put(JSONObject().apply {
                put("name", "Segurança e Iluminação Pública")
                put("percentage", 21.4)
                put("colorHex", "#00335A")
            })
            put(JSONObject().apply {
                put("name", "Geração de Emprego no Turismo")
                put("percentage", 12.1)
                put("colorHex", "#6E431B")
            })
            put(JSONObject().apply {
                put("name", "Educação e Creches")
                put("percentage", 6.0)
                put("colorHex", "#7A8490")
            })
        }.toString()

        return listOf(
            ResearchSurveyEntity(
                id = 1,
                title = "Pesquisa de Intenção de Voto - Eleições Municipais Matinhos 2026",
                institute = "Instituto Ondas Médias / Paraná Pesquisas",
                registrationId = "PR-08492/2026",
                category = "POLÍTICA",
                region = "Matinhos e Caiobá",
                sampleSize = 600,
                marginOfError = 3.9,
                datePeriod = "28 a 31 de Agosto de 2026",
                optionsJson = pollMatinhos,
                analyticalReport = """
                    # 📊 Análise Jornalística: Intenção de Voto em Matinhos 2026
                    
                    **Registro TSE**: PR-08492/2026 | **Amostra**: 600 eleitores presenciais | **Margem de Erro**: ± 3,9%
                    
                    ### 🔍 Principais Achados Estatísticos
                    1. **Liderança Consolidação do Bloco da Situação**: O Candidato A lidera a corrida eleitoral com **39,4%** das intenções de voto estimuladas, sustentado principalmente pela alta aprovação das obras estruturantes da Orla de Matinhos e Caiobá.
                    2. **Competitividade da Oposição**: O Candidato B aparece em segundo lugar com **31,2%**, indicando um cenário de segundo turno virtualmente polarizado e uma margem de diferença de apenas 8,2 pontos percentuais.
                    3. **Terceira Via e Eleitorado Indeciso**: O Candidato C soma **14,8%**, enquanto a soma de indecisos e votos nulos/brancos atinge **14,6%**, representando o grupo fiel que definirá o desfecho nas semanas decisivas.
                    
                    ---
                    
                    ### 🎙️ Destaque para Transmissão de Rádio e Blog
                    > *"A pesquisa revela um eleitorado de Matinhos altamente atento aos resultados concretos de infraestrutura e serviços públicos de saúde. A diferença de 8.2 pontos entre os dois primeiros colocados mantém o debate em aberto para os debates na Rádio Ondas Médias."*
                """.trimIndent(),
                timestamp = now - (2 * 24 * 3600 * 1000L)
            ),
            ResearchSurveyEntity(
                id = 2,
                title = "Avaliação das Obras da Orla e Infraestrutura de Matinhos",
                institute = "DataLitoral - Pesquisas de Opinião",
                registrationId = "Avali-PR-2026/04",
                category = "INFRAESTRUTURA",
                region = "Matinhos e Litoral do Paraná",
                sampleSize = 800,
                marginOfError = 3.2,
                datePeriod = "August 2026",
                optionsJson = surveyInfraOrla,
                analyticalReport = """
                    # 🌊 Relatório de Aprovação Popular: Obras da Orla de Matinhos
                    
                    **Amostra**: 800 entrevistados (moradores e comerciantes locais) | **Margem de Erro**: ± 3,2%
                    
                    ### 📊 Análise Informativa
                    A aceitação das intervenções de engenharia e revitalização da orla em Matinhos atinge níveis recordes no Paraná. **74,2%** dos entrevistados avaliam o projeto como **Ótimo ou Bom**, citando a contenção de ressacas e a valorização imobiliária como fatores determinantes.
                    
                    Apenas 6,8% demonstraram insatisfação, vinculada a transtornos temporários no trânsito e poeira durante o período de obras.
                """.trimIndent(),
                timestamp = now - (5 * 24 * 3600 * 1000L)
            ),
            ResearchSurveyEntity(
                id = 3,
                title = "Prioridades e Demandas da População no Litoral do Paraná",
                institute = "Grupo Ondas Médias Analytics",
                registrationId = "Pesq-Gov-2026/08",
                category = "AVALIAÇÃO GOVERNAMENTAL",
                region = "Matinhos, Pontal do Paraná e Guaratuba",
                sampleSize = 1000,
                marginOfError = 3.0,
                datePeriod = "15 a 25 de Agosto de 2026",
                optionsJson = surveyPrioridadesLitoral,
                analyticalReport = null,
                timestamp = now - (10 * 24 * 3600 * 1000L)
            )
        )
    }
}
