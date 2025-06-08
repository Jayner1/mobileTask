package com.example.mobiletaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.room.Room
import com.example.mobiletaskapp.data.AppDatabase
import com.example.mobiletaskapp.data.Category
import com.example.mobiletaskapp.data.Priority
import com.example.mobiletaskapp.data.Task
import com.example.mobiletaskapp.data.TaskWithDetails
import com.example.mobiletaskapp.ui.TaskScreen
import com.example.mobiletaskapp.ui.theme.MobileTaskAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// MainActivity.kt: Entry point of the app, sets up the database and UI, and handles task operations.
class MainActivity : ComponentActivity() {
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize Room database for storing tasks, categories, and priorities.
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task_database.db"
        ).build()

        setContent {
            MobileTaskAppTheme {
                val tasks = remember { mutableStateOf(listOf<TaskWithDetails>()) }
                val categories = remember { mutableStateOf(listOf<Category>()) }
                val priorities = remember { mutableStateOf(listOf<Priority>()) }

                // Load data from database when app starts.
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        initializeDatabase()
                        tasks.value = appDatabase.taskDao().getAllTasks()
                        categories.value = appDatabase.taskDao().getAllCategories()
                        priorities.value = appDatabase.taskDao().getAllPriorities()
                    }
                }

                TaskScreen(
                    tasks = tasks.value,
                    categories = categories.value,
                    priorities = priorities.value,
                    onAddTask = { desc, catId, priId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().insertTask(
                                Task(description = desc, category_id = catId, priority_id = priId)
                            )
                            tasks.value = appDatabase.taskDao().getAllTasks()
                        }
                    },
                    onCompleteTask = { taskId, isCompleted ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().toggleComplete(taskId, isCompleted)
                            tasks.value = appDatabase.taskDao().getAllTasks()
                        }
                    },
                    onUpdatePriority = { taskId, currentPriorityId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            // Cycle priority: High (1) -> Medium (2) -> Low (3) -> High (1).
                            val nextPriorityId = when (currentPriorityId) {
                                1 -> 2
                                2 -> 3
                                else -> 1
                            }
                            appDatabase.taskDao().updatePriority(taskId, nextPriorityId)
                            tasks.value = appDatabase.taskDao().getAllTasks()
                        }
                    },
                    onDeleteTask = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().deleteTask(taskId)
                            tasks.value = appDatabase.taskDao().getAllTasks()
                        }
                    }
                )
            }
        }
    }

    private suspend fun initializeDatabase() {
        with(appDatabase.taskDao()) {
            if (getAllTasks().isEmpty()) {
                insertPriority(Priority(priority_name = "High"))
                insertPriority(Priority(priority_name = "Medium"))
                insertPriority(Priority(priority_name = "Low"))
                // Initialize categories for task organization.
                insertCategory(Category(category_name = "Work"))
                insertCategory(Category(category_name = "General"))
                insertCategory(Category(category_name = "School"))
                insertCategory(Category(category_name = "Health"))
                insertCategory(Category(category_name = "Personal"))
                insertTask(Task(description = "Team meeting", category_id = 1, priority_id = 1))
                insertTask(Task(description = "Write essay", category_id = 3, priority_id = 2))
            }
        }
    }
}