package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artisanId: Long,
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val tag: String = "Handmade",
    val material: String = "Pure Clay / Organic / Handcrafted"
)
