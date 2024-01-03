// CallLogViewModel.kt
package com.vedant.callnote.callLogScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vedant.callnote.model.CallLogItem
import com.vedant.callnote.repository.CallLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogViewModel @Inject constructor(
    private val callLogRepository: CallLogRepository
) : ViewModel() {

    private val _callLogs = mutableStateOf<List<CallLogItem>>(emptyList())
    val callLogs: State<List<CallLogItem>> = _callLogs

    init {
        viewModelScope.launch {
            _callLogs.value = callLogRepository.getCallLogs()
        }
    }
    fun updateMoodEmojiAndNotes(callLogId: Int, newMoodEmoji: String, notes: String) {
        viewModelScope.launch {
            callLogRepository.updateMoodEmojiAndNotes(callLogId, newMoodEmoji, notes)
            _callLogs.value = callLogRepository.getCallLogs()
        }
    }
    fun insertOrUpdateCallLog(callLogItem: CallLogItem) {
        viewModelScope.launch {
            callLogRepository.insertOrUpdateCallLog(callLogItem)
            _callLogs.value = callLogRepository.getCallLogs()
        }
    }
}
