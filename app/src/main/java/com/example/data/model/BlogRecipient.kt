package com.example.data.model

data class BlogRecipient(
    val id: String,
    val email: String,
    val name: String,
    val organization: String,
    val niche: String,
    val tone: String,
    val primaryPillar: String,
    val exampleTitle: String,
    val exampleIdea: String,
    val tags: List<String>
) {
    companion object {
        val ALL_RECIPIENTS = listOf(
            BlogRecipient(
                id = "raulbuchi",
                email = "blog@raulbuchi.ch",
                name = "Blog Do Raul Buchi",
                organization = "Raul Buchi Praxis",
                niche = "Psicoterapia (TCC), coaching de transformação, burnout, escolhas pessoais ('bônus e ônus') e redefinição pessoal.",
                tone = "Empático, reflexivo, encorajador, clínico porém acessível, profundamente humano.",
                primaryPillar = "DESENVOLVIMENTO PESSOAL",
                exampleTitle = "O dilema das escolhas: assumindo o bônus e o ônus na redefinição de carreira",
                exampleIdea = "Explorar o conceito de que toda escolha madura carrega um custo emocional ('ônus') e uma libertação ('bônus').",
                tags = listOf("Psicoterapia TCC", "Burnout", "Bônus e Ônus", "Carreira", "Redefinição")
            ),
            BlogRecipient(
                id = "everson",
                email = "eve@blogdoeverson.ch",
                name = "Blog do Dr. Everson",
                organization = "Blog do Dr. Everson",
                niche = "Saúde mental, tratamento de ansiedade, depressão, sobriedade/recuperação e desestigmatização psiquiátrica.",
                tone = "Acolhedor, cientificamente embasado, informativo, seguro e empático.",
                primaryPillar = "SAÚDE MENTAL",
                exampleTitle = "Desmistificando o tratamento medicamentoso no transtorno de ansiedade generalizada (TAG)",
                exampleIdea = "Explicar de forma acessível como os neurotransmissores atuam na ansiedade crônica.",
                tags = listOf("Psiquiatria Clínica", "Ansiedade (TAG)", "Depressão", "Sobriedade", "Desestigmatização")
            ),
            BlogRecipient(
                id = "rieden",
                email = "rieden@riedenzetung.ch",
                name = "Blog de Rieden (Rieden Zeitung)",
                organization = "Rieden Zeitung",
                niche = "Vida comunitária local na Suíça (Rieden/Linthgebiet), culinária tradicional e crônicas do dia a dia.",
                tone = "Caloroso, comunitário, nostálgico, cultural e acolhedor.",
                primaryPillar = "COMUNIDADE & CULTURA",
                exampleTitle = "Sabores e memórias do outono em Rieden: o clássico aconchego do Restaurante Speer",
                exampleIdea = "Uma crônica afetuosa sobre a chegada das primeiras brisas frias em Rieden e no vale Linthgebiet.",
                tags = listOf("Rieden", "Linthgebiet", "Restaurante Speer", "Tradição Suíça", "Crônica Local")
            ),
            BlogRecipient(
                id = "trilhas",
                email = "trilhas@filosofiaetrilhas.com",
                name = "Blog de Filosofia e Trilhas",
                organization = "Filosofia e Trilhas",
                niche = "Trilhas físicas, montanhismo, natureza do Sul do Brasil e Suíça combinados com reflexões filosóficas profundas e fotografia.",
                tone = "Contemplativo, descritivo, aventureiro, inspirador.",
                primaryPillar = "NATUREZA & FILOSOFIA",
                exampleTitle = "Entre o Marumbi e as cristas alpinas: a solidão fecunda dos cumes",
                exampleIdea = "Narrar a ascensão de uma trilha íngreme na Serra do Mar e traçar um paralelo com as travessias nas montanhas suíças.",
                tags = listOf("Montanhismo", "Serra do Mar", "Alpes Suíços", "Estoicismo", "Fotografia")
            ),
            BlogRecipient(
                id = "politica",
                email = "politica@omg-com.com",
                name = "Blog de Comunicação (Ondas Médias)",
                organization = "Grupo Ondas Médias",
                niche = "Política, gestão governamental, infraestrutura e sociedade no Paraná e Litoral (ênfase em Matinhos).",
                tone = "Jornalístico, informativo, analítico, sério e focado no desenvolvimento regional.",
                primaryPillar = "POLÍTICA & SOCIEDADE",
                exampleTitle = "Obras de macrodrenagem e revitalização da orla de Matinhos: impactos no turismo e comércio local",
                exampleIdea = "Analisar os avanços estruturais da revitalização em Matinhos e o impacto econômico para comerciantes da orla.",
                tags = listOf("Matinhos", "Litoral do PR", "Infraestrutura", "Gestão Pública", "Desenvolvimento Regional")
            ),
            BlogRecipient(
                id = "ideb",
                email = "blog@institutoideb.com",
                name = "Blog do Instituto IDEB",
                organization = "Instituto IDEB",
                niche = "Educação, avaliação educacional, desenvolvimento socioeducativo, políticas públicas e inovação pedagógica.",
                tone = "Institucional, educativo, analítico, propositivo e fundamentado.",
                primaryPillar = "EDUCAÇÃO & DESENVOLVIMENTO",
                exampleTitle = "Métricas do IDEB e estratégias para fortalecimento da aprendizagem na educação básica",
                exampleIdea = "Analisar indicadores educacionais, valorização docente e projetos pedagógicos de alto impacto.",
                tags = listOf("IDEB", "Educação Básica", "Políticas Públicas", "Aprendizagem", "Gestão Escolar")
            )
        )

        fun findByEmail(email: String): BlogRecipient {
            return ALL_RECIPIENTS.find { it.email.equals(email.trim(), ignoreCase = true) }
                ?: ALL_RECIPIENTS.first()
        }
    }
}
