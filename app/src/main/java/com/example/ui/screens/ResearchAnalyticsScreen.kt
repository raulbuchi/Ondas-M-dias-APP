package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import com.example.util.PdfReportExporter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResearchSurveyEntity
import com.example.data.model.SurveyOption
import com.example.ui.components.AddSurveyDialog
import com.example.ui.components.CanvasDonutChart
import com.example.ui.components.CanvasMarginBarChart
import com.example.ui.components.DashboardKpiOverview
import com.example.ui.components.ResearchChart
import org.json.JSONArray
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialNavyMid
import com.example.ui.theme.EditorialPaper
import com.example.ui.theme.EditorialSurfaceVariant

@Composable
fun ResearchAnalyticsScreen(
    surveys: List<ResearchSurveyEntity>,
    selectedSurveyId: Long?,
    onSelectSurvey: (Long) -> Unit,
    onAddSurvey: (ResearchSurveyEntity) -> Unit,
    onGenerateReport: (ResearchSurveyEntity) -> Unit,
    isGeneratingReport: Boolean,
    onSendToGenerator: (title: String, idea: String) -> Unit
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("Todas") }

    val categories = remember(surveys) {
        listOf("Todas") + surveys.map { it.category }.distinct()
    }

    val filteredSurveys = remember(surveys, selectedCategoryFilter) {
        if (selectedCategoryFilter == "Todas") {
            surveys
        } else {
            surveys.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    val currentSurvey = remember(filteredSurveys, selectedSurveyId) {
        filteredSurveys.find { it.id == selectedSurveyId } ?: filteredSurveys.firstOrNull()
    }

    // Parse options for canvas charts
    val currentOptions = remember(currentSurvey?.optionsJson) {
        val list = mutableListOf<SurveyOption>()
        currentSurvey?.optionsJson?.let { jsonStr ->
            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        SurveyOption(
                            name = obj.getString("name"),
                            percentage = obj.getDouble("percentage"),
                            colorHex = if (obj.has("colorHex") && !obj.isNull("colorHex")) obj.getString("colorHex") else null
                        )
                    )
                }
            } catch (e: Exception) {
                // Fallback
            }
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("research_analytics_screen")
    ) {
        // Header Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFEBF3FC),
            border = BorderStroke(1.dp, EditorialCobalt.copy(alpha = 0.25f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = EditorialCobalt,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DASHBOARD DE PESQUISAS & DATA ANALYTICS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditorialNavyDeep,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Grupo Ondas Médias | Eleições, Avaliação Governamental e Obras de Infraestrutura",
                    fontSize = 11.sp,
                    color = EditorialCobalt,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Dashboard KPI Overview
        DashboardKpiOverview(
            surveys = surveys,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Category Filter Chips & Import Button Row
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtrar Categorias de Pesquisa:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep
                )

                OutlinedButton(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, EditorialCobalt),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("add_survey_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = EditorialCobalt,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Importar Dados",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditorialCobalt
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isCatSelected = selectedCategoryFilter == cat
                    FilterChip(
                        selected = isCatSelected,
                        onClick = { selectedCategoryFilter = cat },
                        label = {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EditorialCobaltLight,
                            selectedLabelColor = EditorialNavyDeep,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isCatSelected) EditorialCobalt else EditorialBorder,
                            selectedBorderColor = EditorialCobalt,
                            borderWidth = 1.dp,
                            enabled = true,
                            selected = isCatSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pesquisas no Banco de Dados (${filteredSurveys.size}):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EditorialNavyDeep
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Chips Row for selecting specific survey
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredSurveys) { survey ->
                    val isSelected = currentSurvey?.id == survey.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectSurvey(survey.id) },
                        label = {
                            Text(
                                text = survey.title,
                                fontSize = 11.sp,
                                maxLines = 1,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EditorialCobaltLight,
                            selectedLabelColor = EditorialNavyDeep,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) EditorialCobalt else EditorialBorder,
                            selectedBorderColor = EditorialCobalt,
                            borderWidth = 1.dp,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Survey Visualization & Analysis
        currentSurvey?.let { survey ->
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // 1. Standard Research Card
                ResearchChart(survey = survey)

                Spacer(modifier = Modifier.height(14.dp))

                // PDF Export Quick Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("export_pdf_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurfaceVariant),
                    border = BorderStroke(1.dp, EditorialBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Exportar Relatório PDF",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialNavyDeep
                            )
                            Text(
                                text = "Gere um documento PDF formatado ideal para publicação jornalística e impressos.",
                                fontSize = 10.5.sp,
                                color = EditorialInkMuted,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                PdfReportExporter.exportAndShare(context, survey)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                            modifier = Modifier.testTag("download_pdf_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Baixar PDF",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Canvas Donut Chart (Distribution Visualizer)
                if (currentOptions.isNotEmpty()) {
                    CanvasDonutChart(
                        options = currentOptions,
                        marginOfError = survey.marginOfError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Canvas Bar Chart with Error Bounds (Tolerances)
                    CanvasMarginBarChart(
                        options = currentOptions,
                        marginOfError = survey.marginOfError
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }

                // AI Analytical Report Generator Trigger Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialNavyMid),
                    border = BorderStroke(1.dp, EditorialCobalt)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Análise Jornalística Inteligente (Gemini IA)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Gere um relatório analítico informativo sobre a margem de erro, cenário eleitoral/de infraestrutura e frases de efeito para rádio e blogs.",
                            fontSize = 11.5.sp,
                            color = EditorialCobaltLight,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onGenerateReport(survey) },
                            enabled = !isGeneratingReport,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generate_report_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isGeneratingReport) "Analisando Dados..." else "Gerar Relatório Analítico",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Render Generated Report if available
                if (!survey.analyticalReport.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, EditorialBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EditorialCobalt,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Relatório Analítico Consolidado",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        color = EditorialNavyDeep
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EditorialPaper,
                                border = BorderStroke(0.5.dp, EditorialBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = survey.analyticalReport,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = EditorialInk,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onSendToGenerator(
                                            "Análise da Pesquisa: ${survey.title}",
                                            survey.analyticalReport
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Gerador",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Button(
                                    onClick = {
                                        PdfReportExporter.exportAndShare(context, survey)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Baixar PDF",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Add Survey Dialog
        if (showAddDialog) {
            AddSurveyDialog(
                onDismiss = { showAddDialog = false },
                onSaveSurvey = { newSurvey ->
                    onAddSurvey(newSurvey)
                    showAddDialog = false
                }
            )
        }
    }
}
