package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsItemEntity
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialSurfaceVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewsCard(
    news: NewsItemEntity,
    onToggleBookmark: () -> Unit,
    onSendToGenerator: (title: String, summary: String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val categoryColor = when (news.category) {
        "POLÍTICA" -> EditorialNavyDeep
        "GESTÃO GOVERNAMENTAL" -> EditorialCobalt
        "INFRAESTRUTURA" -> Color(0xFF0A5844)
        "SOCIEDADE" -> Color(0xFF6E431B)
        else -> EditorialNavyDeep
    }

    val timeFormatted = remember(news.timestamp) {
        val diffMins = (System.currentTimeMillis() - news.timestamp) / (1000 * 60)
        if (diffMins < 60) {
            "Há $diffMins min"
        } else if (diffMins < 24 * 60) {
            "Há ${diffMins / 60} h"
        } else {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(news.timestamp))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("news_card_${news.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, EditorialBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Category, Location & Bookmark Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = categoryColor,
                    ) {
                        Text(
                            text = news.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EditorialCobaltLight,
                        border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = EditorialCobalt,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = news.location,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EditorialNavyDeep
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = timeFormatted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = EditorialInkMuted
                    )
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (news.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Favoritar notícia",
                            tint = if (news.isBookmarked) EditorialCobalt else EditorialInkMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Larger Title
            Text(
                text = news.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = EditorialNavyDeep,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Source
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Newspaper,
                    contentDescription = null,
                    tint = EditorialInkMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = news.source,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EditorialInkMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Larger Summary Text
            Text(
                text = news.summary,
                fontSize = 14.sp,
                color = EditorialInk,
                lineHeight = 20.sp
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EditorialSurfaceVariant,
                        border = BorderStroke(0.5.dp, EditorialBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = news.fullContent,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = EditorialInk,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { isExpanded = !isExpanded },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, EditorialBorder),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Ocultar Texto" else "Ler Matéria Completa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = EditorialInk
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = EditorialInk,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Button(
                    onClick = { onSendToGenerator(news.title, news.summary) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Usar no Gerador",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
