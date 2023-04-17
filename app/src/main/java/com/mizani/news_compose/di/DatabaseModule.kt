package com.mizani.news_compose.di

import android.content.Context
import androidx.room.Room
import com.mizani.news_compose.data.NewsRoom
import com.mizani.news_compose.data.dao.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideNewsDao(newsRoom: NewsRoom): NewsDao {
        return newsRoom.getNews()
    }

    @Provides
    @Singleton
    fun provideNewsRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NewsRoom::class.java,
        "mizani_news_db"
    ).build()


}
