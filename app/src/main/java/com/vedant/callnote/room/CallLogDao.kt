package com.vedant.callnote.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vedant.callnote.model.CallLogItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(callLogItem: CallLogItem): Long

    @Update
    suspend fun update(callLogItem: CallLogItem)




    @Query("SELECT * FROM call_log_table WHERE id = :id")
    suspend fun getNoteById(id: Int): CallLogItem?


    @Query("UPDATE call_log_table SET moodEmoji = :newMoodEmoji WHERE id = :callLogId")
    suspend fun updateMoodEmoji(callLogId: Int, newMoodEmoji: String)

    @Query("UPDATE call_log_table SET notes = :newNotes WHERE id = :callLogId")
    suspend fun updateNotes(callLogId: Int, newNotes: String)
}

