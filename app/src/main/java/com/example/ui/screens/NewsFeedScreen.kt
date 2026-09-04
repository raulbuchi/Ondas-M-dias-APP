package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsItemEntity
import com.example.ui.components.NewsCard
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialSurfaceVariant

@Composable
fun NewsFeedScreen(
    newsList: List<NewsItemEntity>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit,
    onToggleBookmark: (NewsItemEntity) -> Unit,
    onSendToGenerator: (title: String, summary: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("TODAS") }
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyBookmarked by remember { mutableStateOf(false) }

    val categories = listOf("TODAS", "POLÍTICA", "EM CIMA DA HORA", "MERCADO", "GESTÃO GOVERNAMENTAL", "INFRAESTRUTURA", "SOCIEDADE")

    val filteredNews = remember(newsList, selectedCategory, searchQuery, showOnlyBookmarked) {
        newsList.filter { news ->
            val matchesCategory = when (selectedCategory) {
                "TODAS" -> true
                else -> news.category.contains(selectedCategory, ignoreCase = true) || news.title.contains(selectedCategory, ignoreCase = true)
            }

            val matchesSearch = searchQuery.isBlank() ||
                    news.title.contains(searchQuery, ignoreCase = true) ||
                    news.summary.contains(searchQuery, ignoreCase = true) ||
                    news.source.contains(searchQuery, ignoreCase = true) ||
                    news.location.contains(searchQuery, ignoreCase = true)

            val matchesBookmark = !showOnlyBookmarked || news.isBookmarked

            matchesCategory && matchesSearch && matchesBookmark
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("news_feed_screen")
    ) {
        // Live Banner Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFEBF3FC),
            border = BorderStroke(1.dp, EditorialCobalt.copy(alpha = 0.25f))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00C853))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "NOTÍCIAS AO VIVO (FEEDS RSS)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialNavyDeep,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Folha, CNN Brasil, O Antagonista & Brasil 247",
                                fontSize = 10.sp,
                                color = EditorialCobalt,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, EditorialCobalt),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = EditorialCobalt,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Atualizar Feed",
                                tint = EditorialCobalt,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isRefreshing) "Buscando..." else "Atualizar",
                            fontSize = 11.sp,
                            color = EditorialCobalt,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (isRefreshing) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = EditorialCobalt,
                        trackColor = Color(0xFFD1E4FF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar & Filters
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar nas notícias da Folha, CNN, Antagonista...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = EditorialCobalt) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, null, tint = EditorialInkMuted)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("news_search_input"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EditorialCobalt,
                    unfocusedBorderColor = EditorialBorder,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips Row
            Text(
                text = "FILTRAR POR CATEGORIA:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EditorialInkMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
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

        Spacer(modifier = Modifier.height(10.dp))

        // News Items Count & Favorites Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredNews.size} matérias ao vivo no feed",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = EditorialInkMuted
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                FilterChip(
                    selected = showOnlyBookmarked,
                    onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                    label = { Text("Salvas ★", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EditorialCobaltLight,
                        selectedLabelColor = EditorialNavyDeep
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // News List
        if (filteredNews.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = EditorialSurfaceVariant,
                border = BorderStroke(0.5.dp, EditorialBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Newspaper,
                        contentDescription = null,
                        tint = EditorialInkMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nenhuma notícia encontrada para os filtros selecionados",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = EditorialInkMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onRefresh) {
                        Text("Carregar feeds RSS em tempo real", fontSize = 12.sp)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredNews, key = { it.id }) { news ->
                    NewsCard(
                        news = news,
                        onToggleBookmark = { onToggleBookmark(news) },
                        onSendToGenerator = onSendToGenerator
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

