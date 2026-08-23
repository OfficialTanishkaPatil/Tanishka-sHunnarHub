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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.ui.components.ArtisanCard
import com.example.ui.components.CategoryChipRow
import com.example.ui.model.FilterState
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricDark
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricTextSecondary

@Composable
fun CustomerHomeScreen(
    categories: List<CategoryEntity>,
    filteredArtisans: List<ArtisanEntity>,
    filterState: FilterState,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onPriceFilterChanged: (Double) -> Unit,
    onRatingFilterChanged: (Double) -> Unit,
    onToggleAvailability: () -> Unit,
    onResetFilters: () -> Unit,
    onArtisanClick: (ArtisanEntity) -> Unit,
    onBookServiceClick: (ArtisanEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterPanel by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Geometric Customer Sub-Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GeometricPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GeometricPrimary,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.testTag("tab_explore_artisans"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Explore Crafts",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.testTag("tab_order_history"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "My Orders & Requests",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Geometric Balance Hero Banner
                item {
                    HeroArtisanBanner()
                }

                // Search Bar + Filter Toggle Button
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = filterState.searchQuery,
                                onValueChange = onSearchQueryChanged,
                                placeholder = { Text("Search cobbler, potter, tailor, skill...", fontSize = 13.sp, color = GeometricTextSecondary) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = GeometricTextSecondary
                                    )
                                },
                                trailingIcon = {
                                    if (filterState.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onSearchQueryChanged("") }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = GeometricTextSecondary)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricPrimary,
                                    unfocusedBorderColor = GeometricBorder,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_search_crafts")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { showFilterPanel = !showFilterPanel },
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (showFilterPanel) GeometricPrimary else MaterialTheme.colorScheme.surface)
                                    .border(1.dp, if (showFilterPanel) GeometricPrimary else GeometricBorder, RoundedCornerShape(16.dp))
                                    .testTag("btn_toggle_filters")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filters",
                                    tint = if (showFilterPanel) Color.White else GeometricPrimary
                                )
                            }
                        }

                        // Expandable filter controls
                        if (showFilterPanel) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FilterOptionsCard(
                                filterState = filterState,
                                onPriceChange = onPriceFilterChanged,
                                onRatingChange = onRatingFilterChanged,
                                onToggleAvailability = onToggleAvailability,
                                onReset = onResetFilters
                            )
                        }
                    }
                }

                // Category Chips Row with Geometric Uppercase Subheader
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "CRAFT CATEGORIES",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = GeometricTextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    CategoryChipRow(
                        categories = categories,
                        selectedCategory = filterState.selectedCategory,
                        onCategorySelected = onCategorySelected,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Header Results Count
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Local Micro-Entrepreneurs (${filteredArtisans.size})",
                            fontFamily = FontFamily.Serif,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (filterState.selectedCategory != "All" || filterState.searchQuery.isNotBlank() || filterState.onlyAvailable) {
                            Text(
                                text = "FILTERED",
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp,
                                color = GeometricPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Empty State or Artisan Cards
                if (filteredArtisans.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(GeometricBorder),
                                width = 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    tint = GeometricPrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No Artisans Found",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try clearing search keywords or expanding your price/category filter.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedButton(
                                    onClick = onResetFilters,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Reset All Filters", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(filteredArtisans) { artisan ->
                        ArtisanCard(
                            artisan = artisan,
                            onClick = { onArtisanClick(artisan) },
                            onBookService = { onBookServiceClick(artisan) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroArtisanBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "VOCAL FOR LOCAL",
                            color = Color.White,
                            fontSize = 9.sp,
                            letterSpacing = 0.8.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Text(
                        text = "100% Direct to Artisan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Empowering Traditional Craft Masters & Local Service Providers",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Book master cobblers, clay potters, bespoke tailors, and discover authentic handmade wares right in your neighborhood.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun FilterOptionsCard(
    filterState: FilterState,
    onPriceChange: (Double) -> Unit,
    onRatingChange: (Double) -> Unit,
    onToggleAvailability: () -> Unit,
    onReset: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "FILTER REFINEMENTS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp,
                    color = GeometricTextSecondary
                )
                Text(
                    text = "Reset All",
                    color = GeometricPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onReset)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Price slider
            Text(
                text = "Max Starting Price: ₹${filterState.maxPrice.toInt()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Slider(
                value = filterState.maxPrice.toFloat(),
                onValueChange = { onPriceChange(it.toDouble()) },
                valueRange = 100f..2000f,
                steps = 18,
                colors = SliderDefaults.colors(
                    thumbColor = GeometricPrimary,
                    activeTrackColor = GeometricPrimary
                )
            )

            // Rating chips
            Text(
                text = "Minimum Rating",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(0.0 to "All Ratings", 4.5 to "4.5★+", 4.8 to "4.8★+").forEach { (rating, label) ->
                    val isSelected = filterState.minRating == rating
                    FilterChip(
                        selected = isSelected,
                        onClick = { onRatingChange(rating) },
                        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GeometricPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) GeometricPrimary else GeometricBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Availability toggle
            FilterChip(
                selected = filterState.onlyAvailable,
                onClick = onToggleAvailability,
                label = { Text(if (filterState.onlyAvailable) "Showing Available Only" else "Include Away Artisans", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                shape = RoundedCornerShape(10.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GeometricPrimary,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filterState.onlyAvailable,
                    borderColor = if (filterState.onlyAvailable) GeometricPrimary else GeometricBorder
                )
            )
        }
    }
}

