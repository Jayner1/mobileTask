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
import com.example.mobiletaskapp.ui.TaskScreen
import com.example.mobiletaskapp.ui.theme.MobileTaskAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task_manager.db"
        ).build()

        setContent {
            MobileTaskAppTheme {
                var tasks by remember { mutableStateOf(listOf<com.example.mobiletaskapp.data.TaskWithDetails>()) }

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        initializeDatabase()
                        tasks = database.taskDao().getAllTasks()
                    }
                }

                TaskScreen(
                    tasks = tasks,
                    onAddTask = { desc, catId, priId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.taskDao().insertTask(
                                Task(
                                    description = desc,
                                    category_id = catId,
                                    priority_id = priId
                                )
                            )
                            tasks = database.taskDao().getAllTasks()
                        }
                    },
                    onCompleteTask = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.taskDao().markComplete(taskId)
                            tasks = database.taskDao().getAllTasks()
                        }
                    },
                    onUpdatePriority = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.taskDao().updatePriority(taskId, 2) // Medium
                            tasks = database.taskDao().getAllTasks()
                        }
                    },
                    onDeleteTask = { taskId ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.taskDao().deleteTask(taskId)
                            tasks = database.taskDao().getAllTasks()
                        }
                    }
                )
            }
        }
    }

    private suspend fun initializeDatabase() {
        with(database.taskDao()) {
            if (getAllTasks().isEmpty()) {
                insertPriority(Priority(priority_name = "High"))
                insertPriority(Priority(priority_name = "Medium"))
                insertPriority(Priority(priority_name = "Low"))
                insertCategory(Category(category_name = "Work"))
                insertCategory(Category(category_name = "School"))
                insertCategory(Category(category_name = "Personal"))
                insertCategory(Category(category_name = "Health"))
                insertTask(Task(description = "Team meeting", category_id = 1, priority_id = 1))
                insertTask(Task(description = "Write report", category_id = 2, priority_id = 2))
            }
        }
    }
}