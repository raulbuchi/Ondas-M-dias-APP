package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResearchSurveyEntity
import com.example.data.model.SurveyOption
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialNavyMid
import com.example.ui.theme.EditorialPaper
import com.example.ui.theme.EditorialSurfaceVariant
import org.json.JSONArray

private val DEFAULT_PALETTE = listOf(
    EditorialNavyDeep,
    EditorialCobalt,
    Color(0xFF0D6EFD),
    Color(0xFF198754),
    Color(0xFFD63384),
    Color(0xFFFD7E14),
    Color(0xFF6C757D)
)

@Composable
fun DashboardKpiOverview(
    surveys: List<ResearchSurveyEntity>,
    modifier: Modifier = Modifier
) {
    val totalSurveys = surveys.size
    val totalSample = remember(surveys) { surveys.sumOf { it.sampleSize } }
    val categoriesCount = remember(surveys) { surveys.map { it.category }.distinct().size }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "PAINEL DE MÉTRICAS GERAIS (KPIs)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EditorialCobalt,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 1: Total de Pesquisas
            KpiMetricCard(
                title = "Pesquisas",
                value = "$totalSurveys",
                subtitle = "$categoriesCount categorias",
                icon = Icons.Default.BarChart,
                modifier = Modifier.weight(1f)
            )

            // Card 2: Amostra Total
            KpiMetricCard(
                title = "Entrevistados",
                value = "%,d".format(totalSample).replace(',', '.'),
                subtitle = "Total acumulado",
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f)
            )

            // Card 3: Distribuição
            KpiMetricCard(
                title = "Cobertura",
                value = "100%",
                subtitle = "PR e Região Sul",
                icon = Icons.Default.PieChart,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, EditorialBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialInkMuted
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = EditorialNavyDeep
            )
            Text(
                text = subtitle,
                fontSize = 9.5.sp,
                color = EditorialInkMuted
            )
        }
    }
}

/**
 * Donut Chart rendered natively with Jetpack Compose Canvas
 */
@Composable
fun CanvasDonutChart(
    options: List<SurveyOption>,
    marginOfError: Double,
    modifier: Modifier = Modifier
) {
    var animTrigger by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animTrigger,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(options) {
        animTrigger = 1f
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, EditorialBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Distribuição Proporcional dos Dados",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = EditorialNavyDeep
                )

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = EditorialSurfaceVariant
                ) {
                    Text(
                        text = "Margem ±$marginOfError%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EditorialCobalt,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Donut Canvas
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(120.dp)) {
                        val strokeWidth = 22.dp.toPx()
                        var startAngle = -90f
                        val total = options.sumOf { it.percentage }.toFloat().coerceAtLeast(1f)

                        options.forEachIndexed { index, opt ->
                            val color = parseColorOrDefault(opt.colorHex, index)
                            val sweepAngle = ((opt.percentage.toFloat() / total) * 360f) * animatedProgress

                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                                topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                            )
                            startAngle += sweepAngle
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "100%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialNavyDeep
                        )
                        Text(
                            text = "Total",
                            fontSize = 9.sp,
                            color = EditorialInkMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend List
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        val color = parseColorOrDefault(option.colorHex, index)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = option.name,
                                fontSize = 11.sp,
                                color = EditorialInk,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )
                            Text(
                                text = "%.1f%%".format(option.percentage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bar Chart with Error Bounds (Margin of Error tolerances)
 */
@Composable
fun CanvasMarginBarChart(
    options: List<SurveyOption>,
    marginOfError: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, EditorialBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Gráfico de Barras com Intervalo de Confiança (Tolerância)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = EditorialNavyDeep
            )
            Text(
                text = "Linhas indicam a variação mínima e máxima considerand o a margem de erro de ±$marginOfError%",
                fontSize = 10.5.sp,
                color = EditorialInkMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            options.forEachIndexed { index, option ->
                val barColor = parseColorOrDefault(option.colorHex, index)
                val minVal = (option.percentage - marginOfError).coerceAtLeast(0.0)
                val maxVal = (option.percentage + marginOfError).coerceAtMost(100.0)

                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EditorialNavyDeep,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "%.1f%% (%.1f%% - %.1f%%)".format(option.percentage, minVal, maxVal),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = barColor
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Canvas Horizontal Bar with Error Line overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(EditorialSurfaceVariant)
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                            val w = size.width
                            val h = size.height
                            val barWidth = (option.percentage / 100.0).toFloat() * w

                            // Draw Main Value Fill
                            drawRect(
                                color = barColor,
                                size = Size(barWidth, h)
                            )

                            // Draw Error Interval Line (minVal to maxVal)
                            val minX = (minVal / 100.0).toFloat() * w
                            val maxX = (maxVal / 100.0).toFloat() * w
                            val centerY = h / 2f

                            val lineTint = Color.Black.copy(alpha = 0.8f)

                            // Range bar
                            drawLine(
                                color = lineTint,
                                start = Offset(minX, centerY),
                                end = Offset(maxX, centerY),
                                strokeWidth = 2.dp.toPx()
                            )
                            // Left Cap
                            drawLine(
                                color = lineTint,
                                start = Offset(minX, centerY - 4.dp.toPx()),
                                end = Offset(minX, centerY + 4.dp.toPx()),
                                strokeWidth = 2.dp.toPx()
                            )
                            // Right Cap
                            drawLine(
                                color = lineTint,
                                start = Offset(maxX, centerY - 4.dp.toPx()),
                                end = Offset(maxX, centerY + 4.dp.toPx()),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun parseColorOrDefault(hex: String?, index: Int): Color {
    if (!hex.isNullOrBlank()) {
        try {
            return Color(android.graphics.Color.parseColor(hex))
        } catch (e: Exception) {
            // fallback
        }
    }
    return DEFAULT_PALETTE[index % DEFAULT_PALETTE.size]
}
