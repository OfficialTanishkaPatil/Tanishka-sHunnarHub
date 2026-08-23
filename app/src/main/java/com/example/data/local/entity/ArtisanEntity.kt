package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artisans")
data class ArtisanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val craftTitle: String, // e.g. "Master Clay Potter & Terracotta Artisan"
    val category: String,   // "Potters", "Tailors", "Cobblers", "Artisans", "Vendors"
    val location: String,   // e.g. "Khurja, Uttar Pradesh"
    val distanceKm: Double = 1.2,
    val bio: String,
    val phone: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val startingPrice: Double,
    val isVerified: Boolean = true,
    val isAvailable: Boolean = true,
    val verificationNotes: String = "Aadhaar & Craft Guild Verified",
    val avatarColorHex: Long = 0xFFC85A32,
    val specialization: String = "Handmade clay cookware & decorative diyas"
)
