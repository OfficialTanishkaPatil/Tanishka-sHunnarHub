package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artisanId: Long,
    val title: String,
    val description: String,
    val price: Double,
    val estimatedTime: String, // e.g. "1-2 days", "30 mins", "Custom timeline"
    val isAvailable: Boolean = true
)
