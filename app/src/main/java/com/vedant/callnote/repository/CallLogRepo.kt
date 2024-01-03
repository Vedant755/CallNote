package com.vedant.callnote.repository

import android.content.Context
import android.provider.CallLog
import com.vedant.callnote.model.CallLogItem
import com.vedant.callnote.room.CallLogDao
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject


class CallLogRepository @Inject constructor(@ApplicationContext private val context: Context,
    private val callLogDao: CallLogDao) {


    fun getCallLogs(): List<CallLogItem> {
        val callLogList = mutableListOf<CallLogItem>()

        // Define the columns you want to retrieve
        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DURATION
        )

        val sortOrder = "${CallLog.Calls.DATE} DESC"

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val id = cursor.getColumnIndex(CallLog.Calls._ID)
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val contactNameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)


            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(numberIndex)
                val contactName = cursor.getString(contactNameIndex)
                val callDate = Date(cursor.getLong(dateIndex))
                val duration = cursor.getLong(durationIndex)
                val moodEmoji = getMoodEmoji()
                val notes = getNotes()


                val callLogItem = CallLogItem(id,phoneNumber, contactName, duration, callDate, moodEmoji,notes)
                callLogList.add(callLogItem)
            }
        }

        return callLogList
    }


    private fun getMoodEmoji(): String {
        // Implement logic to determine mood based on call details
        // You can use your own criteria or user input to determine the mood emoji
        // For simplicity, returning a fixed emoji here
        return "😊"
    }
    private fun getNotes(): String? {
        return null
    }
    suspend fun insertCallLog(callLogItem: CallLogItem) {
        callLogDao.insert(callLogItem)
    }
    suspend fun insertOrUpdateCallLog(callLogItem: CallLogItem) {
        val existingItem = callLogDao.getNoteById(callLogItem.id)

        if (existingItem == null) {
            // Item is not present, insert it
            callLogDao.insert(callLogItem)
        } else {
            // Item is present, update mood emoji and notes
            callLogDao.updateMoodEmoji(callLogItem.id, callLogItem.moodEmoji ?: "")
            callLogDao.updateNotes(callLogItem.id, callLogItem.notes ?: "")
        }
    }

    // Add a suspend function to update mood emoji and notes
    suspend fun updateMoodEmojiAndNotes(callLogId: Int, newMoodEmoji: String, notes: String) {
        callLogDao.updateMoodEmoji(callLogId, newMoodEmoji)
        callLogDao.updateNotes(callLogId, notes)
    }
}
