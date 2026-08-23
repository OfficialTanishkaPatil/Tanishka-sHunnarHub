package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_requests")
data class OrderRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderType: String,      // "SERVICE" or "PRODUCT"
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val artisanId: Long,
    val artisanName: String,
    val itemTitle: String,
    val quantity: Int = 1,
    val price: Double,
    val status: String = "PENDING", // "PENDING", "ACCEPTED", "IN_PROGRESS", "COMPLETED", "REJECTED"
    val scheduledDate: String,      // e.g. "Tomorrow, 3:00 PM" or "Immediate Delivery"
    val customerNotes: String = "",
    val rejectionReason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
