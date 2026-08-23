package com.example.ui.model

enum class RoleType(val label: String, val badge: String) {
    CUSTOMER("Customer", "👤 Shopper & Client"),
    ENTREPRENEUR("Micro-Entrepreneur", "🛠️ Local Artisan / Vendor"),
    ADMIN("Platform Admin", "🛡️ Verification & Analytics")
}

data class FilterState(
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val maxPrice: Double = 1500.0,
    val minRating: Double = 0.0,
    val onlyAvailable: Boolean = false,
    val selectedLocation: String = "All"
)
