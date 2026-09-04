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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
    onDispatchN8n: () -> Unit,
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
                    text = "2. ASSUNTO DO E-MAIL (SUBJECT)",
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
                    text = "Exemplo de Assunto",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EditorialCobalt
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title / Subject Input
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Assunto (Subject para ${selectedRecipient.email})", fontSize = 13.sp) },
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

        // Optional Idea / Context Input
        OutlinedTextField(
            value = idea,
            onValueChange = onIdeaChange,
            label = { Text("Mensagem / Detalhes Adicionais (Opcional)", fontSize = 13.sp) },
            placeholder = { Text(selectedRecipient.exampleIdea, fontSize = 12.sp, color = EditorialInkMuted.copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("idea_input_field"),
            shape = RoundedCornerShape(10.dp),
            minLines = 2,
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EditorialCobalt,
                unfocusedBorderColor = EditorialBorder,
                focusedLabelColor = EditorialCobalt,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main Primary CTA: DISPARAR E-MAIL PARA O N8N
        Button(
            onClick = onDispatchN8n,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("dispatch_n8n_button")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(EditorialNavyDeep, EditorialNavyMid, EditorialCobalt)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DISPARAR AUTOMAÇÃO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Info Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFEBF3FC),
            border = BorderStroke(1.dp, EditorialCobalt.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "O e-mail contendo apenas o assunto será enviado para ${selectedRecipient.email} para disparar a automação no servidor.",
                    fontSize = 11.sp,
                    color = EditorialNavyDeep,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Secondary Option: Generate in-app preview with Gemini
        OutlinedButton(
            onClick = onGenerate,
            enabled = !isLoading,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, EditorialCobalt),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("generate_preview_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = EditorialCobalt,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gerando Prévia...", fontSize = 12.sp, color = EditorialCobalt)
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Gerar Prévia Multimodal no App (Gemini)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EditorialCobalt
                )
            }
        }
    }
}
