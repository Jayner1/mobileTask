package com.example.mobiletaskapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Insert
    suspend fun insertPriority(priority: Priority)

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("""
           SELECT t.task_id, t.description, t.is_completed, c.category_name, p.priority_name
           FROM Tasks t
           JOIN Categories c ON t.category_id = c.category_id
           JOIN Priority p ON t.priority_id = p.priority_id
       """)
    suspend fun getAllTasks(): List<TaskWithDetails>

    @Query("SELECT * FROM Priority")
    suspend fun getAllPriorities(): List<Priority>

    @Query("SELECT * FROM Categories")
    suspend fun getAllCategories(): List<Category>

    @Query("UPDATE Tasks SET is_completed = 1 WHERE task_id = :taskId")
    suspend fun markComplete(taskId: Int)

    @Query("UPDATE Tasks SET priority_id = :priorityId WHERE task_id = :taskId")
    suspend fun updatePriority(taskId: Int, priorityId: Int)

    @Query("DELETE FROM Tasks WHERE task_id = :taskId")
    suspend fun deleteTask(taskId: Int)
}

data class TaskWithDetails(
    val task_id: Int,
    val description: String,
    val is_completed: Boolean,
    val category_name: String,
    val priority_name: String
)