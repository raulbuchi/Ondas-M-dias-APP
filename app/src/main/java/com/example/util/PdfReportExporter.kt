package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.ResearchSurveyEntity
import com.example.data.model.SurveyOption
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportExporter {

    fun exportAndShare(context: Context, survey: ResearchSurveyEntity) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 (595 x 842 pt)
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            isAntiAlias = true
        }

        // 1. Background Canvas (Editorial Paper)
        paint.color = Color.rgb(250, 250, 252)
        canvas.drawRect(0f, 0f, 595f, 842f, paint)

        // 2. Header Banner (Editorial Navy Deep)
        paint.color = Color.rgb(0, 29, 54) // #001D36
        canvas.drawRect(0f, 0f, 595f, 85f, paint)

        // Header Text
        paint.color = Color.WHITE
        paint.textSize = 17f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        canvas.drawText("GRUPO ONDAS MÉDIAS DE COMUNICAÇÃO", 25f, 38f, paint)

        paint.color = Color.rgb(180, 215, 255)
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Relatório Analítico de Pesquisa • Exportação Jornalística Ecossistema Buchi", 25f, 60f, paint)

        // 3. Survey Metadata Section
        var y = 115f
        paint.color = Color.rgb(0, 97, 164) // Cobalt
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val regIdText = if (survey.registrationId.isNotBlank()) "REGISTRO/ID: ${survey.registrationId}" else "DADOS INTERNOS"
        canvas.drawText("CATEGORIA: ${survey.category.uppercase()}  |  $regIdText", 25f, y, paint)

        y += 24f
        paint.color = Color.rgb(0, 29, 54)
        paint.textSize = 16f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        val titleLines = wrapText(survey.title, 55)
        for (tLine in titleLines) {
            canvas.drawText(tLine, 25f, y, paint)
            y += 20f
        }

        paint.color = Color.rgb(80, 85, 95)
        paint.textSize = 10.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Instituto: ${survey.institute}  •  Região: ${survey.region}  •  Período: ${survey.datePeriod}", 25f, y, paint)

        y += 18f
        paint.color = Color.rgb(0, 97, 164)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val sampleFormatted = "%,d".format(survey.sampleSize).replace(',', '.')
        canvas.drawText("Amostra (N): $sampleFormatted entrevistados   |   Margem de Erro Estipulada: ±${survey.marginOfError}%", 25f, y, paint)

        // Divider
        y += 18f
        paint.color = Color.rgb(210, 215, 225)
        paint.strokeWidth = 1f
        canvas.drawLine(25f, y, 570f, y, paint)

        // 4. Mapped Percentages Section
        y += 24f
        paint.color = Color.rgb(0, 29, 54)
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        canvas.drawText("Distribuição Percentual de Resultados", 25f, y, paint)

        // Parse options
        val optionsList = mutableListOf<SurveyOption>()
        try {
            val jsonArray = JSONArray(survey.optionsJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                optionsList.add(
                    SurveyOption(
                        name = obj.getString("name"),
                        percentage = obj.getDouble("percentage"),
                        colorHex = if (obj.has("colorHex") && !obj.isNull("colorHex")) obj.getString("colorHex") else null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val palette = listOf(
            Color.rgb(0, 29, 54),
            Color.rgb(0, 97, 164),
            Color.rgb(25, 135, 84),
            Color.rgb(214, 51, 132),
            Color.rgb(253, 126, 20),
            Color.rgb(108, 117, 125)
        )

        optionsList.forEachIndexed { idx, option ->
            y += 22f
            val itemColor = palette[idx % palette.size]

            // Color Indicator Square
            paint.color = itemColor
            paint.style = Paint.Style.FILL
            canvas.drawRect(25f, y - 9f, 35f, y + 1f, paint)

            // Name
            paint.color = Color.rgb(30, 35, 45)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(option.name, 42f, y, paint)

            // Percentage
            paint.color = itemColor
            paint.textSize = 11.5f
            val pctStr = "%.1f%%".format(option.percentage)
            val minMaxStr = " (min %.1f%% - max %.1f%%)".format(
                (option.percentage - survey.marginOfError).coerceAtLeast(0.0),
                (option.percentage + survey.marginOfError).coerceAtMost(100.0)
            )
            canvas.drawText("$pctStr $minMaxStr", 350f, y, paint)

            // Bar background
            y += 5f
            paint.color = Color.rgb(230, 235, 242)
            canvas.drawRect(42f, y, 570f, y + 7f, paint)

            // Bar fill
            paint.color = itemColor
            val fillW = (option.percentage / 100.0).toFloat() * (570f - 42f)
            canvas.drawRect(42f, y, 42f + fillW, y + 7f, paint)

            y += 8f
        }

        // 5. Analytical Report Text (if available)
        if (!survey.analyticalReport.isNullOrBlank()) {
            y += 24f
            paint.color = Color.rgb(0, 29, 54)
            paint.textSize = 13f
            paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("Análise Editorial & Síntese Jornalística", 25f, y, paint)

            y += 14f
            paint.color = Color.rgb(45, 50, 60)
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            val lines = wrapText(survey.analyticalReport, 100)
            for (line in lines) {
                if (y > 780f) break
                canvas.drawText(line, 25f, y, paint)
                y += 13f
            }
        }

        // 6. Footer
        paint.color = Color.rgb(130, 135, 145)
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("pt", "BR")).format(Date())
        canvas.drawText("Relatório gerado em $timeNow • Grupo Ondas Médias & Ecossistema Digital Buchi", 25f, 818f, paint)

        pdfDocument.finishPage(page)

        // Write file and share
        try {
            val fileName = "Relatorio_Pesquisa_${survey.id}.pdf"
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            // Open share intent
            val authority = "${context.packageName}.fileprovider"
            val uri = FileProvider.getUriForFile(context, authority, file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Relatório PDF: ${survey.title}")
                putExtra(Intent.EXTRA_TEXT, "Segue em anexo o relatório em PDF da pesquisa '${survey.title}' emitido pelo Grupo Ondas Médias.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Baixar / Compartilhar Relatório PDF")
            context.startActivity(chooser)

            Toast.makeText(context, "Relatório PDF gerado com sucesso!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            Toast.makeText(context, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun wrapText(text: String, maxChars: Int): List<String> {
        val result = mutableListOf<String>()
        val paragraphs = text.split("\n")
        for (p in paragraphs) {
            if (p.isBlank()) {
                result.add("")
                continue
            }
            val words = p.split(" ")
            var sb = StringBuilder()
            for (w in words) {
                if (sb.length + w.length + 1 > maxChars) {
                    result.add(sb.toString())
                    sb = StringBuilder(w)
                } else {
                    if (sb.isNotEmpty()) sb.append(" ")
                    sb.append(w)
                }
            }
            if (sb.isNotEmpty()) {
                result.add(sb.toString())
            }
        }
        return result
    }
}
