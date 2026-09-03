package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep

@Composable
fun ApiKeyDialog(
    currentCustomKey: String,
    onSaveKey: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var keyInput by remember { mutableStateOf(currentCustomKey) }
    val hasBuildConfigKey = runCatching { BuildConfig.GEMINI_API_KEY }.getOrNull()?.let {
        it.isNotBlank() && it != "MY_GEMINI_API_KEY"
    } == true

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Motor de IA (Gemini 2.5 Flash)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = EditorialNavyDeep
                )
            }
        },
        text = {
            Column {
                if (hasBuildConfigKey) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EditorialCobaltLight,
                        border = BorderStroke(0.5.dp, EditorialCobalt.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = EditorialCobalt, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chave GEMINI_API_KEY detectada no ambiente!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EditorialNavyDeep
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    text = "O app utiliza o modelo gemini-2.5-flash com system prompt dedicado para os 5 blogs do Ecossistema Digital Buchi.",
                    fontSize = 12.sp,
                    color = EditorialInkMuted,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("Chave API Personalizada (Opcional)", fontSize = 12.sp) },
                    placeholder = { Text("AIzaSy...", fontSize = 12.sp, color = EditorialInkMuted.copy(alpha = 0.5f)) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialCobalt,
                        unfocusedBorderColor = EditorialBorder,
                        focusedLabelColor = EditorialCobalt
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Se nenhuma chave for fornecida, o app ativa automaticamente o gerador inteligente baseado no tom do canal para demonstração completa e instantânea.",
                    fontSize = 11.sp,
                    color = EditorialInkMuted.copy(alpha = 0.8f)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveKey(keyInput.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialBorder)
            ) {
                Text("Fechar", color = EditorialInk)
            }
        }
    )
}
