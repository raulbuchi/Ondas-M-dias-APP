package com.example.data.remote

import com.example.data.model.BlogRecipient

object FallbackContentGenerator {

    fun generate(recipient: BlogRecipient, title: String, idea: String): String {
        val cleanTitle = if (title.isNotBlank()) title else recipient.exampleTitle
        val cleanIdea = if (idea.isNotBlank()) idea else recipient.exampleIdea

        return when (recipient.id) {
            "politica" -> generatePoliticaContent(cleanTitle, cleanIdea)
            "raulbuchi" -> generateRaulBuchiContent(cleanTitle, cleanIdea)
            "everson" -> generateEversonContent(cleanTitle, cleanIdea)
            "trilhas" -> generateTrilhasContent(cleanTitle, cleanIdea)
            "rieden" -> generateRiedenContent(cleanTitle, cleanIdea)
            else -> generateGenericContent(recipient, cleanTitle, cleanIdea)
        }
    }

    private fun generatePoliticaContent(title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

O desenvolvimento do litoral paranaense atravessa um período de transformações estruturais decisivas. No epicentro dessa nova dinâmica regional, o município de **Matinhos** destaca-se como exemplo de como o planejamento governamental e investimentos consistentes em infraestrutura urbana e costeira podem redefinir o panorama socioeconômico de toda uma comunidade.

## A Dinâmica das Obras e a Conexão com a Economia Regional

A modernização da infraestrutura costeira vai muito além da estética urbana: ela protege áreas suscetíveis a ressacas marinhas, equaciona gargalos históricos de drenagem pluvial e eleva o padrão de recepção turística durante o ano inteiro. Como destacado nos recentes debates sobre o tema, *$idea*, a integração entre os aportes do poder público estadual e as demandas dos comerciantes e pescadores tradicionais é a chave para a sustentabilidade do litoral.

A geração de empregos diretos na construção civil e a valorização imobiliária planejada apontam para um horizonte promissor. Contudo, analistas de gestão pública reforçam que a perenidade desses ganhos depende de uma governança fiscal austera, fiscalização rigorosa dos contratos e diálogo constante com as lideranças comunitárias.

## Desafios de Gestão e Perspectivas para o Litoral

Para além das intervenções imediatas, a sustentabilidade do Paraná litorâneo exige avanços concomitantes em mobilidade integrada, saneamento básico universal e segurança pública preventiva. Matinhos assume o protagonismo dessa transição, provando que políticas públicas sérias convertem investimentos em qualidade de vida real para os paranaenses.

---
**Meta-descrição:** Uma análise detalhada sobre $title, abordando infraestrutura, impactos econômicos e gestão governamental em Matinhos e no litoral do Paraná.
**Tags:** Matinhos, Litoral do Paraná, Infraestrutura Urbana, Gestão Pública, Ondas Médias, Política Regional, Economia do Paraná.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom jornalístico, sóbrio e articulado]
Olá, ouvintes da Rádio Ondas Médias. Estamos iniciando mais uma edição da nossa coluna de análise política e desenvolvimento regional.

[Pausa breve]
Hoje o nosso foco se volta para o Litoral do Paraná e as transformações em curso em Matinhos. O tema central: **$title**.

[Tom analítico, ritmo moderado]
As intervenções urbanas e de contenção que vêm sendo executadas na orla representam muito mais do que novas calçadas e engordamento de faixa de areia. Trata-se de uma estratégia de proteção territorial e dinamização econômica. Como pontuamos em nosso editorial: $idea. 

[Pausa reflexiva de 1 segundo]
O desafio primordial agora reside na continuidade administrativa e na capacidade dos gestores municipais e estaduais em transformar cimento em dignidade para o cidadão que vive ali os 365 dias do ano, não apenas na alta temporada.

[Tom conclusivo, firme]
Fique conectado com a Rádio Ondas Médias: informação analítica com credibilidade para o futuro do Paraná.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Editorial documentary photography of the Matinhos coastal waterfront in Paraná, Brazil, featuring modern urban revitalization architecture alongside the Atlantic ocean waves, dramatic golden hour sunlight filtering through coastal clouds, wide angle shot, clean journalistic composition, deep slate and warm natural tones, authentic brazilian coastline infrastructure, Leica SL2, 35mm lens, f/4, highly detailed, 8k resolution --ar 16:9 --style raw
        """.trimIndent()
    }

    private fun generateRaulBuchiContent(title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

Quantas vezes nos flagramos presos ao desejo de uma vida sem atritos, esperando um momento ideal onde todas as nossas escolhas nos tragam apenas recompensas? Na prática clínica da Terapia Cognitivo-Comportamental (TCC) e nos processos de coaching de transformação, há uma premissa basilar que precisamos resgatar: **toda escolha carrega inseparavelmente o seu bônus e o seu ônus**.

## O Peso Oculto das Renúncias Não Reconhecidas

Ao enfrentarmos momentos de exaustão ou burnout, frequentemente o sintoma não é apenas o excesso de trabalho, mas a ilusão de que poderíamos sustentar múltiplos papéis sem pagar o pedágio emocional correspondente. Como costumo pontuar em nossas reflexões: *$idea*.

Quando dizemos "sim" a um projeto que exige 14 horas diárias, o bônus pode ser o reconhecimento social e o retorno financeiro. No entanto, o ônus é a renúncia da serenidade noturna, do tempo contemplativo e do equilíbrio fisiológico. A maturidade psicológica começa quando paramos de nos vitimizar diante do ônus e passamos a escolhê-lo com consciência e intencionalidade.

## Redefinição com Sensibilidade e Coragem

Redefinir a própria trajetória não significa abandonar o mundo, mas alinhá-lo aos nossos valores mais caros. Na TCC, trabalhamos a reestruturação das crenças limitantes sobre o que "deveríamos" ser para abrir espaço para o que genuinamente faz sentido. Ao acolher os bônus e honrar os ônus das suas decisões, você recupera a autoria da sua própria existência.

---
**Meta-descrição:** Uma reflexão profunda com base na Terapia Cognitivo-Comportamental sobre escolhas maduras, burnout e como abraçar o bônus e o ônus da redefinição pessoal.
**Tags:** Psicoterapia TCC, Burnout, Raul Buchi, Bônus e Ônus, Redefinição de Vida, Saúde Emocional, Coaching de Carreira.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom caloroso, intimista e acolhedor]
Bem-vindo a mais um episódio de nossas conversas sobre vida, escolhas e redefinição. Aqui é o Raul Buchi.

[Pausa reflexiva suave]
Hoje eu quero te convidar para uma pausa honesta. Vamos falar sobre: **$title**.

[Tom empático, compassado]
Muitas vezes chegamos à exaustão não porque erramos o caminho, mas porque acreditamos na ilusão de que poderíamos escolher apenas a colheita, esquecendo o cultivo. A verdade é simples, mas libertadora: toda decisão traz consigo um bônus que nos eleva, e um ônus que nos cobra presença. Como refletimos hoje: $idea.

[Pausa de 2 segundos, respiração consciente]
Quando você aceita o custo da sua liberdade, o medo de errar perde a força. Você não está atrasado; você está, neste exato instante, no ponto exato onde a sua coragem precisa florescer.

[Tom inspirador e encorajador]
Respire fundo, assuma a sua escolha e cuide do seu próprio ritmo. Nos encontramos na próxima reflexão.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Fine art portrait photography representing personal emotional transformation and quiet psychological contemplation, an individual standing near a minimalist wooden window in Zurich looking out at soft morning mist, balanced chiaroscuro lighting, warm wood and cool slate blue aesthetic, contemplative mood, atmospheric depth, Hasselblad H6D-100c, 80mm lens, artistic editorial composition, 8k resolution --ar 16:9 --style raw
        """.trimIndent()
    }

    private fun generateEversonContent(title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

Falar abertamente sobre saúde mental é um dos passos mais urgentes e civilizatórios da medicina contemporânea. Durante décadas, os transtornos psiquiátricos — como a ansiedade crônica, a depressão e as dependências químicas — foram cercados por estigmas, mitos e julgamentos morais que apenas adiaram a busca por auxílio especializado.

## A Neurobiologia por Trás do Sofrimento e o Papel da Ciência

A mente não habita um éter abstrato; ela é uma expressão direta de circuitos neurais, neurotransmissores e respostas imunológicas complexas. Como observamos na prática clínica: *$idea*. 

Quando um paciente desenvolve um quadro de Transtorno de Ansiedade Generalizada (TAG) ou uma crise depressiva maior, ocorrem alterações mensuráveis na neurotransmissão serotoninérgica, noradrenérgica e dopaminérgica, além de um estado inflamatório e de hiperativação do eixo hipotálamo-hipófise-adrenal. 

O tratamento medicamentoso moderno não busca "mudar a personalidade" ou "entorpecer" o indivíduo, mas restaurar o equilíbrio biológico para que ele possa pensar com clareza, sentir com proporcionalidade e reconstruir suas rotinas de vida com autonomia.

## A Abordagem Integrada: Medicação, Psicoterapia e Sobriedade

Nenhum fármaco atua sozinho de forma sustentável. A verdadeira recuperação e o caminho da sobriedade sustentável exigem a combinação virtuosa de prescrição técnica judiciosa, acompanhamento psicoterápico contínuo, higiene do sono, atividade física e, acima de tudo, um ambiente de acolhimento seguro livre de preconceitos. Buscar ajuda psiquiátrica é um ato de profunda coragem e autopreservação.

---
**Meta-descrição:** Entenda as bases científicas e o tratamento humanizado para $title, desmistificando a psiquiatria com o Dr. Everson Buchi.
**Tags:** Psiquiatria Humanizada, Dr. Everson Buchi, Saúde Mental, Ansiedade Generalizada, Depressão, Sobriedade, Ciência Médica.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom seguro, acolhedor e cientificamente embasado]
Olá a todos. Sou o Dr. Everson Buchi, e este é o nosso espaço dedicado à compreensão lúcida e sem preconceitos da saúde mental.

[Pausa de 1 segundo]
No encontro de hoje, abordamos: **$title**.

[Tom empático e didático]
Quero começar lembrando que o cérebro é um órgão do corpo humano tão vulnerável a desequilíbrios químicos e inflamatórios quanto o coração ou a tireoide. Ninguém escolhe sentir uma crise de pânico ou perder a energia vital na depressão. Como sempre reforço: $idea.

[Pausa reflexiva]
O tratamento psiquiátrico atual é personalizado e seguro. O objetivo nunca é calar as suas emoções, mas resgatar a sua dignidade e a sua clareza de pensamento para que você possa viver plenamente.

[Tom afetuoso e tranquilizador]
Se você ou alguém que você ama está enfrentando noites difíceis, saiba que existe saída respaldada pela ciência. Procure um profissional de confiança. Cuidar da mente é honrar a vida. Até o nosso próximo encontro.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Cinematic scientific medical illustration showing serene human neurological balance, abstract glowing neural synapses blending smoothly into soft organic botanical leaves, clean soothing clinical colors with deep slate navy and soft jade green tones, peaceful calming atmosphere, 3D medical visualization style, 8k resolution, minimalist elegance --ar 16:9
        """.trimIndent()
    }

    private fun generateTrilhasContent(title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

Há uma sabedoria silenciosa que só se revela quando os pés tocam a terra batida e a respiração se compassada com a inclinação da encosta. Subir uma montanha — seja na densa e úmida imponência da Serra do Mar paranaense, seja diante das cristas gélidas e recortadas dos Alpes Suíços — é uma prática tanto física quanto profundamente filosófica.

## O Corpo em Movimento como Laboratório da Mente

Na trilha, toda ilusão de controle desmorona. A rocha molhada, a raiz exposta e a névoa súbita exigem presença absoluta no aqui e no agora. Como meditamos nesta jornada: *$idea*.

Os estoicos já nos ensinavam que a virtude reside em saber distinguir o que depende de nós daquilo que nos escapa. Diante do cume envolto em tempestade, não cabe lutar contra o vento; cabe ajustar o casaco, verificar o passo e aceitar a lentidão necessária da subida. O montanhismo desconstrói a pressa contemporânea e nos devolve a proporcionalidade da nossa existência perante a imensidão da natureza.

## O Cume e a Revelação do Retorno

Chegar ao ponto mais alto não é uma conquista sobre a montanha, mas uma vitória sobre a dispersão interna. A fotografia de montanha, nesse contexto, torna-se um exercício de reverência: capturar a luz que se despede na crista rochosa é eternizar o lembrete de que as maiores belezas da vida não podem ser compradas, apenas caminhadas.

---
**Meta-descrição:** Uma jornada reflexiva entre o montanhismo no Sul do Brasil e as cristas alpinas da Suíça: $title. Filosofia, esforço físico e contemplação.
**Tags:** Filosofia e Trilhas, Montanhismo, Serra do Mar, Alpes Suíços, Fotografia de Paisagem, Estoicismo, Natureza.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom contemplativo, calmo, com respiração natural de quem caminha]
Bem-vindo à trilha. Respire o ar fresco das encostas. Aqui é o Filosofia e Trilhas.

[Pausa de 2 segundos, som imaginado de passos em cascalho e vento]
O tema dos nossos passos hoje é: **$title**.

[Tom descritivo e reflexivo, voz aveludada]
Existe um momento da caminhada em que o cansaço das pernas deixa de ser dor e se transforma em foco absoluto. Quando a névoa baixa e o silêncio da cordilheira envolve tudo ao redor, o mundo ruidoso das cidades silencia dentro de nós. Como escrevi em minhas anotações de bordo: $idea.

[Pausa contemplativa de 2 segundos]
A montanha não tem pressa para ser contemplada. Ela nos ensina que a paciência de cada passada firme é a única ponte verdadeira entre o chão que pisamos e a vastidão do céu.

[Tom suave, inspirador]
Mantenha o olhar no horizonte, respeite os seus limites e caminhe com propósito. Nos vemos no próximo cume.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Breathtaking majestic mountain landscape photograph, sharp jagged alpine peaks emerging through rolling ocean of white clouds at dawn, golden morning sunlight grazing the granite ridges, solitary hiker standing on a high rocky overlook gazing at the panoramic view, misty atmospheric perspective, National Geographic style photography, Sony A7R V, 24-70mm lens, f/8, crisp textures, ultra-detailed, 8k resolution --ar 16:9 --style raw
        """.trimIndent()
    }

    private fun generateRiedenContent(title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

Quando as folhas de faia e carvalho começam a dourar nas encostas do Linthgebiet e o ar fresco dos pré-Alpes desce suavemente sobre a pacata vila de Rieden, um sentimento acolhedor de pertencimento preenche as ruas e as casas. Na Suíça rural, as estações do ano não são apenas datas no calendário; são rituais vivos de comunidade, amizade e memória.

## O Encontro Comunitário no Restaurante Speer

No coração desse cotidiano acolhedor está o clássico **Restaurante Speer**, um ponto de encontro histórico onde gerações de moradores e visitantes se reúnem para compartilhar as tradicionais especialidades da culinária regional suíça. Como ressaltamos em nossa crônica: *$idea*.

O aroma reconfortante dos pratos típicos servidos com o queijo da região, o tilintar das taças de vinho local e as conversas animadas entre vizinhos que se conhecem pelo nome há décadas criam uma atmosfera nostálgica e preciosa. Em uma era de conexões digitais efêmeras, sentar-se à mesa de madeira do Speer é um lembrete vívido da importância das raízes e do convívio caloroso de carne e osso.

## A Poesia das Pequenas Tradições

A vida em Rieden segue um compasso próprio, guiado pelo respeito à história local, pela preservação das festividades tradicionais e pelo cuidado mútuo entre as famílias. Celebrar esses momentos na *Rieden Zeitung* é manter acesa a chama de um vilarejo que sabe acolher o futuro sem jamais esquecer a nobreza de sua gente e de seus sabores.

---
**Meta-descrição:** Crônicas e memórias locais de Rieden e da região de Linthgebiet, destacando a tradição gastronômica e o convívio comunitário no Restaurante Speer.
**Tags:** Rieden, Linthgebiet, Restaurante Speer, Suíça, Vida Comunitária, Tradições Locais, Rieden Zeitung.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom caloroso, comunitário, nostálgico e muito acolhedor]
Grüezi e muito bem-vindo a mais uma crônica da nossa Rieden Zeitung.

[Pausa amigável, sorriso perceptível na voz]
Hoje abrimos as portas para uma história de aconchego e sabor: **$title**.

[Tom nostálgico e descritivo]
Quem já caminhou pelas ruelas de Rieden ao entardecer sabe o encanto que tem a fumaça subindo das lareiras e a luz dourada batendo nas janelas de madeira. E quando o destino é o Restaurante Speer, o convite à conversa se torna irresistível. Como relembramos hoje: $idea.

[Pausa calorosa de 1 segundo]
É em torno dessas mesas generosas, com uma boa conversa de vizinho e o respeito às nossas tradições no Linthgebiet, que a vida comunitária revela seu valor mais duradouro e bonito.

[Tom de abraço comunitário]
Que você desfrute desse espírito acolhedor onde quer que esteja. Um abraço afetuoso de Rieden, e até a nossa próxima edição.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Charming Swiss alpine village street scene in Rieden, Linthgebiet Switzerland during autumn, traditional Swiss chalets with flower boxes, golden autumn trees, majestic Speer mountain peak in the background under soft atmospheric morning light, cozy historic restaurant with warm amber lights glowing from windows, cinematic atmospheric realism, Leica Q3, 28mm lens, f/2.8, warm cozy nostalgic mood, 8k resolution --ar 16:9 --style raw
        """.trimIndent()
    }

    private fun generateGenericContent(recipient: BlogRecipient, title: String, idea: String): String {
        return """
## 📝 1. TEXTO: ARTIGO WORDPRESS (SEO)

# $title

Este conteúdo foi formulado para o canal **${recipient.name}**, integrando o ecossistema de publicações multimídia do Grupo Ondas Médias de Comunicação.

## Análise e Desenvolvimento do Tema

Explorando os pontos destacados: *$idea*. A abordagem deste artigo segue rigorosamente a diretriz editorial estipulada para o canal, mantendo um tom de voz ${recipient.tone} e foco prioritário no nicho de ${recipient.niche}.

Aprofundando os conceitos essenciais com clareza, relevância e consistência com os princípios éticos e informativos da rede Buchi.

---
**Meta-descrição:** Artigo exclusivo sobre $title para ${recipient.name}.
**Tags:** ${recipient.tags.joinToString(", ")}.

## 🎙️ 2. ÁUDIO: ROTEIRO DE VOZ (TTS / PODCAST)

[Tom ${recipient.tone}]
Olá, seja muito bem-vindo. Hoje abordamos um tema de especial relevância para o nosso público: **$title**.

[Pausa reflexiva]
Como articulamos em nossa reflexão principal: $idea. Acompanhe nossas atualizações e compartilhe esta mensagem.

## 🎨 3. IMAGEM: PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)
Editorial cinematic concept art representing $title, themed around ${recipient.niche}, sophisticated color harmony with deep charcoal slate and oceanic blue tones, professional lighting, photorealistic, 8k resolution --ar 16:9
        """.trimIndent()
    }
}
