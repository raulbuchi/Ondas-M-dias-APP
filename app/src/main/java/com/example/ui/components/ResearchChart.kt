package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.EditorialSurfaceVariant
import org.json.JSONArray

@Composable
fun ResearchChart(survey: ResearchSurveyEntity) {
    val optionsList = remember(survey.optionsJson) {
        val list = mutableListOf<SurveyOption>()
        try {
            val jsonArray = JSONArray(survey.optionsJson)
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
            // Fallback parsing if needed
        }
        list
    }

    val defaultColors = listOf(
        EditorialNavyDeep,
        EditorialCobalt,
        Color(0xFF0A5844),
        Color(0xFF8D5B18),
        Color(0xFF5A626C)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, EditorialBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title & Reg Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = survey.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditorialCobalt,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = survey.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = EditorialNavyDeep
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${survey.institute} (${survey.registrationId})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = EditorialInkMuted
                )
                Text(
                    text = "${survey.region} • ${survey.datePeriod}",
                    fontSize = 11.sp,
                    color = EditorialInkMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Technical Specs Box
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = EditorialSurfaceVariant,
                border = BorderStroke(0.5.dp, EditorialBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Amostra: ${survey.sampleSize} entrevistados",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EditorialNavyDeep
                    )
                    Text(text = "•", fontSize = 11.sp, color = EditorialBorder)
                    Text(
                        text = "Margem de Erro: ±${survey.marginOfError}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EditorialCobalt
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bar Charts Section
            optionsList.forEachIndexed { index, option ->
                val barColor = remember(option.colorHex) {
                    option.colorHex?.let {
                        try {
                            Color(android.graphics.Color.parseColor(it))
                        } catch (e: Exception) {
                            defaultColors[index % defaultColors.size]
                        }
                    } ?: defaultColors[index % defaultColors.size]
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option.name,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EditorialInk,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "%.1f%%".format(option.percentage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = barColor
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Percentage Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(EditorialSurfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = (option.percentage / 100.0).toFloat().coerceIn(0.02f, 1.0f))
                                .clip(RoundedCornerShape(6.dp))
                                .background(barColor)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
