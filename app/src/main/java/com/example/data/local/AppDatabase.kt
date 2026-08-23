package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ArtisanDao
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.OrderRequestDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.ReviewDao
import com.example.data.local.dao.ServiceDao
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceEntity

@Database(
    entities = [
        ArtisanEntity::class,
        ServiceEntity::class,
        ProductEntity::class,
        OrderRequestEntity::class,
        CategoryEntity::class,
        ReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artisanDao(): ArtisanDao
    abstract fun serviceDao(): ServiceDao
    abstract fun productDao(): ProductDao
    abstract fun orderRequestDao(): OrderRequestDao
    abstract fun categoryDao(): CategoryDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hunarhub_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
