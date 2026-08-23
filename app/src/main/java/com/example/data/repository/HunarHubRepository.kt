package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class HunarHubRepository(private val database: AppDatabase) {

    private val artisanDao = database.artisanDao()
    private val serviceDao = database.serviceDao()
    private val productDao = database.productDao()
    private val orderDao = database.orderRequestDao()
    private val categoryDao = database.categoryDao()
    private val reviewDao = database.reviewDao()

    suspend fun checkAndInitializeData() {
        val existing = artisanDao.getAllArtisans().first()
        if (existing.isEmpty()) {
            DatabaseInitializer.populateInitialData(database)
        }
    }

    // Artisans
    fun getAllArtisans(): Flow<List<ArtisanEntity>> = artisanDao.getAllArtisans()
    fun getVerifiedArtisans(): Flow<List<ArtisanEntity>> = artisanDao.getVerifiedArtisans()
    fun getPendingArtisans(): Flow<List<ArtisanEntity>> = artisanDao.getPendingArtisans()
    fun getArtisanById(id: Long): Flow<ArtisanEntity?> = artisanDao.getArtisanById(id)
    suspend fun getArtisanDirect(id: Long): ArtisanEntity? = artisanDao.getArtisanDirect(id)
    fun getArtisansByCategory(category: String): Flow<List<ArtisanEntity>> = artisanDao.getArtisansByCategory(category)
    suspend fun insertArtisan(artisan: ArtisanEntity): Long = artisanDao.insertArtisan(artisan)
    suspend fun updateArtisan(artisan: ArtisanEntity) = artisanDao.updateArtisan(artisan)
    suspend fun updateAvailability(id: Long, isAvailable: Boolean) = artisanDao.updateAvailability(id, isAvailable)
    suspend fun updateVerification(id: Long, isVerified: Boolean) = artisanDao.updateVerification(id, isVerified)

    // Services
    fun getServicesByArtisan(artisanId: Long): Flow<List<ServiceEntity>> = serviceDao.getServicesByArtisan(artisanId)
    suspend fun insertService(service: ServiceEntity): Long = serviceDao.insertService(service)
    suspend fun updateService(service: ServiceEntity) = serviceDao.updateService(service)
    suspend fun deleteService(id: Long) = serviceDao.deleteService(id)

    // Products
    fun getProductsByArtisan(artisanId: Long): Flow<List<ProductEntity>> = productDao.getProductsByArtisan(artisanId)
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    suspend fun insertProduct(product: ProductEntity): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)
    suspend fun deleteProduct(id: Long) = productDao.deleteProduct(id)

    // Orders & Requests
    fun getAllOrders(): Flow<List<OrderRequestEntity>> = orderDao.getAllOrders()
    fun getOrdersByArtisan(artisanId: Long): Flow<List<OrderRequestEntity>> = orderDao.getOrdersByArtisan(artisanId)
    fun getOrdersByCustomerPhone(phone: String): Flow<List<OrderRequestEntity>> = orderDao.getOrdersByCustomerPhone(phone)
    suspend fun placeOrder(order: OrderRequestEntity): Long = orderDao.insertOrder(order)
    suspend fun updateOrderStatus(id: Long, status: String, reason: String = "") = orderDao.updateOrderStatus(id, status, reason)

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    fun getActiveCategories(): Flow<List<CategoryEntity>> = categoryDao.getActiveCategories()
    suspend fun updateCategoryStatus(id: Long, isActive: Boolean) = categoryDao.updateCategoryStatus(id, isActive)
    suspend fun insertCategory(category: CategoryEntity): Long = categoryDao.insertCategory(category)

    // Reviews
    fun getReviewsForArtisan(artisanId: Long): Flow<List<ReviewEntity>> = reviewDao.getReviewsForArtisan(artisanId)
    suspend fun insertReview(review: ReviewEntity): Long = reviewDao.insertReview(review)
}
