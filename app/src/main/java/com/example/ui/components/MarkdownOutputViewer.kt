package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.ParsedOutput
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialNavyMid
import com.example.ui.theme.EditorialSurfaceVariant

@Composable
fun MarkdownOutputViewer(
    output: ParsedOutput,
    activeTab: Int,
    onTabSelected: (Int) -> Unit,
    isSpeaking: Boolean,
    onPlayTts: () -> Unit,
    onStopTts: () -> Unit,
    onCopy: (String, String) -> Unit,
    onShare: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("output_viewer_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, EditorialBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header with Quick Copy All
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EditorialNavyDeep)
                    .padding(horizontal = 16.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SAÍDAS MULTIMODAIS GERADAS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.8.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Artigo SEO • Roteiro de Áudio • Prompt Visual",
                        fontSize = 10.sp,
                        color = EditorialCobaltLight.copy(alpha = 0.9f)
                    )
                }

                Row {
                    IconButton(
                        onClick = { onCopy(output.fullMarkdown, "Markdown Completo") },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("copy_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar Markdown Completo",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onShare("Publicação Ondas Médias", output.fullMarkdown) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("share_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Tabs
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 8.dp,
                containerColor = EditorialSurfaceVariant,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = EditorialCobalt,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { onTabSelected(0) },
                    text = { Text("1. Artigo SEO", fontSize = 12.sp, fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Medium, color = if (activeTab == 0) EditorialNavyDeep else EditorialInkMuted) },
                    icon = { Icon(Icons.Default.Article, null, tint = if (activeTab == 0) EditorialCobalt else EditorialInkMuted, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_article")
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { onTabSelected(1) },
                    text = { Text("2. Roteiro Áudio (TTS)", fontSize = 12.sp, fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Medium, color = if (activeTab == 1) EditorialNavyDeep else EditorialInkMuted) },
                    icon = { Icon(Icons.Default.Mic, null, tint = if (activeTab == 1) EditorialCobalt else EditorialInkMuted, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_audio")
                )
                Tab(
                    selected = activeTab == 2,
                    onClick = { onTabSelected(2) },
                    text = { Text("3. Prompt Imagem", fontSize = 12.sp, fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Medium, color = if (activeTab == 2) EditorialNavyDeep else EditorialInkMuted) },
                    icon = { Icon(Icons.Default.Image, null, tint = if (activeTab == 2) EditorialCobalt else EditorialInkMuted, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_image")
                )
                Tab(
                    selected = activeTab == 3,
                    onClick = { onTabSelected(3) },
                    text = { Text("Markdown Bruto", fontSize = 12.sp, fontWeight = if (activeTab == 3) FontWeight.Bold else FontWeight.Medium, color = if (activeTab == 3) EditorialNavyDeep else EditorialInkMuted) },
                    icon = { Icon(Icons.Default.Code, null, tint = if (activeTab == 3) EditorialCobalt else EditorialInkMuted, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_raw")
                )
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (activeTab) {
                    0 -> ArticleView(
                        text = output.articleText,
                        onCopy = { onCopy(output.articleText, "Artigo SEO") },
                        onShare = { onShare("Artigo WordPress (SEO)", output.articleText) }
                    )
                    1 -> AudioScriptView(
                        script = output.audioScript,
                        isSpeaking = isSpeaking,
                        onPlayTts = onPlayTts,
                        onStopTts = onStopTts,
                        onCopy = { onCopy(output.audioScript, "Roteiro de Áudio") },
                        onShare = { onShare("Roteiro de Voz", output.audioScript) }
                    )
                    2 -> ImagePromptView(
                        prompt = output.imagePrompt,
                        onCopy = { onCopy(output.imagePrompt, "Prompt de Imagem") },
                        onShare = { onShare("Prompt Visual", output.imagePrompt) }
                    )
                    3 -> RawMarkdownView(
                        markdown = output.fullMarkdown,
                        onCopy = { onCopy(output.fullMarkdown, "Markdown Completo") },
                        onShare = { onShare("Publicação Multimodal", output.fullMarkdown) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleView(
    text: String,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EditorialCobaltLight,
                border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "📝 ARTIGO WORDPRESS OTIMIZADO (SEO)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Row {
                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, EditorialCobalt),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, null, tint = EditorialCobalt, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copiar Artigo", fontSize = 11.sp, color = EditorialCobalt)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, EditorialBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                fontSize = 13.5.sp,
                lineHeight = 22.sp,
                color = EditorialInk,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun AudioScriptView(
    script: String,
    isSpeaking: Boolean,
    onPlayTts: () -> Unit,
    onStopTts: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Column {
        // Player Control Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEBF3FC)
            ),
            border = BorderStroke(1.dp, EditorialCobalt.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EditorialCobalt),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ROTEIRO DE VOZ (TTS / PODCAST)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialNavyDeep
                            )
                            Text(
                                text = if (isSpeaking) "Reproduzindo narração via TTS..." else "Pronto para conversão ou leitura",
                                fontSize = 10.sp,
                                color = EditorialCobalt,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row {
                        if (isSpeaking) {
                            Button(
                                onClick = onStopTts,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("stop_tts_button")
                            ) {
                                Icon(Icons.Default.Stop, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Parar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = onPlayTts,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("play_tts_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ouvir TTS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Marcações de expressão inclusas [entre colchetes]:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = EditorialInkMuted
            )

            OutlinedButton(
                onClick = onCopy,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialCobalt),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = EditorialCobalt, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copiar Roteiro", fontSize = 11.sp, color = EditorialCobalt)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, EditorialBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = script,
                fontSize = 13.5.sp,
                lineHeight = 22.sp,
                color = EditorialInk,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ImagePromptView(
    prompt: String,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EditorialCobaltLight,
                border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "🎨 PROMPT VISUAL (IMAGEN 3 / MIDJOURNEY)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            OutlinedButton(
                onClick = onCopy,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialCobalt),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = EditorialCobalt, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copiar Prompt", fontSize = 11.sp, color = EditorialCobalt)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Pronto para colar em geradores como Imagen 3, Midjourney ou DALL-E (em inglês para máxima fidelidade visual):",
            fontSize = 11.sp,
            color = EditorialInkMuted,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = EditorialNavyDeep,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROMPT FORMATADO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditorialCobaltLight,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Aspect: 16:9 • Style: Editorial / Raw",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = prompt,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun RawMarkdownView(
    markdown: String,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EditorialCobaltLight,
                border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "📋 RESPOSTA COMPLETA EM MARKDOWN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            OutlinedButton(
                onClick = onCopy,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialCobalt),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = EditorialCobalt, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copiar Tudo", fontSize = 11.sp, color = EditorialCobalt)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, EditorialBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = markdown,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp,
                color = EditorialInk,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}
