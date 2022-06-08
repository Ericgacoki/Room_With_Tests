package com.ericg.groom.di

import android.app.Application
import androidx.room.Room
import com.ericg.groom.data.UserDatabase
import com.ericg.groom.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesDatabase(application: Application): UserDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            UserDatabase::class.java,
            "user_database",
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesRepository(database: UserDatabase): UserRepository {
        return UserRepository(db = database)
    }
}