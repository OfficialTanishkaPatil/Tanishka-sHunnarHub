package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceEntity
import com.example.data.repository.HunarHubRepository
import com.example.ui.model.FilterState
import com.example.ui.model.RoleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HunarHubViewModel(private val repository: HunarHubRepository) : ViewModel() {

    // Current Role
    private val _currentRole = MutableStateFlow(RoleType.CUSTOMER)
    val currentRole: StateFlow<RoleType> = _currentRole.asStateFlow()

    // Selected Entrepreneur ID when in Entrepreneur mode (default to Ramu Prajapati, id = 1)
    private val _selectedEntrepreneurId = MutableStateFlow<Long>(1L)
    val selectedEntrepreneurId: StateFlow<Long> = _selectedEntrepreneurId.asStateFlow()

    // Filters for Customer
    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // Toast/Snackbar message notification
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Navigation sub-state
    private val _selectedArtisanForDetail = MutableStateFlow<ArtisanEntity?>(null)
    val selectedArtisanForDetail: StateFlow<ArtisanEntity?> = _selectedArtisanForDetail.asStateFlow()

    // Customer Navigation Tab (0: Explore/Artisans, 1: My Orders & Requests)
    private val _customerTab = MutableStateFlow(0)
    val customerTab: StateFlow<Int> = _customerTab.asStateFlow()

    // Raw flows from Database
    val allArtisans: StateFlow<List<ArtisanEntity>> = repository.getAllArtisans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val verifiedArtisans: StateFlow<List<ArtisanEntity>> = repository.getVerifiedArtisans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingArtisans: StateFlow<List<ArtisanEntity>> = repository.getPendingArtisans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderRequestEntity>> = repository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Artisans for Customer View
    val filteredArtisans: StateFlow<List<ArtisanEntity>> = combine(
        verifiedArtisans,
        _filterState
    ) { artisans, filter ->
        artisans.filter { artisan ->
            val matchCategory = filter.selectedCategory == "All" ||
                    artisan.category.equals(filter.selectedCategory, ignoreCase = true)
            val matchQuery = filter.searchQuery.isBlank() ||
                    artisan.name.contains(filter.searchQuery, ignoreCase = true) ||
                    artisan.craftTitle.contains(filter.searchQuery, ignoreCase = true) ||
                    artisan.location.contains(filter.searchQuery, ignoreCase = true) ||
                    artisan.specialization.contains(filter.searchQuery, ignoreCase = true)
            val matchPrice = artisan.startingPrice <= filter.maxPrice
            val matchRating = artisan.rating >= filter.minRating
            val matchAvailability = !filter.onlyAvailable || artisan.isAvailable
            val matchLocation = filter.selectedLocation == "All" || artisan.location.contains(filter.selectedLocation, ignoreCase = true)

            matchCategory && matchQuery && matchPrice && matchRating && matchAvailability && matchLocation
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndInitializeData()
        }
    }

    // Role Switching
    fun switchRole(newRole: RoleType) {
        _currentRole.value = newRole
        _selectedArtisanForDetail.value = null
        val roleMsg = when(newRole) {
            RoleType.CUSTOMER -> "Switched to Customer Mode: Explore & Book Crafts"
            RoleType.ENTREPRENEUR -> "Switched to Entrepreneur Dashboard"
            RoleType.ADMIN -> "Switched to Admin Portal: Approvals & Analytics"
        }
        showMessage(roleMsg)
    }

    fun selectEntrepreneur(artisanId: Long) {
        _selectedEntrepreneurId.value = artisanId
    }

    fun setCustomerTab(tab: Int) {
        _customerTab.value = tab
    }

    fun viewArtisanDetail(artisan: ArtisanEntity?) {
        _selectedArtisanForDetail.value = artisan
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // Filter Actions
    fun updateCategoryFilter(category: String) {
        _filterState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(searchQuery = query) }
    }

    fun updatePriceFilter(maxPrice: Double) {
        _filterState.update { it.copy(maxPrice = maxPrice) }
    }

    fun updateRatingFilter(minRating: Double) {
        _filterState.update { it.copy(minRating = minRating) }
    }

    fun toggleAvailabilityOnly() {
        _filterState.update { it.copy(onlyAvailable = !it.onlyAvailable) }
    }

    fun resetFilters() {
        _filterState.value = FilterState()
    }

    // Customer Actions: Book service or Buy product
    fun placeServiceRequest(
        artisan: ArtisanEntity,
        service: ServiceEntity,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        scheduledDate: String,
        notes: String
    ) {
        viewModelScope.launch {
            val order = OrderRequestEntity(
                orderType = "SERVICE",
                customerName = customerName.ifBlank { "Ananya Sharma" },
                customerPhone = customerPhone.ifBlank { "+91 98765 11223" },
                customerAddress = customerAddress.ifBlank { "Flat 402, Royal Palms" },
                artisanId = artisan.id,
                artisanName = artisan.name,
                itemTitle = service.title,
                quantity = 1,
                price = service.price,
                status = "PENDING",
                scheduledDate = scheduledDate,
                customerNotes = notes,
                timestamp = System.currentTimeMillis()
            )
            repository.placeOrder(order)
            showMessage("Service booking submitted to ${artisan.name}!")
        }
    }

    fun buyProduct(
        artisan: ArtisanEntity,
        product: ProductEntity,
        quantity: Int,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        notes: String
    ) {
        viewModelScope.launch {
            val order = OrderRequestEntity(
                orderType = "PRODUCT",
                customerName = customerName.ifBlank { "Ananya Sharma" },
                customerPhone = customerPhone.ifBlank { "+91 98765 11223" },
                customerAddress = customerAddress.ifBlank { "Flat 402, Royal Palms" },
                artisanId = artisan.id,
                artisanName = artisan.name,
                itemTitle = product.title,
                quantity = quantity,
                price = product.price * quantity,
                status = "PENDING",
                scheduledDate = "Standard 2-3 Day Artisan Shipping",
                customerNotes = notes,
                timestamp = System.currentTimeMillis()
            )
            repository.placeOrder(order)
            showMessage("Order placed for ${product.title} (x$quantity)!")
        }
    }

    // Micro-Entrepreneur Actions
    fun toggleAvailability(artisanId: Long, currentAvailability: Boolean) {
        viewModelScope.launch {
            val newStatus = !currentAvailability
            repository.updateAvailability(artisanId, newStatus)
            showMessage(if (newStatus) "Status updated: Available for new bookings" else "Status updated: Currently busy / away")
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String, reason: String = "") {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus, reason)
            val msg = when (newStatus) {
                "ACCEPTED" -> "Booking confirmed! Customer notified."
                "IN_PROGRESS" -> "Service marked as In Progress."
                "COMPLETED" -> "Order marked Completed! Earnings credited."
                "REJECTED" -> "Request declined."
                else -> "Order status updated to $newStatus"
            }
            showMessage(msg)
        }
    }

    fun addService(artisanId: Long, title: String, description: String, price: Double, estimatedTime: String) {
        viewModelScope.launch {
            val service = ServiceEntity(
                artisanId = artisanId,
                title = title,
                description = description,
                price = price,
                estimatedTime = estimatedTime
            )
            repository.insertService(service)
            showMessage("New service '$title' added to your storefront!")
        }
    }

    fun deleteService(serviceId: Long) {
        viewModelScope.launch {
            repository.deleteService(serviceId)
            showMessage("Service removed successfully.")
        }
    }

    fun addProduct(artisanId: Long, title: String, description: String, price: Double, stock: Int, tag: String, material: String) {
        viewModelScope.launch {
            val product = ProductEntity(
                artisanId = artisanId,
                title = title,
                description = description,
                price = price,
                stock = stock,
                tag = tag.ifBlank { "Handcrafted" },
                material = material.ifBlank { "Artisan Grade Material" }
            )
            repository.insertProduct(product)
            showMessage("Product '$title' listed for sale!")
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            showMessage("Product listing removed.")
        }
    }

    // Admin Actions
    fun approveArtisan(artisanId: Long) {
        viewModelScope.launch {
            repository.updateVerification(artisanId, true)
            showMessage("Artisan verified and published to public marketplace!")
        }
    }

    fun rejectArtisan(artisanId: Long) {
        viewModelScope.launch {
            repository.updateVerification(artisanId, false)
            showMessage("Verification request rejected.")
        }
    }

    fun toggleCategoryStatus(categoryId: Long, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.updateCategoryStatus(categoryId, !currentStatus)
            showMessage("Category visibility updated.")
        }
    }

    fun addCategory(name: String, localizedName: String, iconName: String, description: String) {
        viewModelScope.launch {
            val cat = CategoryEntity(
                name = name,
                localizedName = localizedName,
                iconName = iconName,
                description = description,
                isActive = true,
                artisanCount = 1
            )
            repository.insertCategory(cat)
            showMessage("New marketplace category '$name' created!")
        }
    }

    // Data helpers
    fun getServicesForArtisan(artisanId: Long) = repository.getServicesByArtisan(artisanId)
    fun getProductsForArtisan(artisanId: Long) = repository.getProductsByArtisan(artisanId)
    fun getReviewsForArtisan(artisanId: Long) = repository.getReviewsForArtisan(artisanId)
    fun getOrdersForArtisan(artisanId: Long) = repository.getOrdersByArtisan(artisanId)
}

class HunarHubViewModelFactory(private val repository: HunarHubRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HunarHubViewModel::class.java)) {
            return HunarHubViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
