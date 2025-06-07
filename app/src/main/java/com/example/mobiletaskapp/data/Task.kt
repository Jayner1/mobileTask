package com.example.mobiletaskapp.data

class Task {
}package com.example.mobiletaskapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val task_id: Int = 0,
    val description: String,
    val is_completed: Boolean = false,
    val category_id: Int,
    val priority_id: Int
)