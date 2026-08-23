package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ArtisanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtisanDao {
    @Query("SELECT * FROM artisans ORDER BY rating DESC")
    fun getAllArtisans(): Flow<List<ArtisanEntity>>

    @Query("SELECT * FROM artisans WHERE isVerified = 1 ORDER BY rating DESC")
    fun getVerifiedArtisans(): Flow<List<ArtisanEntity>>

    @Query("SELECT * FROM artisans WHERE isVerified = 0 ORDER BY id DESC")
    fun getPendingArtisans(): Flow<List<ArtisanEntity>>

    @Query("SELECT * FROM artisans WHERE id = :id")
    fun getArtisanById(id: Long): Flow<ArtisanEntity?>

    @Query("SELECT * FROM artisans WHERE id = :id")
    suspend fun getArtisanDirect(id: Long): ArtisanEntity?

    @Query("SELECT * FROM artisans WHERE category = :category AND isVerified = 1")
    fun getArtisansByCategory(category: String): Flow<List<ArtisanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtisans(artisans: List<ArtisanEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtisan(artisan: ArtisanEntity): Long

    @Update
    suspend fun updateArtisan(artisan: ArtisanEntity)

    @Query("UPDATE artisans SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateAvailability(id: Long, isAvailable: Boolean)

    @Query("UPDATE artisans SET isVerified = :isVerified WHERE id = :id")
    suspend fun updateVerification(id: Long, isVerified: Boolean)

    @Query("DELETE FROM artisans WHERE id = :id")
    suspend fun deleteArtisan(id: Long)
}
