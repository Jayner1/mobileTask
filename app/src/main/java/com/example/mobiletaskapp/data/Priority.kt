package com.example.mobiletaskapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Priority")
data class Priority(
    @PrimaryKey(autoGenerate = true)
    val priority_id: Int = 0,
    val priority_name: String
)