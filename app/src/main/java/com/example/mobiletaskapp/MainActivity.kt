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

class MainActivity : ComponentActivity() {
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task_database.db"
        ).build()

        setContent {
            MobileTaskAppTheme {
                val tasks = remember { mutableStateOf(listOf<TaskWithDetails>()) }
                val categories = remember { mutableStateOf(listOf<Category>()) }
                val priorities = remember { mutableStateOf(listOf<Priority>()) }

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
                            val nextPriorityId = when (currentPriorityId) {
                                1 -> 2 // High -> Medium
                                2 -> 3 // Medium -> Low
                                else -> 1 // Low -> High
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