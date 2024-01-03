package com.vedant.callnote

sealed interface LogEvent{
    object SaveNote: LogEvent
    data class SetEmoji(val emoji: String):LogEvent
    data class SetNote(val note: String):LogEvent
    object ShowDialog:LogEvent
    object HideDialog:LogEvent
    data class SortContacts(val sortType: SortType):LogEvent

}