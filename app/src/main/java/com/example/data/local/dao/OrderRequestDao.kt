package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.OrderRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderRequestDao {
    @Query("SELECT * FROM order_requests ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderRequestEntity>>

    @Query("SELECT * FROM order_requests WHERE artisanId = :artisanId ORDER BY timestamp DESC")
    fun getOrdersByArtisan(artisanId: Long): Flow<List<OrderRequestEntity>>

    @Query("SELECT * FROM order_requests WHERE customerPhone = :phone ORDER BY timestamp DESC")
    fun getOrdersByCustomerPhone(phone: String): Flow<List<OrderRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderRequestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderRequestEntity>)

    @Update
    suspend fun updateOrder(order: OrderRequestEntity)

    @Query("UPDATE order_requests SET status = :status, rejectionReason = :reason WHERE id = :id")
    suspend fun updateOrderStatus(id: Long, status: String, reason: String = "")

    @Query("DELETE FROM order_requests WHERE id = :id")
    suspend fun deleteOrder(id: Long)
}
