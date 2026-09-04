package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BlogRecipient
import com.example.data.model.NewsItemEntity
import com.example.data.model.PublicationEntity
import com.example.data.model.ResearchSurveyEntity
import com.example.data.remote.ContentParser
import com.example.data.remote.GeminiService
import com.example.data.remote.NewsFeedProvider
import com.example.data.remote.ParsedOutput
import com.example.data.repository.NewsRepository
import com.example.data.repository.PublicationRepository
import com.example.data.repository.ResearchRepository
import com.example.ui.tts.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val selectedMainTab: Int = 0, // 0 = Gerador Multimodal, 1 = Notícias Ao Vivo, 2 = Análise de Pesquisas
    val selectedRecipient: BlogRecipient = BlogRecipient.ALL_RECIPIENTS.first(),
    val viewingBlog: BlogRecipient? = null, // Non-null when viewing a specific blog's dedicated screen
    val titleInput: String = "",
    val ideaInput: String = "",
    val customApiKey: String = "",
    val isLoading: Boolean = false,
    val isGeneratingReport: Boolean = false,
    val isRefreshingNews: Boolean = false,
    val errorMessage: String? = null,
    val currentResult: ParsedOutput? = null,
    val activeTab: Int = 0,
    val showHistorySheet: Boolean = false,
    val showApiKeyDialog: Boolean = false,
    val showRecipientDetails: Boolean = false,
    val selectedSurveyId: Long? = null,
    val statusNotification: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val publicationRepo: PublicationRepository
    private val newsRepo: NewsRepository
    private val researchRepo: ResearchRepository
    private val geminiService: GeminiService = GeminiService()

    val ttsManager: TtsManager = TtsManager(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val historyList: StateFlow<List<PublicationEntity>>
    val newsList: StateFlow<List<NewsItemEntity>>
    val surveysList: StateFlow<List<ResearchSurveyEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        publicationRepo = PublicationRepository(db.publicationDao(), geminiService)
        newsRepo = NewsRepository(db.newsDao())
        researchRepo = ResearchRepository(db.researchSurveyDao())

        historyList = publicationRepo.allPublications.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        newsList = newsRepo.allNews.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        surveysList = researchRepo.allSurveys.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Seed initial mock/default data for news and surveys
        viewModelScope.launch {
            newsRepo.seedInitialDataIfNeeded()
            researchRepo.seedInitialDataIfNeeded()
        }

        // Pre-fill initial recipient's example
        val initial = BlogRecipient.ALL_RECIPIENTS.first()
        _uiState.value = _uiState.value.copy(
            selectedRecipient = initial,
            titleInput = initial.exampleTitle,
            ideaInput = initial.exampleIdea
        )
    }

    fun setMainTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedMainTab = tabIndex, viewingBlog = null)
    }

    fun openBlogDetail(blog: BlogRecipient?) {
        if (blog == null) {
            _uiState.value = _uiState.value.copy(viewingBlog = null)
        } else {
            _uiState.value = _uiState.value.copy(
                viewingBlog = blog,
                selectedRecipient = blog
            )
        }
    }

    fun closeBlogDetail() {
        _uiState.value = _uiState.value.copy(viewingBlog = null)
    }

    fun generateForBlog(blog: BlogRecipient) {
        _uiState.value = _uiState.value.copy(
            selectedRecipient = blog,
            titleInput = blog.exampleTitle,
            ideaInput = blog.exampleIdea,
            selectedMainTab = 0,
            viewingBlog = null,
            statusNotification = "Blog '${blog.name}' selecionado para novo artigo"
        )
    }

    fun selectRecipient(recipient: BlogRecipient) {
        _uiState.value = _uiState.value.copy(
            selectedRecipient = recipient,
            titleInput = recipient.exampleTitle,
            ideaInput = recipient.exampleIdea
        )
    }

    fun onTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(titleInput = newTitle)
    }

    fun onIdeaChanged(newIdea: String) {
        _uiState.value = _uiState.value.copy(ideaInput = newIdea)
    }

    fun setCustomApiKey(key: String) {
        _uiState.value = _uiState.value.copy(customApiKey = key, showApiKeyDialog = false)
    }

    fun setActiveTab(index: Int) {
        _uiState.value = _uiState.value.copy(activeTab = index)
    }

    fun toggleHistorySheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showHistorySheet = show)
    }

    fun toggleApiKeyDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showApiKeyDialog = show)
    }

    fun toggleRecipientDetails(show: Boolean) {
        _uiState.value = _uiState.value.copy(showRecipientDetails = show)
    }

    fun setSelectedSurveyId(id: Long) {
        _uiState.value = _uiState.value.copy(selectedSurveyId = id)
    }

    fun clearNotification() {
        _uiState.value = _uiState.value.copy(statusNotification = null)
    }

    // N8N Email Dispatch Method
    fun dispatchEmailToN8n(context: Context) {
        val currentState = _uiState.value
        val recipient = currentState.selectedRecipient
        val subject = currentState.titleInput.trim().ifBlank { recipient.exampleTitle }

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = android.net.Uri.parse("mailto:${recipient.email}")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, "") // Email strictly containing ONLY the subject line
        }

        try {
            val chooser = Intent.createChooser(intent, "Disparar E-mail N8N (${recipient.email})")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            // Log dispatch in history repository
            viewModelScope.launch {
                publicationRepo.savePublication(
                    recipientEmail = recipient.email,
                    recipientName = recipient.name,
                    title = subject,
                    rawIdea = "Disparo de e-mail efetuado para o servidor N8N (${recipient.email})",
                    parsedOutput = com.example.data.remote.ParsedOutput(
                        fullMarkdown = "# E-mail Disparado para N8N\n\n**Serviço de E-mail:** ${recipient.name} (`${recipient.email}`)\n\n**Assunto (Subject):** $subject\n\n*Este e-mail contendo apenas o assunto foi disparado para o servidor n8n, que executará o fluxo de automação.*",
                        articleText = "E-mail disparado para ${recipient.email} com o assunto: '$subject'",
                        audioScript = "Disparo de e-mail realizado para ${recipient.name} com o assunto: $subject.",
                        imagePrompt = "Ilustração editorial sobre: $subject"
                    )
                )
                _uiState.value = _uiState.value.copy(
                    statusNotification = "E-mail disparado para ${recipient.email} com assunto '$subject'!"
                )
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Não foi possível abrir o app de e-mail: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Generator logic
    fun generateContent() {
        val currentState = _uiState.value
        val title = currentState.titleInput.trim().ifBlank { currentState.selectedRecipient.exampleTitle }
        val idea = currentState.ideaInput.trim().ifBlank { currentState.selectedRecipient.exampleIdea }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            ttsManager.stop()

            val result = publicationRepo.generateContent(
                recipientEmail = currentState.selectedRecipient.email,
                title = title,
                idea = idea,
                customApiKey = currentState.customApiKey.ifBlank { null }
            )

            result.onSuccess { parsed ->
                publicationRepo.savePublication(
                    recipientEmail = currentState.selectedRecipient.email,
                    recipientName = currentState.selectedRecipient.name,
                    title = title,
                    rawIdea = idea,
                    parsedOutput = parsed
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentResult = parsed,
                    statusNotification = "Conteúdo Multimodal gerado com sucesso!"
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Erro ao gerar publicação"
                )
            }
        }
    }

    fun sendToGenerator(title: String, summaryOrIdea: String) {
        val ondasMediasRecipient = BlogRecipient.ALL_RECIPIENTS.first() // Ondas Médias
        _uiState.value = _uiState.value.copy(
            selectedMainTab = 0, // Switch to Multimodal Generator
            selectedRecipient = ondasMediasRecipient,
            titleInput = title,
            ideaInput = summaryOrIdea,
            statusNotification = "Dados enviados para o Gerador Multimodal!"
        )
    }

    // News Methods
    fun refreshNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshingNews = true)
            val count = newsRepo.refreshLiveNews()
            _uiState.value = _uiState.value.copy(
                isRefreshingNews = false,
                statusNotification = if (count > 0) "Feed de notícias atualizado ao vivo ($count notícias dos portais)!" else "Não foi possível carregar os feeds ao vivo no momento."
            )
        }
    }

    fun toggleNewsBookmark(news: NewsItemEntity) {
        viewModelScope.launch {
            newsRepo.toggleBookmark(news)
        }
    }

    // Research Survey Methods
    fun addSurvey(survey: ResearchSurveyEntity) {
        viewModelScope.launch {
            val newId = researchRepo.saveSurvey(survey)
            _uiState.value = _uiState.value.copy(
                selectedSurveyId = newId,
                statusNotification = "Pesquisa cadastrada com sucesso!"
            )
        }
    }

    fun generateSurveyReport(survey: ResearchSurveyEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGeneratingReport = true)
            val report = geminiService.generateResearchReport(
                title = survey.title,
                institute = survey.institute,
                regId = survey.registrationId,
                category = survey.category,
                region = survey.region,
                sampleSize = survey.sampleSize,
                marginOfError = survey.marginOfError,
                optionsJson = survey.optionsJson,
                customApiKey = _uiState.value.customApiKey.ifBlank { null }
            )

            val updatedSurvey = survey.copy(analyticalReport = report)
            researchRepo.updateSurvey(updatedSurvey)

            _uiState.value = _uiState.value.copy(
                isGeneratingReport = false,
                statusNotification = "Relatório Analítico gerado com sucesso!"
            )
        }
    }

    // History Methods
    fun loadFromHistory(item: PublicationEntity) {
        val recipient = BlogRecipient.findByEmail(item.recipientEmail)
        val parsed = ContentParser.parse(item.fullMarkdown)
        _uiState.value = _uiState.value.copy(
            selectedMainTab = 0,
            selectedRecipient = recipient,
            titleInput = item.title,
            ideaInput = item.rawIdea,
            currentResult = parsed,
            showHistorySheet = false,
            statusNotification = "Publicação carregada do histórico"
        )
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            publicationRepo.deletePublication(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            publicationRepo.clearHistory()
        }
    }

    // Audio & System Methods
    fun playTts() {
        val result = _uiState.value.currentResult ?: return
        val lang = if (_uiState.value.selectedRecipient.id == "rieden") "de" else "pt"
        ttsManager.speak(result.audioScript, lang)
    }

    fun stopTts() {
        ttsManager.stop()
    }

    fun copyToClipboard(context: Context, text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
    }

    fun shareContent(context: Context, title: String, text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartilhar via")
        context.startActivity(shareIntent)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
