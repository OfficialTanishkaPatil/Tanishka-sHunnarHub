package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ServiceEntity
import com.example.data.repository.HunarHubRepository
import com.example.ui.components.RoleSwitcherBanner
import com.example.ui.dialogs.BookServiceDialog
import com.example.ui.dialogs.BuyProductDialog
import com.example.ui.model.RoleType
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ArtisanDetailScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.EntrepreneurDashboardScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HunarHubViewModel
import com.example.ui.viewmodel.HunarHubViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = HunarHubRepository(database)
        val factory = HunarHubViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                HunarHubApp(factory = factory)
            }
        }
    }
}

@Composable
fun HunarHubApp(
    factory: HunarHubViewModelFactory,
    viewModel: HunarHubViewModel = viewModel(factory = factory)
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val filteredArtisans by viewModel.filteredArtisans.collectAsStateWithLifecycle()
    val verifiedArtisans by viewModel.verifiedArtisans.collectAsStateWithLifecycle()
    val pendingArtisans by viewModel.pendingArtisans.collectAsStateWithLifecycle()
    val allArtisans by viewModel.allArtisans.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val selectedArtisanDetail by viewModel.selectedArtisanForDetail.collectAsStateWithLifecycle()
    val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
    val selectedEntrepreneurId by viewModel.selectedEntrepreneurId.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    // Dialog state
    var bookingArtisan by remember { mutableStateOf<ArtisanEntity?>(null) }
    var bookingPreSelectedService by remember { mutableStateOf<ServiceEntity?>(null) }
    var purchasingProduct by remember { mutableStateOf<Pair<ArtisanEntity, ProductEntity>?>(null) }

    // Service & product flows for currently selected entrepreneur or detail view
    val activeArtisanId = if (currentRole == RoleType.ENTREPRENEUR) {
        selectedEntrepreneurId
    } else {
        selectedArtisanDetail?.id ?: 1L
    }

    val activeServices by viewModel.getServicesForArtisan(activeArtisanId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val activeProducts by viewModel.getProductsForArtisan(activeArtisanId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val activeReviews by viewModel.getReviewsForArtisan(activeArtisanId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val entrepreneurOrders by viewModel.getOrdersForArtisan(activeArtisanId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    // Show Snackbars on user actions
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RoleSwitcherBanner(
                currentRole = currentRole,
                onRoleSelected = { viewModel.switchRole(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                RoleType.CUSTOMER -> {
                    if (selectedArtisanDetail != null) {
                        ArtisanDetailScreen(
                            artisan = selectedArtisanDetail!!,
                            services = activeServices,
                            products = activeProducts,
                            reviews = activeReviews,
                            onBack = { viewModel.viewArtisanDetail(null) },
                            onBookService = { service ->
                                bookingArtisan = selectedArtisanDetail
                                bookingPreSelectedService = service
                            },
                            onBuyProduct = { product ->
                                selectedArtisanDetail?.let {
                                    purchasingProduct = Pair(it, product)
                                }
                            },
                            onCallArtisan = { phone ->
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Artisan Phone: $phone", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    } else if (customerTab == 0) {
                        CustomerHomeScreen(
                            categories = categories,
                            filteredArtisans = filteredArtisans,
                            filterState = filterState,
                            selectedTab = customerTab,
                            onTabSelected = { viewModel.setCustomerTab(it) },
                            onCategorySelected = { viewModel.updateCategoryFilter(it) },
                            onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                            onPriceFilterChanged = { viewModel.updatePriceFilter(it) },
                            onRatingFilterChanged = { viewModel.updateRatingFilter(it) },
                            onToggleAvailability = { viewModel.toggleAvailabilityOnly() },
                            onResetFilters = { viewModel.resetFilters() },
                            onArtisanClick = { viewModel.viewArtisanDetail(it) },
                            onBookServiceClick = { artisan ->
                                bookingArtisan = artisan
                                bookingPreSelectedService = null
                            }
                        )
                    } else {
                        OrderHistoryScreen(
                            orders = allOrders,
                            onCancelOrder = { viewModel.updateOrderStatus(it, "REJECTED", "Cancelled by customer") }
                        )
                    }
                }

                RoleType.ENTREPRENEUR -> {
                    val currentArtisan = allArtisans.find { it.id == selectedEntrepreneurId }
                        ?: allArtisans.firstOrNull()

                    EntrepreneurDashboardScreen(
                        currentArtisan = currentArtisan,
                        allArtisans = allArtisans,
                        services = activeServices,
                        products = activeProducts,
                        orders = entrepreneurOrders,
                        onSelectArtisan = { viewModel.selectEntrepreneur(it) },
                        onToggleAvailability = { id, currentStatus ->
                            viewModel.toggleAvailability(id, currentStatus)
                        },
                        onUpdateOrderStatus = { orderId, newStatus, reason ->
                            viewModel.updateOrderStatus(orderId, newStatus, reason)
                        },
                        onAddService = { title, desc, price, time ->
                            currentArtisan?.let {
                                viewModel.addService(it.id, title, desc, price, time)
                            }
                        },
                        onDeleteService = { viewModel.deleteService(it) },
                        onAddProduct = { title, desc, price, stock, tag, mat ->
                            currentArtisan?.let {
                                viewModel.addProduct(it.id, title, desc, price, stock, tag, mat)
                            }
                        },
                        onDeleteProduct = { viewModel.deleteProduct(it) }
                    )
                }

                RoleType.ADMIN -> {
                    AdminDashboardScreen(
                        pendingArtisans = pendingArtisans,
                        verifiedArtisans = verifiedArtisans,
                        categories = categories,
                        allOrders = allOrders,
                        onApproveArtisan = { viewModel.approveArtisan(it) },
                        onRejectArtisan = { viewModel.rejectArtisan(it) },
                        onToggleCategoryStatus = { id, status ->
                            viewModel.toggleCategoryStatus(id, status)
                        },
                        onAddCategory = { name, localized, icon, desc ->
                            viewModel.addCategory(name, localized, icon, desc)
                        }
                    )
                }
            }
        }
    }

    // Book Service Dialog Flow
    bookingArtisan?.let { artisan ->
        val dialogServices by viewModel.getServicesForArtisan(artisan.id)
            .collectAsStateWithLifecycle(initialValue = emptyList())

        BookServiceDialog(
            artisan = artisan,
            services = dialogServices,
            preSelectedService = bookingPreSelectedService,
            onDismiss = {
                bookingArtisan = null
                bookingPreSelectedService = null
            },
            onConfirmBooking = { service, name, phone, address, slot, notes ->
                viewModel.placeServiceRequest(
                    artisan = artisan,
                    service = service,
                    customerName = name,
                    customerPhone = phone,
                    customerAddress = address,
                    scheduledDate = slot,
                    notes = notes
                )
                bookingArtisan = null
                bookingPreSelectedService = null
            }
        )
    }

    // Buy Product Dialog Flow
    purchasingProduct?.let { (artisan, product) ->
        BuyProductDialog(
            artisan = artisan,
            product = product,
            onDismiss = { purchasingProduct = null },
            onConfirmPurchase = { quantity, name, phone, address, notes ->
                viewModel.buyProduct(
                    artisan = artisan,
                    product = product,
                    quantity = quantity,
                    customerName = name,
                    customerPhone = phone,
                    customerAddress = address,
                    notes = notes
                )
                purchasingProduct = null
            }
        )
    }
}
