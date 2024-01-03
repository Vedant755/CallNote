package com.vedant.callnote.callLogScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vedant.callnote.model.CallLogItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(callLogViewModel: CallLogViewModel = viewModel()) {
    val callLogs = callLogViewModel.callLogs.value
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "CallNote") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                itemsIndexed(callLogs) { _, callLog ->
                    CallLogItemCard(
                        callLog = callLog,
                        onNoteAndEmojiAdded = { notes, selectedEmoji ->
                            // Handle the saving of notes and emoji here
                            // You may want to call a function in your ViewModel to handle this logic
                            //callLogViewModel.saveNotesAndEmoji(callLog.id, notes, selectedEmoji)
                            callLogViewModel.updateMoodEmojiAndNotes(callLog.id, selectedEmoji, notes)
                            callLogViewModel.insertOrUpdateCallLog(callLog.copy(moodEmoji = selectedEmoji, notes = notes))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CallLogItemCard(callLog: CallLogItem, onNoteAndEmojiAdded: (String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf(callLog.moodEmoji ?: "😊") }
    var notes by remember { mutableStateOf(callLog.notes ?: "") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(154.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    showDialog = true
                }
        ) {
            Text(
                text = "Phone Number: ${callLog.phoneNumber}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Contact Name: ${callLog.contactName}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Call Date: ${callLog.callDate}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Duration: ${callLog.duration} seconds")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Mood Emoji: ${callLog.moodEmoji}")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("Things to remember:") },
            text = {
                Column {
                    // Add a TextField for notes
                    TextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Notes!")}
                        
                    )

                    // Add a DropdownMenu for selecting mood emoji
                    var expanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        OutlinedButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Text("Feelings: $selectedEmoji")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            listOf("😢", "😊", "😕").forEach { emoji ->
                                DropdownMenuItem(
                                    text = { Text(text = emoji) },
                                    onClick = {
                                        selectedEmoji = emoji
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Save notes and emoji
                        onNoteAndEmojiAdded(notes, selectedEmoji)
                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}




