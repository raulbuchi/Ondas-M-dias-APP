package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterHdr
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BlogRecipient
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.EditorialNavyMid
import com.example.ui.theme.EditorialSurfaceVariant

@Composable
fun BlogDrawerContent(
    selectedBlog: BlogRecipient?,
    onSelectBlog: (BlogRecipient?) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFF7FAFE),
        drawerContentColor = EditorialInk,
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .testTag("blog_drawer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE2EEFC),
                                Color(0xFFF7FAFE)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, EditorialCobalt, CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_ondas_logo),
                                contentDescription = "Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "ECOSSISTEMA BUCHI",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialNavyDeep,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Plataforma de Blogs & Mídia",
                                fontSize = 11.sp,
                                color = EditorialCobalt,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Selecione um blog para acessar sua área exclusiva, gerenciar matérias e publicar conteúdos direcionados.",
                        fontSize = 11.sp,
                        color = EditorialInkMuted,
                        lineHeight = 16.sp
                    )
                }
            }

            HorizontalDivider(color = EditorialBorder, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // Option 0: Visão Geral / Inicio
            val isHomeSelected = selectedBlog == null
            NavigationDrawerItem(
                label = {
                    Column {
                        Text(
                            text = "Visão Geral das Ferramentas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHomeSelected) EditorialNavyDeep else EditorialInk
                        )
                        Text(
                            text = "Gerador Multimodal, Feed Ao Vivo & Pesquisas",
                            fontSize = 10.sp,
                            color = if (isHomeSelected) EditorialCobalt else EditorialInkMuted
                        )
                    }
                },
                selected = isHomeSelected,
                onClick = {
                    onSelectBlog(null)
                    onCloseDrawer()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = if (isHomeSelected) EditorialNavyDeep else EditorialCobalt
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = EditorialCobaltLight,
                    unselectedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .testTag("drawer_item_home")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Section Label
            Text(
                text = "BLOGS DO ECOSSISTEMA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EditorialCobalt,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Loop through all Recipients
            BlogRecipient.ALL_RECIPIENTS.forEach { blog ->
                val isSelected = selectedBlog?.id == blog.id
                val icon = getBlogIcon(blog.id)

                NavigationDrawerItem(
                    label = {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                            Text(
                                text = blog.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) EditorialNavyDeep else EditorialInk,
                                modifier = Modifier.fillMaxWidth()
                            )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = blog.email,
                                fontSize = 10.sp,
                                color = if (isSelected) EditorialCobalt else EditorialInkMuted
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = {
                        onSelectBlog(blog)
                        onCloseDrawer()
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) EditorialNavyDeep else EditorialCobalt,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    badge = {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = if (isSelected) EditorialNavyDeep else EditorialInkMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = EditorialCobaltLight,
                        unselectedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("drawer_blog_${blog.id}")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RssFeed,
                            contentDescription = null,
                            tint = EditorialCobaltLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rede Ondas Médias & Buchi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "5 canais de publicação integrados com inteligência artificial para geração de matérias, roteiros para rádio e mídias sociais.",
                        fontSize = 10.sp,
                        color = EditorialCobaltLight,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

private fun getBlogIcon(id: String): ImageVector {
    return when (id) {
        "politica" -> Icons.Default.Newspaper
        "raulbuchi" -> Icons.Default.Psychology
        "everson" -> Icons.Default.MedicalServices
        "trilhas" -> Icons.Default.FilterHdr
        "rieden" -> Icons.Default.Language
        else -> Icons.Default.RssFeed
    }
}
