package com.vedant.callnote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "call_log_table")
data class CallLogItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val phoneNumber: String,
    val contactName: String?,
    val duration: Long,
    val callDate: Date,
    val moodEmoji: String?, // Emoji representing mood (e.g., 😢, 😊, 😕)
    var notes: String? = null
)
