package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artisanId: Long,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val dateText: String = "2 days ago"
)
