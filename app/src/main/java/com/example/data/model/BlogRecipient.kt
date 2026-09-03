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
                id = "politica",
                email = "politica@omg-com.com",
                name = "Ondas Médias Comunicação",
                organization = "Grupo Ondas Médias",
                niche = "Política, gestão governamental, infraestrutura e sociedade no Paraná e Litoral (ênfase em Matinhos).",
                tone = "Jornalístico, informativo, analítico, sério e focado no desenvolvimento regional.",
                primaryPillar = "POLÍTICA & SOCIEDADE",
                exampleTitle = "Obras de macrodrenagem e revitalização da orla de Matinhos: impactos no turismo e comércio local",
                exampleIdea = "Analisar os avanços estruturais da revitalização em Matinhos, o impacto econômico para comerciantes da orla e pescadores artesanais, as perspectivas para a próxima temporada de verão e a gestão orçamentária do Estado.",
                tags = listOf("Matinhos", "Litoral do PR", "Infraestrutura", "Gestão Pública", "Desenvolvimento Regional")
            ),
            BlogRecipient(
                id = "raulbuchi",
                email = "blog@raulbuchi.ch",
                name = "Raul Buchi | Psicoterapia & Coaching",
                organization = "Raul Buchi Praxis",
                niche = "Psicoterapia (TCC), coaching de transformação, burnout, escolhas pessoais ('bônus e ônus') e redefinição pessoal com sensibilidade artística.",
                tone = "Empático, reflexivo, encorajador, clínico porém acessível, profundamente humano.",
                primaryPillar = "DESENVOLVIMENTO PESSOAL",
                exampleTitle = "O dilema das escolhas: assumindo o bônus e o ônus na redefinição de carreira",
                exampleIdea = "Explorar o conceito de que toda escolha madura carrega um custo emocional ('ônus') e uma libertação ('bônus'). Relatar como profissionais em crise de burnout podem romper o ciclo de autosacrifício usando pilares da TCC e redescobrindo sua essência autêntica.",
                tags = listOf("Psicoterapia TCC", "Burnout", "Bônus e Ônus", "Carreira", "Redefinição")
            ),
            BlogRecipient(
                id = "everson",
                email = "eve@blogdoeverson.com",
                name = "Dr. Everson Buchi | Psiquiatria",
                organization = "Blog do Dr. Everson",
                niche = "Saúde mental, tratamento de ansiedade, depressão, sobriedade/recuperação e desestigmatização psiquiátrica.",
                tone = "Acolhedor, cientificamente embasado, informativo, seguro e empático.",
                primaryPillar = "SAÚDE MENTAL",
                exampleTitle = "Desmistificando o tratamento medicamentoso no transtorno de ansiedade generalizada (TAG)",
                exampleIdea = "Explicar de forma acessível e baseada em evidências científicas como os neurotransmissores atuam na ansiedade crônica, desmistificar o medo de dependência aos ansiolíticos modernos e reforçar o papel integrado da psicoterapia e hábitos saudáveis.",
                tags = listOf("Psiquiatria Clínica", "Ansiedade (TAG)", "Depressão", "Sobriedade", "Desestigmatização")
            ),
            BlogRecipient(
                id = "trilhas",
                email = "trilhas@filosofiaetrilhas.com",
                name = "Filosofia e Trilhas",
                organization = "Filosofia e Trilhas",
                niche = "Trilhas físicas, montanhismo, natureza do Sul do Brasil e Suíça combinados com reflexões filosóficas profundas e fotografia.",
                tone = "Contemplativo, descritivo, aventureiro, inspirador.",
                primaryPillar = "NATUREZA & FILOSOFIA",
                exampleTitle = "Entre o Marumbi e as cristas alpinas: a solidão fecunda dos cumes",
                exampleIdea = "Narrar a ascensão de uma trilha íngreme na Serra do Mar e traçar um paralelo com as travessias nas montanhas suíças. Abordar a ideia nietzschiana ou estóica de perseverança física como ancoragem existencial e a contemplação silenciosa da névoa.",
                tags = listOf("Montanhismo", "Serra do Mar", "Alpes Suíços", "Estoicismo", "Fotografia de Montanha")
            ),
            BlogRecipient(
                id = "rieden",
                email = "rieden@riedenzeitung.ch",
                name = "Rieden Zeitung",
                organization = "Rieden Zeitung",
                niche = "Vida comunitária local na Suíça (Rieden/Linthgebiet), culinária tradicional (como as especialidades do Restaurante Speer), tradições locais e crônicas do dia a dia.",
                tone = "Caloroso, comunitário, nostálgico, cultural e acolhedor.",
                primaryPillar = "COMUNIDADE & CULTURA",
                exampleTitle = "Sabores e memórias do outono em Rieden: o clássico aconchego do Restaurante Speer",
                exampleIdea = "Uma crônica afetuosa sobre a chegada das primeiras brisas frias em Rieden e no vale Linthgebiet, o encontro dos vizinhos ao redor das especialidades gastronômicas do Speer, e o valor do convívio comunitário que resiste ao ritmo acelerado do mundo.",
                tags = listOf("Rieden", "Linthgebiet", "Restaurante Speer", "Tradição Suíça", "Crônica Local")
            )
        )

        fun findByEmail(email: String): BlogRecipient {
            return ALL_RECIPIENTS.find { it.email.equals(email.trim(), ignoreCase = true) }
                ?: ALL_RECIPIENTS.first()
        }
    }
}
