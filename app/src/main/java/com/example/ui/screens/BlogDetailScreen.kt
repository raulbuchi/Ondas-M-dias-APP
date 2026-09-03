package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterHdr
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BlogRecipient
import com.example.data.model.PublicationEntity
import com.example.data.remote.ContentParser
import com.example.ui.components.MarkdownOutputViewer
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialNavyMid
import com.example.ui.theme.EditorialPaper
import com.example.ui.theme.EditorialSurfaceVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BlogDetailScreen(
    blog: BlogRecipient,
    publications: List<PublicationEntity>,
    onOpenMenu: () -> Unit,
    onBackToHome: () -> Unit,
    onGenerateForBlog: (BlogRecipient) -> Unit,
    onDeletePublication: (Long) -> Unit,
    onPlayTts: (text: String, lang: String) -> Unit,
    onCopyText: (Context, String, String) -> Unit,
    onShareText: (Context, String, String) -> Unit
) {
    val context = LocalContext.current
    val blogPublications = remember(publications, blog.email) {
        publications.filter { it.recipientEmail.equals(blog.email, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("blog_detail_screen")
    ) {
        // Top Header
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
                // Top Action Bar Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onOpenMenu,
                            modifier = Modifier.testTag("blog_screen_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Lateral",
                                tint = EditorialNavyDeep
                            )
                        }

                        IconButton(
                            onClick = onBackToHome,
                            modifier = Modifier.testTag("blog_screen_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar ao Início",
                                tint = EditorialNavyDeep
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Blog Title & Details
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.5.dp, EditorialCobalt),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = getBlogIconVector(blog.id),
                                contentDescription = null,
                                tint = EditorialCobalt,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = blog.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialNavyDeep,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "${blog.organization} • ${blog.email}",
                            fontSize = 11.sp,
                            color = EditorialCobalt,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Niche description
                Text(
                    text = blog.niche,
                    fontSize = 11.5.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tags Row
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    blog.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "#$tag",
                                fontSize = 10.sp,
                                color = EditorialCobaltLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Bar for Blog: "Gerar Nova Publicação"
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialNavyMid),
                border = BorderStroke(1.dp, EditorialCobalt)
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
                            text = "Criar Conteúdo para este Blog",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Tom de voz: ${blog.tone}",
                            fontSize = 11.sp,
                            color = EditorialCobaltLight
                        )
                    }

                    Button(
                        onClick = { onGenerateForBlog(blog) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                        modifier = Modifier.testTag("generate_for_blog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Novo Artigo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ARTIGOS & MATÉRIAS PUBLICADAS (${blogPublications.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Articles List
        if (blogPublications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = EditorialSurfaceVariant,
                    border = BorderStroke(0.5.dp, EditorialBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.RssFeed,
                            contentDescription = null,
                            tint = EditorialInkMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Nenhum artigo publicado ainda para o ${blog.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialNavyDeep
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Clique no botão acima para gerar a primeira matéria completa com áudio TTS e prompt de imagem para este portal.",
                            fontSize = 12.sp,
                            color = EditorialInkMuted,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { onGenerateForBlog(blog) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt)
                        ) {
                            Text("Gerar Primeira Matéria Agora", fontSize = 12.sp, color = Color.White)
                        }
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
                items(blogPublications, key = { it.id }) { item ->
                    BlogArticleCard(
                        publication = item,
                        blog = blog,
                        onDelete = { onDeletePublication(item.id) },
                        onPlayTts = onPlayTts,
                        onCopyText = onCopyText,
                        onShareText = onShareText
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun BlogArticleCard(
    publication: PublicationEntity,
    blog: BlogRecipient,
    onDelete: () -> Unit,
    onPlayTts: (text: String, lang: String) -> Unit,
    onCopyText: (Context, String, String) -> Unit,
    onShareText: (Context, String, String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    val dateFormatted = remember(publication.timestamp) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(publication.timestamp))
    }

    val parsedOutput = remember(publication.fullMarkdown) {
        ContentParser.parse(publication.fullMarkdown)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("blog_article_card_${publication.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, EditorialBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Date & Delete row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Publicado em $dateFormatted",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = EditorialInkMuted
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Color.Red.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title
            Text(
                text = publication.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = EditorialNavyDeep,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Raw Idea Excerpt
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EditorialSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pauta base: ${publication.rawIdea}",
                    fontSize = 11.sp,
                    color = EditorialInkMuted,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Markdown Content
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    MarkdownOutputViewer(
                        output = parsedOutput,
                        activeTab = 0,
                        onTabSelected = {},
                        isSpeaking = false,
                        onPlayTts = {
                            val lang = if (blog.id == "rieden") "de" else "pt"
                            onPlayTts(parsedOutput.audioScript, lang)
                        },
                        onStopTts = {},
                        onCopy = { text, label -> onCopyText(context, text, label) },
                        onShare = { title, text -> onShareText(context, title, text) }
                    )
                }
            }

            // Quick Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { isExpanded = !isExpanded },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, EditorialBorder),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Ocultar Matéria" else "Ler Conteúdo Gerado",
                        fontSize = 11.sp,
                        color = EditorialInk
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = EditorialInk,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val lang = if (blog.id == "rieden") "de" else "pt"
                            onPlayTts(parsedOutput.audioScript, lang)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Ouvir Áudio Rádio",
                            tint = EditorialCobalt,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onCopyText(context, publication.fullMarkdown, "Matéria Completa") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar",
                            tint = EditorialInkMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { onShareText(context, publication.title, publication.fullMarkdown) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar",
                            tint = EditorialInkMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getBlogIconVector(id: String): ImageVector {
    return when (id) {
        "politica" -> Icons.Default.Newspaper
        "raulbuchi" -> Icons.Default.Psychology
        "everson" -> Icons.Default.MedicalServices
        "trilhas" -> Icons.Default.FilterHdr
        "rieden" -> Icons.Default.Language
        else -> Icons.Default.RssFeed
    }
}
