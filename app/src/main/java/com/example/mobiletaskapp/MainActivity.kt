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
import com.example.mobiletaskapp.ui.theme.CompleteAppTheme
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
            CompleteAppTheme {
                var tasks by remember { mutableStateOf(listOf<TaskWithDetails>()) }
                var categories by remember { mutableStateOf(listOf<Category>()) }
                var priorities by remember { mutableStateOf(listOf<Priority>()) }

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        initializeDatabase()
                        tasks = appDatabase.taskDao().getAllTasks()
                        categories = appDatabase.taskDao().getAllCategories()
                        priorities = appDatabase.taskDao().getAllPriorities()
                    }
                }

                TaskScreen(
                    tasks = tasks,
                    categories = categories,
                    priorities = priorities,
                    onAddTask = { desc, catId, priId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().insertTask(
                                Task(description = desc, category_id = catId, priority_id = priId)
                            )
                            tasks = appDatabase.taskDao().getAllTasks()
                        }
                    },
                    onCompleteTask = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().markComplete(taskId)
                            tasks = appDatabase.taskDao().getAllTasks()
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
                            tasks = appDatabase.taskDao().getAllTasks()
                        }
                    },
                    onDeleteTask = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.taskDao().deleteTask(taskId)
                            tasks = appDatabase.taskDao().getAllTasks()
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
                insertTask(Task(description = "Team meeting", category_id = 1, priority_id = 1))
                insertTask(Task(description = "Write essay", category_id = 2, priority_id = 2))
            }
        }
    }
}