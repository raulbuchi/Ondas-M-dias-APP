package com.example.data.repository

import com.example.data.local.NewsDao
import com.example.data.model.NewsItemEntity
import com.example.data.remote.NewsFeedProvider
import com.example.data.remote.RssFeedFetcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NewsRepository(private val newsDao: NewsDao) {

    val allNews: Flow<List<NewsItemEntity>> = newsDao.getAllNews()
    val bookmarkedNews: Flow<List<NewsItemEntity>> = newsDao.getBookmarkedNews()

    suspend fun seedInitialDataIfNeeded() {
        val current = newsDao.getAllNews().first()
        if (current.isEmpty()) {
            val liveNews = RssFeedFetcher.fetchAllLiveFeeds()
            if (liveNews.isNotEmpty()) {
                newsDao.insertNews(liveNews)
            } else {
                newsDao.insertNews(NewsFeedProvider.getDefaultNews())
            }
        }
    }

    suspend fun refreshLiveNews(): Int {
        val liveNews = RssFeedFetcher.fetchAllLiveFeeds()
        if (liveNews.isNotEmpty()) {
            newsDao.clearUnbookmarkedNews()
            newsDao.insertNews(liveNews)
            return liveNews.size
        }
        return 0
    }

    suspend fun addNews(newsItem: NewsItemEntity) {
        newsDao.insertSingleNews(newsItem)
    }

    suspend fun toggleBookmark(news: NewsItemEntity) {
        val updated = news.copy(isBookmarked = !news.isBookmarked)
        newsDao.updateNews(updated)
    }

    suspend fun deleteNews(id: Long) {
        newsDao.deleteNewsById(id)
    }
}

