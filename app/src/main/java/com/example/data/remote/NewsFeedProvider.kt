package com.example.data.remote

import com.example.data.model.NewsItemEntity

object NewsFeedProvider {

    fun getDefaultNews(): List<NewsItemEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            NewsItemEntity(
                id = 1,
                title = "Obras da Orla de Matinhos Avançam com Nova Etapa de MacroDrenagem e Urbanização",
                category = "INFRAESTRUTURA",
                location = "Matinhos",
                summary = "A revitalização da orla marítima de Matinhos atinge 88% de conclusão. As obras de macrodrenagem dos canais de Caiobá entram na fase final para mitigar ressacas e alagamentos no próximo verão.",
                fullContent = """
                    A revitalização da orla marítima de Matinhos, no Litoral do Paraná, avança para a fase decisiva de engenharia costeira e urbanística. Segundo o balanço divulgado pelo Governo do Estado e pela Prefeitura de Matinhos, a obra atinge 88% de execução total.
                    
                    Os destaques desta etapa incluem o assentamento do novo calçadão nas praias de Caiobá e do Flórida, a instalação do iluminação em LED com postes reforçados contra salobra e o avanço da macrodrenagem no canal da Avenida Juscelino Kubitschek.
                    
                    A intervenção na macrodrenagem tem como objetivo principal erradicar os históricos pontos de alagamento causados pelas chuvas intensas e ressacas no litoral. As novas estruturas de amortecimento de cheias já passaram pelos primeiros testes hidráulicos com sucesso.
                    
                    Segundo engenheiros responsáveis, o novo calçadão contará com ciclovia contínua, áreas para quiostes padronizados e paisagismo nativo com restinga preservada, transformando o turismo e valorizando o comércio de Matinhos.
                """.trimIndent(),
                source = "Portal Ondas Médias - Matinhos",
                timestamp = now - (12 * 60 * 1000) // 12 mins ago
            ),
            NewsItemEntity(
                id = 2,
                title = "Assembleia Legislativa Aprova Incentivos para Infraestrutura Portuária e Logística no Litoral",
                category = "POLÍTICA",
                location = "Paraná / Litoral",
                summary = "Deputados aprovaram em segundo turno o projeto de lei que cria a Zona de Desenvolvimento Logístico do Litoral Paranaense, dinamizando investimentos nos portos de Paranaguá e Antonina.",
                fullContent = """
                    A Assembleia Legislativa do Estado do Paraná (ALEP) aprovou em segunda votação o projeto de lei que institui o Programa de Apoio ao Desenvolvimento Logístico do Litoral Paranaense.
                    
                    A proposta visa atrair novos investimentos privados para a infraestrutura de apoio ao porto, incluindo pátios de triagem modernizados, ferrovias regionais e melhorias no fluxo rodoviário da BR-277 e PR-407.
                    
                    Para o município de Matinhos e Litoral Sul, a medida projeta a geração de mais de 1.200 empregos diretos e indiretos nos setores de transporte, manutenção náutica e serviços. Lideranças políticas comemoraram a aprovação unânime do texto.
                """.trimIndent(),
                source = "Agência Estadual de Notícias / ALEP",
                timestamp = now - (35 * 60 * 1000) // 35 mins ago
            ),
            NewsItemEntity(
                id = 3,
                title = "Prefeitura de Matinhos Lança Programa de Atendimento em Saúde no Bairro Sertãozinho",
                category = "GESTÃO GOVERNAMENTAL",
                location = "Matinhos",
                summary = "A Secretaria Municipal de Saúde de Matinhos inaugurou o mutirão 'Saúde Perto de Você', ampliando a oferta de consultas especializadas, exames laboratoriais e odontologia no Litoral.",
                fullContent = """
                    A Prefeitura Municipal de Matinhos deu início nesta manhã ao programa 'Saúde Perto de Você', uma ação itinerante criada pela Secretaria Municipal de Saúde para descentralizar o atendimento médico de especialidades.
                    
                    A primeira etapa atende os moradores do bairro Sertãozinho e regiões vizinhas. Estão sendo disponibilizadas consultas com cardiologistas, pediatras, dermatologistas e exames preventivos, além de vacinação contra a gripe e dengue.
                    
                    A Prefeita de Matinhos destacou que a ampliação das equipes de Estratégia Saúde da Família (ESF) reduziu em 40% a fila de espera na UPA de Matinhos nos últimos três meses.
                """.trimIndent(),
                source = "Secretaria de Comunicação de Matinhos",
                timestamp = now - (75 * 60 * 1000) // 1h15m ago
            ),
            NewsItemEntity(
                id = 4,
                title = "Projeto de Preservação Marinha em Matinhos Recebe Selo de Sustentabilidade Ambiental",
                category = "SOCIEDADE",
                location = "Matinhos / Caiobá",
                summary = "Iniciativa comunitária em parceria com pescadores locais recupera ecossistema de restinga e vida marinha no Pico de Matinhos e Baía de Guaratuba.",
                fullContent = """
                    O projeto 'Litoral Vivo', criado por pesquisadores locais em parceria com a colônia de pescadores Z-8 de Matinhos, recebeu o certificado de excelência sustentável do Instituto Água e Terra (IAT).
                    
                    A iniciativa realiza o monitoramento participativo de tartatugas marinhas e golfinhos, além do plantio de espécies nativas de restinga nas dunas recuperadas da praia central de Matinhos.
                    
                    O reconhecimento estimula o ecoturismo na região e fortalece a conscientização de moradores e turistas no cuidado com a orla.
                """.trimIndent(),
                source = "Portal Ondas Médias - Sociedade",
                timestamp = now - (150 * 60 * 1000) // 2h30m ago
            ),
            NewsItemEntity(
                id = 5,
                title = "Ponte de Guaratuba Chega a 60% de Conclusão e Promove Mobilidade Entre Matinhos e Guaratuba",
                category = "INFRAESTRUTURA",
                location = "Litoral PR",
                summary = "A construção da Ponte de Guaratuba bate novo recorde com a colocação de vigas de sustentação e avanço no canal de navegação, com entrega prevista para 2026.",
                fullContent = """
                    A tão aguardada Ponte de Guaratuba alcançou a marca de 60% de suas obras executadas, segundo boletim oficial de medição rodoviária do Paraná.
                    
                    A estrutura de 1.244 metros sobre a baía integrará definitivamente os municípios de Matinhos e Guaratuba, eliminando a dependência histórica do sistema de ferry-boat e encurtando o tempo de viagem na temporada de verão de horas para poucos minutos.
                    
                    Comerciantes e empresários de Matinhos projetam aumento no fluxo turístico ao longo do ano inteiro.
                """.trimIndent(),
                source = "DER-PR / Governo do Estado",
                timestamp = now - (240 * 60 * 1000) // 4h ago
            )
        )
    }
}
