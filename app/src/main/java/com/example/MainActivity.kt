package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.ApiKeyDialog
import com.example.ui.components.BlogDrawerContent
import com.example.ui.components.BrandHeader
import com.example.ui.components.HistorySheet
import com.example.ui.components.InputSection
import com.example.ui.components.MarkdownOutputViewer
import com.example.ui.components.RecipientSelector
import com.example.ui.screens.BlogDetailScreen
import com.example.ui.screens.NewsFeedScreen
import com.example.ui.screens.ResearchAnalyticsScreen
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialCobalt
import com.example.ui.theme.EditorialCobaltLight
import com.example.ui.theme.EditorialInkMuted
import com.example.ui.theme.EditorialNavyDeep
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val newsList by viewModel.newsList.collectAsStateWithLifecycle()
    val surveysList by viewModel.surveysList.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.statusNotification) {
        uiState.statusNotification?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotification()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { err ->
            snackbarHostState.showSnackbar("Atenção: $err")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            BlogDrawerContent(
                selectedBlog = uiState.viewingBlog,
                onSelectBlog = { blog ->
                    viewModel.openBlogDetail(blog)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFFF4F8FD),
                    contentColor = EditorialNavyDeep,
                    tonalElevation = 4.dp,
                    modifier = Modifier.testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = uiState.viewingBlog == null && uiState.selectedMainTab == 0,
                        onClick = { viewModel.setMainTab(0) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Gerador Multimodal"
                            )
                        },
                        label = { Text("Gerador", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = EditorialNavyDeep,
                            indicatorColor = EditorialCobalt,
                            unselectedIconColor = EditorialInkMuted,
                            unselectedTextColor = EditorialInkMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_generator")
                    )

                    NavigationBarItem(
                        selected = uiState.viewingBlog == null && uiState.selectedMainTab == 1,
                        onClick = { viewModel.setMainTab(1) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Newspaper,
                                contentDescription = "Notícias Ao Vivo"
                            )
                        },
                        label = { Text("Notícias Ao Vivo", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = EditorialNavyDeep,
                            indicatorColor = EditorialCobalt,
                            unselectedIconColor = EditorialInkMuted,
                            unselectedTextColor = EditorialInkMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_news")
                    )

                    NavigationBarItem(
                        selected = uiState.viewingBlog == null && uiState.selectedMainTab == 2,
                        onClick = { viewModel.setMainTab(2) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Pesquisas"
                            )
                        },
                        label = { Text("Pesquisas", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = EditorialNavyDeep,
                            indicatorColor = EditorialCobalt,
                            unselectedIconColor = EditorialInkMuted,
                            unselectedTextColor = EditorialInkMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_surveys")
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("main_screen_container")
            ) {
                if (uiState.viewingBlog != null) {
                    // Dedicated view for the selected blog
                    BlogDetailScreen(
                        blog = uiState.viewingBlog!!,
                        publications = historyList,
                        onOpenMenu = { scope.launch { drawerState.open() } },
                        onBackToHome = { viewModel.closeBlogDetail() },
                        onGenerateForBlog = { viewModel.generateForBlog(it) },
                        onDeletePublication = { viewModel.deleteHistoryItem(it) },
                        onPlayTts = { text, lang -> viewModel.ttsManager.speak(text, lang) },
                        onCopyText = { ctx, text, label -> viewModel.copyToClipboard(ctx, text, label) },
                        onShareText = { ctx, title, text -> viewModel.shareContent(ctx, title, text) }
                    )
                } else {
                    // Brand Header with circular logo emblem & categories
                    BrandHeader(
                        historyCount = historyList.size,
                        onOpenHistory = { viewModel.toggleHistorySheet(true) },
                        onOpenApiKey = { viewModel.toggleApiKeyDialog(true) },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Main Content Switcher
                    when (uiState.selectedMainTab) {
                        0 -> {
                            // TAB 0: Multimodal Content Generator
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    // 1. Recipient Selector
                                    RecipientSelector(
                                        selectedRecipient = uiState.selectedRecipient,
                                        onSelectRecipient = { viewModel.selectRecipient(it) },
                                        showDetails = uiState.showRecipientDetails,
                                        onToggleDetails = { viewModel.toggleRecipientDetails(!uiState.showRecipientDetails) }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // 2. Title & Message / Idea Input Section
                                    InputSection(
                                        selectedRecipient = uiState.selectedRecipient,
                                        title = uiState.titleInput,
                                        onTitleChange = { viewModel.onTitleChanged(it) },
                                        idea = uiState.ideaInput,
                                        onIdeaChange = { viewModel.onIdeaChanged(it) },
                                        isLoading = uiState.isLoading,
                                        onGenerate = { viewModel.generateContent() },
                                        onFillExample = {
                                            viewModel.onTitleChanged(uiState.selectedRecipient.exampleTitle)
                                            viewModel.onIdeaChanged(uiState.selectedRecipient.exampleIdea)
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // 3. Generated Multimodal Output
                                    AnimatedVisibility(visible = uiState.currentResult != null) {
                                        uiState.currentResult?.let { result ->
                                            MarkdownOutputViewer(
                                                output = result,
                                                activeTab = uiState.activeTab,
                                                onTabSelected = { viewModel.setActiveTab(it) },
                                                isSpeaking = isSpeaking,
                                                onPlayTts = { viewModel.playTts() },
                                                onStopTts = { viewModel.stopTts() },
                                                onCopy = { text, label -> viewModel.copyToClipboard(context, text, label) },
                                                onShare = { title, text -> viewModel.shareContent(context, title, text) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }

                        1 -> {
                            // TAB 1: Real-Time News Feed
                            NewsFeedScreen(
                                newsList = newsList,
                                isRefreshing = uiState.isRefreshingNews,
                                onRefresh = { viewModel.refreshNews() },
                                onToggleBookmark = { viewModel.toggleNewsBookmark(it) },
                                onSendToGenerator = { title, summary ->
                                    viewModel.sendToGenerator(title, summary)
                                }
                            )
                        }

                        2 -> {
                            // TAB 2: Research Data Analytics
                            ResearchAnalyticsScreen(
                                surveys = surveysList,
                                selectedSurveyId = uiState.selectedSurveyId,
                                onSelectSurvey = { viewModel.setSelectedSurveyId(it) },
                                onAddSurvey = { viewModel.addSurvey(it) },
                                onGenerateReport = { viewModel.generateSurveyReport(it) },
                                isGeneratingReport = uiState.isGeneratingReport,
                                onSendToGenerator = { title, idea ->
                                    viewModel.sendToGenerator(title, idea)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // History Sheet
    if (uiState.showHistorySheet) {
        HistorySheet(
            history = historyList,
            onDismiss = { viewModel.toggleHistorySheet(false) },
            onLoadPublication = { viewModel.loadFromHistory(it) },
            onDeleteItem = { viewModel.deleteHistoryItem(it) },
            onClearAll = { viewModel.clearAllHistory() }
        )
    }

    // API Key Dialog
    if (uiState.showApiKeyDialog) {
        ApiKeyDialog(
            currentCustomKey = uiState.customApiKey,
            onSaveKey = { viewModel.setCustomApiKey(it) },
            onDismiss = { viewModel.toggleApiKeyDialog(false) }
        )
    }
}
