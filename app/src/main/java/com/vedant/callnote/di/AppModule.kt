// AppModule.kt
package com.vedant.callnote.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.vedant.callnote.callLogScreen.CallLogViewModel
import com.vedant.callnote.repository.CallLogRepository
import com.vedant.callnote.room.AppDatabase
import com.vedant.callnote.room.CallLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCallLogViewModel(
        callLogRepository: CallLogRepository
    ): CallLogViewModel {
        return CallLogViewModel(callLogRepository)
    }

    @Provides
    @Singleton
    fun provideCallLogRepository(@ApplicationContext context: Context, db: AppDatabase): CallLogRepository {
        return CallLogRepository(context, db.callLogDao())
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "call_log_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCallLogDao(appDatabase: AppDatabase): CallLogDao {
        return appDatabase.callLogDao()
    }
}

