package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val localizedName: String, // e.g. "Kumhar / Potters", "Darzi / Tailors", "Mochi / Cobblers"
    val iconName: String,      // "pottery", "scissors", "shoe", "palette", "storefront"
    val description: String,
    val isActive: Boolean = true,
    val artisanCount: Int = 0
)
