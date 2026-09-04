package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun RecipientSelector(
    selectedRecipient: BlogRecipient,
    onSelectRecipient: (BlogRecipient) -> Unit,
    showDetails: Boolean,
    onToggleDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("recipient_selector_section")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AlternateEmail,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "1. SERVIÇO DE E-MAIL (DESTINO AUTOMAÇÃO)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    letterSpacing = 0.5.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = EditorialCobaltLight.copy(alpha = 0.6f),
                border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f)),
                modifier = Modifier.clickable { onToggleDetails() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (showDetails) "Ocultar Diretrizes" else "Ver Diretrizes",
                        fontSize = 11.sp,
                        color = EditorialNavyDeep,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (showDetails) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = EditorialNavyDeep,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal chips for quick recipient selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BlogRecipient.ALL_RECIPIENTS.forEach { recipient ->
                val isSelected = recipient.id == selectedRecipient.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectRecipient(recipient) },
                    label = {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = recipient.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isSelected) EditorialNavyDeep else EditorialInk
                            )
                            Text(
                                text = recipient.email,
                                fontSize = 10.sp,
                                color = if (isSelected) EditorialCobalt else EditorialInkMuted
                            )
                        }
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EditorialCobalt,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EditorialCobaltLight,
                        selectedLabelColor = EditorialNavyDeep,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) EditorialCobalt else EditorialBorder,
                        selectedBorderColor = EditorialCobalt,
                        borderWidth = if (isSelected) 1.5.dp else 1.dp,
                        enabled = true,
                        selected = isSelected
                    ),
                    modifier = Modifier.testTag("chip_${recipient.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active profile preview card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("active_recipient_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, EditorialBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedRecipient.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        color = EditorialNavyDeep
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = EditorialCobalt,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Tom: ${selectedRecipient.tone}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EditorialCobalt
                    )
                }

                AnimatedVisibility(visible = showDetails) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "Nicho Editorial:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialInkMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedRecipient.niche,
                            fontSize = 12.sp,
                            color = EditorialInk,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            selectedRecipient.tags.take(3).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = EditorialSurfaceVariant,
                                    border = BorderStroke(0.5.dp, EditorialBorder)
                                ) {
                                    Text(
                                        text = "#$tag",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = EditorialNavyMid,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
