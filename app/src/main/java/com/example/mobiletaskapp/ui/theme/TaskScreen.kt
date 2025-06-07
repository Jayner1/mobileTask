package com.example.mobiletaskapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobiletaskapp.data.TaskWithDetails

@Composable
fun TaskScreen(
    tasks: List<TaskWithDetails>,
    onAddTask: (String, Int, Int) -> Unit,
    onCompleteTask: (Int) -> Unit,
    onUpdatePriority: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var priorityId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = categoryId,
                onValueChange = { categoryId = it },
                label = { Text("Category ID") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            TextField(
                value = priorityId,
                onValueChange = { priorityId = it },
                label = { Text("Priority ID") },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                if (description.isNotBlank()) {
                    onAddTask(description, categoryId.toIntOrNull() ?: 1, priorityId.toIntOrNull() ?: 1)
                    description = ""
                    categoryId = ""
                    priorityId = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Add Task")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 16.dp)
        ) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("${task.task_id}: ${task.description} (${task.category_name}, ${task.priority_name}, Done: ${task.is_completed})")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { onCompleteTask(task.task_id) }) {
                                Text("Complete")
                            }
                            Button(onClick = { onUpdatePriority(task.task_id) }) {
                                Text("Update Priority")
                            }
                            Button(onClick = { onDeleteTask(task.task_id) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}