package com.example.bookshelf.App.di.modules

import android.content.Context
import com.example.bookshelf.App.Model.Database.UserDao
import com.example.bookshelf.App.Model.Database.UserDatabase
import com.example.bookshelf.App.Model.Database.UserRepository
import com.example.bookshelf.App.Utilities.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

@Singleton
@Provides
    fun provideUserDao(@ApplicationContext context: Context) : UserDao{
       return UserDatabase.getDatabase(context).getUserDao()
    }

 @Singleton
 @Provides
    fun provideUserRepository(userDao: UserDao) : UserRepository{
       return UserRepository(userDao)
    }

@Singleton
@Provides
    fun providePreferenceManager(@ApplicationContext context: Context) : PreferenceManager{
        return PreferenceManager(context)
    }
}