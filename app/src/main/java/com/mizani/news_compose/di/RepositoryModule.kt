package com.mizani.news_compose.di

import com.mizani.news_compose.data.repository.NewsRepositoryImpl
import com.mizani.news_compose.data.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

}