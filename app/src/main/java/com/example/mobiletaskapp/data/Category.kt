package com.example.mobiletaskapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val category_id: Int = 0,
    val category_name: String
)