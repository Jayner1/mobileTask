package com.example.mobiletaskapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobiletaskapp.data.Category
import com.example.mobiletaskapp.data.Priority
import com.example.mobiletaskapp.data.TaskWithDetails
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

// TaskScreen.kt: Defines the UI for adding, viewing, and managing tasks.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    tasks: List<TaskWithDetails>,
    categories: List<Category>,
    priorities: List<Priority>,
    onAddTask: (String, Int, Int) -> Unit,
    onCompleteTask: (Int, Boolean) -> Unit,
    onUpdatePriority: (Int, Int) -> Unit,
    onDeleteTask: (Int) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.category_id ?: 0) }
    var selectedPriorityId by remember { mutableStateOf(priorities.firstOrNull()?.priority_id ?: 0) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

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
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for selecting task category (e.g., Work, School).
        Box {
            OutlinedTextField(
                value = categories.find { it.category_id == selectedCategoryId }?.category_name ?: "",
                onValueChange = {},
                label = { Text("Category") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) }
            )
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.category_name) },
                        onClick = {
                            selectedCategoryId = category.category_id
                            categoryExpanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier
                .matchParentSize()
                .clickable { categoryExpanded = true })
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for selecting task priority (e.g., High, Medium).
        Box {
            OutlinedTextField(
                value = priorities.find { it.priority_id == selectedPriorityId }?.priority_name ?: "",
                onValueChange = {},
                label = { Text("Priority") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) }
            )
            DropdownMenu(
                expanded = priorityExpanded,
                onDismissRequest = { priorityExpanded = false }
            ) {
                priorities.forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(priority.priority_name) },
                        onClick = {
                            selectedPriorityId = priority.priority_id
                            priorityExpanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier
                .matchParentSize()
                .clickable { priorityExpanded = true })
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (description.isNotBlank() && selectedCategoryId != 0 && selectedPriorityId != 0) {
                    onAddTask(description, selectedCategoryId, selectedPriorityId)
                    description = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.is_completed,
                        onCheckedChange = { onCompleteTask(task.task_id, it) }
                    )
                    Column {
                        Text(text = task.description)
                        Text(text = "Category: ${task.category_name}")
                    }
                    Text(
                        text = task.priority_name,
                        modifier = Modifier
                            .clickable {
                                val currentPriorityId = priorities.find { it.priority_name == task.priority_name }?.priority_id ?: 1
                                onUpdatePriority(task.task_id, currentPriorityId)
                            }
                            .padding(8.dp)
                    )
                    IconButton(onClick = { onDeleteTask(task.task_id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
}