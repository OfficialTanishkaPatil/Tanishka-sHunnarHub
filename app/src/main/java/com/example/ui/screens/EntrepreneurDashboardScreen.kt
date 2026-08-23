package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ServiceEntity
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.VerifiedBadge
import com.example.ui.dialogs.AddEditProductDialog
import com.example.ui.dialogs.AddEditServiceDialog
import com.example.ui.theme.GeometricAccentGold
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricDark
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricSage
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.theme.SuccessGreen

@Composable
fun EntrepreneurDashboardScreen(
    currentArtisan: ArtisanEntity?,
    allArtisans: List<ArtisanEntity>,
    services: List<ServiceEntity>,
    products: List<ProductEntity>,
    orders: List<OrderRequestEntity>,
    onSelectArtisan: (Long) -> Unit,
    onToggleAvailability: (Long, Boolean) -> Unit,
    onUpdateOrderStatus: (Long, String, String) -> Unit,
    onAddService: (title: String, description: String, price: Double, time: String) -> Unit,
    onDeleteService: (Long) -> Unit,
    onAddProduct: (title: String, description: String, price: Double, stock: Int, tag: String, mat: String) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDashboardTab by remember { mutableIntStateOf(0) }
    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    val artisan = currentArtisan ?: allArtisans.firstOrNull()

    if (artisan == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading artisan profile...")
        }
        return
    }

    val pendingOrders = orders.filter { it.status == "PENDING" }
    val activeOrders = orders.filter { it.status in listOf("ACCEPTED", "IN_PROGRESS") }
    val completedOrders = orders.filter { it.status == "COMPLETED" }
    val totalEarnings = completedOrders.sumOf { it.price }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Artisan Switcher Bar (Quick tester convenience)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "SWITCH MICRO-ENTREPRENEUR PROFILE",
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold,
                        color = GeometricTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(allArtisans.filter { it.isVerified }) { a ->
                            val isSelected = a.id == artisan.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) GeometricPrimary else MaterialTheme.colorScheme.surface)
                                    .border(1.dp, if (isSelected) GeometricPrimary else GeometricBorder, RoundedCornerShape(12.dp))
                                    .clickable { onSelectArtisan(a.id) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("artisan_switch_${a.id}")
                            ) {
                                Text(
                                    text = a.name.split(" ").firstOrNull() ?: a.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Top Profile & Availability Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(artisan.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = artisan.name
                                    .split(" ")
                                    .take(2)
                                    .mapNotNull { it.firstOrNull()?.toString() }
                                    .joinToString("")
                                Text(
                                    text = initials,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = artisan.name,
                                    fontFamily = FontFamily.Serif,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = artisan.craftTitle.uppercase(),
                                    fontSize = 11.sp,
                                    letterSpacing = 0.5.sp,
                                    color = GeometricPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "📍 ${artisan.location}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Availability Toggle Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (artisan.isAvailable) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (artisan.isAvailable) Color(0xFFBBF7D0) else GeometricBorder, RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (artisan.isAvailable) "🟢 Shop Status: Online & Open" else "⚪ Shop Status: Offline / Taking a Break",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (artisan.isAvailable) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Manage your live store presence on HunarHub",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = artisan.isAvailable,
                                onCheckedChange = { onToggleAvailability(artisan.id, artisan.isAvailable) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = SuccessGreen
                                ),
                                modifier = Modifier.testTag("switch_availability")
                            )
                        }
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardStatCard(
                        title = "Pending",
                        value = "${pendingOrders.size}",
                        icon = Icons.Default.NotificationsActive,
                        color = Color(0xFFD97706),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardStatCard(
                        title = "Active Jobs",
                        value = "${activeOrders.size}",
                        icon = Icons.Default.Handyman,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardStatCard(
                        title = "Earnings",
                        value = "₹${totalEarnings.toInt()}",
                        icon = Icons.Default.CurrencyRupee,
                        color = GeometricSage,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardStatCard(
                        title = "Rating",
                        value = "${artisan.rating}★",
                        icon = Icons.Default.Star,
                        color = GeometricAccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Dashboard Management Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedDashboardTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = GeometricPrimary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedDashboardTab]),
                            color = GeometricPrimary,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedDashboardTab == 0,
                        onClick = { selectedDashboardTab = 0 },
                        text = { Text("Customer Requests (${orders.size})", fontWeight = if (selectedDashboardTab == 0) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp) }
                    )
                    Tab(
                        selected = selectedDashboardTab == 1,
                        onClick = { selectedDashboardTab = 1 },
                        text = { Text("My Services (${services.size})", fontWeight = if (selectedDashboardTab == 1) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp) }
                    )
                    Tab(
                        selected = selectedDashboardTab == 2,
                        onClick = { selectedDashboardTab = 2 },
                        text = { Text("Store Products (${products.size})", fontWeight = if (selectedDashboardTab == 2) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp) }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Tab Contents
            when (selectedDashboardTab) {
                0 -> { // Incoming requests & orders
                    if (orders.isEmpty()) {
                        item {
                            EmptySectionText(text = "No incoming customer requests yet.")
                        }
                    } else {
                        items(orders) { order ->
                            EntrepreneurOrderManagementCard(
                                order = order,
                                onAccept = { onUpdateOrderStatus(order.id, "ACCEPTED", "") },
                                onReject = { onUpdateOrderStatus(order.id, "REJECTED", "Artisan fully booked") },
                                onStartProgress = { onUpdateOrderStatus(order.id, "IN_PROGRESS", "") },
                                onComplete = { onUpdateOrderStatus(order.id, "COMPLETED", "") },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                1 -> { // Services management
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Your Service Catalog", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Button(
                                onClick = { showAddServiceDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary),
                                modifier = Modifier.testTag("btn_open_add_service")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Service", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (services.isEmpty()) {
                        item {
                            EmptySectionText(text = "No services listed. Add your first service above!")
                        }
                    } else {
                        items(services) { service ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                                    width = 1.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = service.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = service.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "₹${service.price.toInt()} • Turnaround: ${service.estimatedTime}", fontSize = 11.sp, color = GeometricPrimary, fontWeight = FontWeight.SemiBold)
                                    }

                                    IconButton(onClick = { onDeleteService(service.id) }) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> { // Products management
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Your Handmade Products", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Button(
                                onClick = { showAddProductDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary),
                                modifier = Modifier.testTag("btn_open_add_product")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("List Product", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (products.isEmpty()) {
                        item {
                            EmptySectionText(text = "No handmade products listed yet.")
                        }
                    } else {
                        items(products) { product ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                                    width = 1.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = product.title, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = product.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "₹${product.price.toInt()} • Stock: ${product.stock} units", fontSize = 11.sp, color = GeometricPrimary, fontWeight = FontWeight.SemiBold)
                                    }

                                    IconButton(onClick = { onDeleteProduct(product.id) }) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddServiceDialog) {
        AddEditServiceDialog(
            onDismiss = { showAddServiceDialog = false },
            onSaveService = { title, desc, price, time ->
                onAddService(title, desc, price, time)
                showAddServiceDialog = false
            }
        )
    }

    if (showAddProductDialog) {
        AddEditProductDialog(
            onDismiss = { showAddProductDialog = false },
            onSaveProduct = { title, desc, price, stock, tag, mat ->
                onAddProduct(title, desc, price, stock, tag, mat)
                showAddProductDialog = false
            }
        )
    }
}

@Composable
private fun DashboardStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
            width = 1.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = color)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun EntrepreneurOrderManagementCard(
    order: OrderRequestEntity,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onStartProgress: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
            width = 1.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (order.orderType == "SERVICE") "SERVICE BOOKING" else "PRODUCT ORDER",
                    fontSize = 10.sp,
                    letterSpacing = 0.6.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeometricPrimary
                )
                OrderStatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.itemTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Customer: ${order.customerName} (${order.customerPhone})",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "📍 ${order.customerAddress}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "📅 Schedule: ${order.scheduledDate}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (order.customerNotes.isNotBlank()) {
                Text(
                    text = "Customer Note: \"${order.customerNotes}\"",
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = GeometricPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Value: ₹${order.price.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GeometricPrimary
                )

                // Action buttons based on current status
                when (order.status) {
                    "PENDING" -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = onReject,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_reject_${order.id}")
                            ) {
                                Text("Decline", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                            }
                            Button(
                                onClick = onAccept,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                modifier = Modifier.testTag("btn_accept_${order.id}")
                            ) {
                                Text("Accept", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    "ACCEPTED" -> {
                        Button(
                            onClick = onStartProgress,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            modifier = Modifier.testTag("btn_start_${order.id}")
                        ) {
                            Text("Mark In Progress", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    "IN_PROGRESS" -> {
                        Button(
                            onClick = onComplete,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.testTag("btn_complete_${order.id}")
                        ) {
                            Text("Mark Completed ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    "COMPLETED" -> {
                        Text(
                            text = "✓ Order Fulfilled",
                            fontSize = 11.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    "REJECTED" -> {
                        Text(
                            text = "Declined",
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySectionText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

