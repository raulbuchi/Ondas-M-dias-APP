package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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

@Composable
fun InputSection(
    selectedRecipient: BlogRecipient,
    title: String,
    onTitleChange: (String) -> Unit,
    idea: String,
    onIdeaChange: (String) -> Unit,
    isLoading: Boolean,
    onGenerate: () -> Unit,
    onFillExample: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("input_section")
    ) {
        // Section Header with Example filler button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Title,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "2. TÍTULO & MENSAGEM / IDEIA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep,
                    letterSpacing = 0.5.sp
                )
            }

            OutlinedButton(
                onClick = onFillExample,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialCobalt),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("fill_example_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Exemplo do Canal",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EditorialCobalt
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Título / Tema Central do Post", fontSize = 13.sp) },
            placeholder = { Text(selectedRecipient.exampleTitle, fontSize = 12.sp, color = EditorialInkMuted.copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("title_input_field"),
            shape = RoundedCornerShape(10.dp),
            singleLine = false,
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EditorialCobalt,
                unfocusedBorderColor = EditorialBorder,
                focusedLabelColor = EditorialCobalt,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Idea/Draft Input
        OutlinedTextField(
            value = idea,
            onValueChange = onIdeaChange,
            label = { Text("Mensagem / Ideia (Rascunho, insights ou tópicos)", fontSize = 13.sp) },
            placeholder = { Text(selectedRecipient.exampleIdea, fontSize = 12.sp, color = EditorialInkMuted.copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("idea_input_field"),
            shape = RoundedCornerShape(10.dp),
            minLines = 3,
            maxLines = 6,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EditorialCobalt,
                unfocusedBorderColor = EditorialBorder,
                focusedLabelColor = EditorialCobalt,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Main CTA: Generate
        Button(
            onClick = onGenerate,
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_button")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = if (!isLoading) {
                                listOf(EditorialNavyDeep, EditorialNavyMid, EditorialCobalt)
                            } else {
                                listOf(Color(0xFF7A8490), Color(0xFF5A626C))
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Gerando Conteúdo Multimodal...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GERAR CONTEÚDO MULTIMODAL",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        }
    }
}
