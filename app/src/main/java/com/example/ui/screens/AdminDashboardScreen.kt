package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.GeometricAccentGold
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricDark
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricSage
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.theme.SuccessGreen

@Composable
fun AdminDashboardScreen(
    pendingArtisans: List<ArtisanEntity>,
    verifiedArtisans: List<ArtisanEntity>,
    categories: List<CategoryEntity>,
    allOrders: List<OrderRequestEntity>,
    onApproveArtisan: (Long) -> Unit,
    onRejectArtisan: (Long) -> Unit,
    onToggleCategoryStatus: (Long, Boolean) -> Unit,
    onAddCategory: (name: String, localized: String, icon: String, desc: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var adminTab by remember { mutableIntStateOf(0) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val totalGMV = allOrders.filter { it.status == "COMPLETED" }.sumOf { it.price }
    val totalOrdersCount = allOrders.size
    val totalArtisansCount = verifiedArtisans.size + pendingArtisans.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                width = 1.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GeometricPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Admin Governance Console",
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Micro-Entrepreneur Quality, Metrics & Approval",
                        fontSize = 11.sp,
                        color = GeometricTextSecondary
                    )
                }
            }
        }

        // Admin Tabs
        ScrollableTabRow(
            selectedTabIndex = adminTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GeometricPrimary,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                if (adminTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[adminTab]),
                        color = GeometricPrimary,
                        height = 3.dp
                    )
                }
            }
        ) {
            Tab(
                selected = adminTab == 0,
                onClick = { adminTab = 0 },
                modifier = Modifier.testTag("admin_tab_queue"),
                text = {
                    Text(
                        "Queue (${pendingArtisans.size})",
                        fontWeight = if (adminTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (adminTab == 0) GeometricPrimary else GeometricTextSecondary
                    )
                }
            )
            Tab(
                selected = adminTab == 1,
                onClick = { adminTab = 1 },
                modifier = Modifier.testTag("admin_tab_categories"),
                text = {
                    Text(
                        "Categories (${categories.size})",
                        fontWeight = if (adminTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (adminTab == 1) GeometricPrimary else GeometricTextSecondary
                    )
                }
            )
            Tab(
                selected = adminTab == 2,
                onClick = { adminTab = 2 },
                modifier = Modifier.testTag("admin_tab_analytics"),
                text = {
                    Text(
                        "Platform Analytics",
                        fontWeight = if (adminTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (adminTab == 2) GeometricPrimary else GeometricTextSecondary
                    )
                }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (adminTab) {
                0 -> { // Verification Queue
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                                width = 1.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(GeometricPrimary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.HowToReg, contentDescription = null, tint = GeometricPrimary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Verify authentic craftsmen identities to grant public marketplace visibility and trust badge.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (pendingArtisans.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Verification Queue Clear!",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "All artisan applicants have been reviewed.",
                                        fontSize = 12.sp,
                                        color = GeometricTextSecondary
                                    )
                                }
                            }
                        }
                    } else {
                        items(pendingArtisans) { artisan ->
                            PendingArtisanVerificationCard(
                                artisan = artisan,
                                onApprove = { onApproveArtisan(artisan.id) },
                                onReject = { onRejectArtisan(artisan.id) }
                            )
                        }
                    }
                }
                1 -> { // Category Management
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Marketplace Categories",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Button(
                                onClick = { showAddCategoryDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary),
                                modifier = Modifier.testTag("btn_admin_add_category")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Category", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    items(categories) { category ->
                        Card(
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getCategoryIcon(category.iconName),
                                            contentDescription = null,
                                            tint = GeometricPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = category.localizedName,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(text = category.description, fontSize = 11.sp, color = GeometricTextSecondary, maxLines = 1)
                                    }
                                }

                                Switch(
                                    checked = category.isActive,
                                    onCheckedChange = { onToggleCategoryStatus(category.id, category.isActive) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = GeometricPrimary
                                    ),
                                    modifier = Modifier.testTag("switch_category_${category.id}")
                                )
                            }
                        }
                    }
                }
                2 -> { // Analytics
                    item {
                        AnalyticsOverviewSection(
                            totalGMV = totalGMV,
                            totalOrders = totalOrdersCount,
                            verifiedArtisansCount = verifiedArtisans.size,
                            pendingCount = pendingArtisans.size,
                            categories = categories,
                            orders = allOrders
                        )
                    }
                }
            }
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onSave = { name, localized, icon, desc ->
                onAddCategory(name, localized, icon, desc)
                showAddCategoryDialog = false
            }
        )
    }
}

@Composable
private fun PendingArtisanVerificationCard(
    artisan: ArtisanEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
            width = 1.dp
        ),
        modifier = Modifier.fillMaxWidth().testTag("pending_artisan_${artisan.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(artisan.avatarColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = artisan.name.split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
                    Text(text = initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = artisan.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = artisan.craftTitle.uppercase(),
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        color = GeometricPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "📍 ${artisan.location} • ${artisan.experienceYears} yrs experience", fontSize = 11.sp, color = GeometricTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = artisan.bio, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp)
            ) {
                Text(
                    text = "📄 Application Notes: ${artisan.verificationNotes}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = GeometricTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReject,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_admin_reject_${artisan.id}")
                ) {
                    Text("Reject Application", fontSize = 11.sp, color = Color(0xFFDC2626))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onApprove,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary),
                    modifier = Modifier.testTag("btn_admin_approve_${artisan.id}")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve & Publish", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AnalyticsOverviewSection(
    totalGMV: Double,
    totalOrders: Int,
    verifiedArtisansCount: Int,
    pendingCount: Int,
    categories: List<CategoryEntity>,
    orders: List<OrderRequestEntity>
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // High level KPI cards
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            KpiCard(
                title = "Total GMV Fulfilled",
                value = "₹${totalGMV.toInt()}",
                icon = Icons.Default.CurrencyRupee,
                color = GeometricPrimary,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Total Orders Placed",
                value = "$totalOrders",
                icon = Icons.Default.ShoppingBag,
                color = GeometricAccentGold,
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            KpiCard(
                title = "Verified Artisans",
                value = "$verifiedArtisansCount",
                icon = Icons.Default.VerifiedUser,
                color = GeometricSage,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Pending Applications",
                value = "$pendingCount",
                icon = Icons.Default.Group,
                color = GeometricDark,
                modifier = Modifier.weight(1f)
            )
        }

        // Breakdown Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                width = 1.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Order Status Distribution",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                listOf("COMPLETED", "IN_PROGRESS", "ACCEPTED", "PENDING", "REJECTED").forEach { status ->
                    val count = orders.count { it.status == status }
                    val percent = if (totalOrders > 0) (count * 100) / totalOrders else 0

                    Column(modifier = Modifier.padding(vertical = 5.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = status, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                            Text(text = "$count ($percent%)", fontSize = 11.sp, color = GeometricTextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(if (percent > 0) percent / 100f else 0.02f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        when(status) {
                                            "COMPLETED" -> SuccessGreen
                                            "PENDING" -> GeometricAccentGold
                                            "REJECTED" -> Color(0xFFE11D48)
                                            else -> GeometricPrimary
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
            width = 1.dp
        ),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, fontSize = 11.sp, color = GeometricTextSecondary)
        }
    }
}

@Composable
private fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, localized: String, icon: String, desc: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var localized by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("palette") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                width = 1.dp
            ),
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Craft Category",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = GeometricPrimary
                    )
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Key (e.g. Weavers)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = localized,
                    onValueChange = { localized = it },
                    label = { Text("Display Name (e.g. Bunkar / Handloom Weavers)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Category Description") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onSave(name, localized.ifBlank { name }, icon, desc)
                        }
                    },
                    enabled = name.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Category", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

