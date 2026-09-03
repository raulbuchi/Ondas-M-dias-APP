package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.ResearchSurveyEntity
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialNavyDeep
import org.json.JSONArray
import org.json.JSONObject

data class OptionInputState(var name: String, var percentage: String)

@Composable
fun AddSurveyDialog(
    onDismiss: () -> Unit,
    onSaveSurvey: (ResearchSurveyEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var institute by remember { mutableStateOf("Instituto Ondas Médias") }
    var regId by remember { mutableStateOf("PR-0000/2026") }
    var category by remember { mutableStateOf("POLÍTICA") }
    var region by remember { mutableStateOf("Matinhos e Litoral do PR") }
    var sampleSize by remember { mutableStateOf("600") }
    var marginOfError by remember { mutableStateOf("3.5") }
    var datePeriod by remember { mutableStateOf("Setembro 2026") }

    val optionsList = remember {
        mutableStateListOf(
            OptionInputState("Candidato / Opção A", "40.0"),
            OptionInputState("Candidato / Opção B", "35.0"),
            OptionInputState("Indecisos / Brancos", "25.0")
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = EditorialCobalt,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Cadastrar / Importar Pesquisa",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = EditorialNavyDeep
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da Pesquisa", fontSize = 12.sp) },
                    placeholder = { Text("Ex: Intenção de Voto - Prefeitura de Matinhos", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialCobalt,
                        unfocusedBorderColor = EditorialBorder
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = institute,
                        onValueChange = { institute = it },
                        label = { Text("Instituto", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                    OutlinedTextField(
                        value = regId,
                        onValueChange = { regId = it },
                        label = { Text("Registro TSE / ID", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Categoria (POLÍTICA / INFRAESTRUTURA)", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                    OutlinedTextField(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Região", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sampleSize,
                        onValueChange = { sampleSize = it },
                        label = { Text("Amostra (N)", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                    OutlinedTextField(
                        value = marginOfError,
                        onValueChange = { marginOfError = it },
                        label = { Text("Margem %", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialCobalt,
                            unfocusedBorderColor = EditorialBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Opções e Percentuais (%)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EditorialNavyDeep
                )

                optionsList.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = option.name,
                            onValueChange = { newName ->
                                optionsList[index] = option.copy(name = newName)
                            },
                            label = { Text("Opção ${index + 1}", fontSize = 10.sp) },
                            modifier = Modifier.weight(2f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EditorialCobalt,
                                unfocusedBorderColor = EditorialBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        OutlinedTextField(
                            value = option.percentage,
                            onValueChange = { newPct ->
                                optionsList[index] = option.copy(percentage = newPct)
                            },
                            label = { Text("%", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EditorialCobalt,
                                unfocusedBorderColor = EditorialBorder
                            )
                        )
                        if (optionsList.size > 2) {
                            IconButton(onClick = { optionsList.removeAt(index) }) {
                                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.7f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = { optionsList.add(OptionInputState("Nova Opção", "10.0")) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, EditorialCobalt)
                ) {
                    Icon(Icons.Default.Add, null, tint = EditorialCobalt)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar Opção", fontSize = 11.sp, color = EditorialCobalt)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val jsonArray = JSONArray()
                        val colors = listOf("#001D36", "#0061A4", "#0A5844", "#8D5B18", "#7A8490")
                        optionsList.forEachIndexed { i, opt ->
                            jsonArray.put(JSONObject().apply {
                                put("name", opt.name)
                                put("percentage", opt.percentage.toDoubleOrNull() ?: 0.0)
                                put("colorHex", colors[i % colors.size])
                            })
                        }

                        val entity = ResearchSurveyEntity(
                            title = title,
                            institute = institute,
                            registrationId = regId,
                            category = category.uppercase(),
                            region = region,
                            sampleSize = sampleSize.toIntOrNull() ?: 600,
                            marginOfError = marginOfError.toDoubleOrNull() ?: 3.5,
                            datePeriod = datePeriod,
                            optionsJson = jsonArray.toString()
                        )
                        onSaveSurvey(entity)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EditorialCobalt),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar Pesquisa", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EditorialBorder)
            ) {
                Text("Cancelar", color = EditorialInk)
            }
        }
    )
}
