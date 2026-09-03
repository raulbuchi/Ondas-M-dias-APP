package com.example.data.remote

import android.util.Xml
import com.example.data.model.NewsItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object RssFeedFetcher {

    data class FeedConfig(
        val url: String,
        val sourceName: String,
        val defaultCategory: String,
        val location: String
    )

    val LIVE_FEEDS = listOf(
        FeedConfig(
            url = "https://feeds.folha.uol.com.br/emcimadahora/rss091.xml",
            sourceName = "Folha de S.Paulo",
            defaultCategory = "EM CIMA DA HORA",
            location = "Folha / Brasil"
        ),
        FeedConfig(
            url = "https://feeds.folha.uol.com.br/poder/rss091.xml",
            sourceName = "Folha de S.Paulo",
            defaultCategory = "POLÍTICA",
            location = "Folha / Poder"
        ),
        FeedConfig(
            url = "https://feeds.folha.uol.com.br/mercado/rss091.xml",
            sourceName = "Folha de S.Paulo",
            defaultCategory = "MERCADO",
            location = "Folha / Mercado"
        ),
        FeedConfig(
            url = "https://www.cnnbrasil.com.br/feed/",
            sourceName = "CNN Brasil",
            defaultCategory = "POLÍTICA & NOTÍCIAS",
            location = "CNN Brasil"
        ),
        FeedConfig(
            url = "https://oantagonista.com.br/feed/",
            sourceName = "O Antagonista",
            defaultCategory = "POLÍTICA",
            location = "O Antagonista"
        ),
        FeedConfig(
            url = "https://www.brasil247.com/feed",
            sourceName = "Brasil 247",
            defaultCategory = "POLÍTICA & SOCIEDADE",
            location = "Brasil 247"
        )
    )

    suspend fun fetchAllLiveFeeds(): List<NewsItemEntity> = withContext(Dispatchers.IO) {
        val jobs = LIVE_FEEDS.map { config ->
            async {
                fetchSingleFeed(config)
            }
        }
        val results = jobs.awaitAll().flatten()
        if (results.isEmpty()) {
            NewsFeedProvider.getDefaultNews()
        } else {
            results.distinctBy { it.title.trim().lowercase() }.sortedByDescending { it.timestamp }
        }
    }

    private fun fetchSingleFeed(config: FeedConfig): List<NewsItemEntity> {
        val newsList = mutableListOf<NewsItemEntity>()
        var connection: HttpURLConnection? = null
        try {
            val url = URL(config.url)
            connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 10000
                readTimeout = 10000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                setRequestProperty("Accept", "application/rss+xml, application/xml, text/xml, */*")
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, "UTF-8")

                var eventType = parser.eventType
                var insideItem = false

                var title = ""
                var link = ""
                var description = ""
                var pubDateStr = ""
                var category = config.defaultCategory

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val tagName = parser.name ?: ""
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            if (tagName.equals("item", ignoreCase = true) || tagName.equals("entry", ignoreCase = true)) {
                                insideItem = true
                                title = ""
                                link = ""
                                description = ""
                                pubDateStr = ""
                                category = config.defaultCategory
                            } else if (insideItem) {
                                when {
                                    tagName.equals("title", ignoreCase = true) -> title = safeNextText(parser)
                                    tagName.equals("link", ignoreCase = true) -> {
                                        val attrHref = parser.getAttributeValue(null, "href")
                                        if (!attrHref.isNullOrBlank()) {
                                            link = attrHref
                                        } else {
                                            val t = safeNextText(parser)
                                            if (t.isNotBlank()) link = t
                                        }
                                    }
                                    tagName.equals("description", ignoreCase = true) ||
                                            tagName.equals("summary", ignoreCase = true) ||
                                            tagName.equals("content", ignoreCase = true) ||
                                            tagName.equals("content:encoded", ignoreCase = true) -> {
                                        val rawText = safeNextText(parser)
                                        if (description.isBlank() || rawText.length > description.length) {
                                            description = rawText
                                        }
                                    }
                                    tagName.equals("pubDate", ignoreCase = true) ||
                                            tagName.equals("published", ignoreCase = true) ||
                                            tagName.equals("dc:date", ignoreCase = true) -> {
                                        pubDateStr = safeNextText(parser)
                                    }
                                    tagName.equals("category", ignoreCase = true) -> {
                                        val catText = safeNextText(parser)
                                        if (catText.isNotBlank()) {
                                            category = catText.uppercase()
                                        }
                                    }
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (tagName.equals("item", ignoreCase = true) || tagName.equals("entry", ignoreCase = true)) {
                                insideItem = false
                                val cleanTitle = cleanHtml(title)
                                val cleanSummary = cleanHtml(description)
                                if (cleanTitle.isNotBlank()) {
                                    val ts = parsePubDate(pubDateStr)
                                    val summaryText = if (cleanSummary.isNotBlank()) {
                                        if (cleanSummary.length > 280) cleanSummary.take(280) + "..." else cleanSummary
                                    } else {
                                        cleanTitle
                                    }
                                    val fullContentText = if (cleanSummary.isNotBlank()) cleanSummary else cleanTitle

                                    newsList.add(
                                        NewsItemEntity(
                                            title = cleanTitle,
                                            category = category,
                                            location = config.location,
                                            summary = summaryText,
                                            fullContent = fullContentText,
                                            source = config.sourceName,
                                            timestamp = ts
                                        )
                                    )
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        return newsList
    }

    private fun safeNextText(parser: XmlPullParser): String {
        return try {
            parser.nextText() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun cleanHtml(input: String): String {
        return input
            .replace(Regex("<!\\[CDATA\\[([\\s\\S]*?)\\]\\]>"), "$1")
            .replace(Regex("<style[\\s\\S]*?</style>"), "")
            .replace(Regex("<script[\\s\\S]*?</script>"), "")
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&#039;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun parsePubDate(dateStr: String): Long {
        if (dateStr.isBlank()) return System.currentTimeMillis()
        val formats = listOf(
            "EEE, dd MMM yyyy HH:mm:ss z",
            "EEE, dd MMM yyyy HH:mm:ss Z",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ssXXX"
        )
        for (fmt in formats) {
            try {
                val sdf = SimpleDateFormat(fmt, Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val date = sdf.parse(dateStr)
                if (date != null) return date.time
            } catch (e: Exception) {
                // Ignore and try next
            }
        }
        return System.currentTimeMillis()
    }
}
