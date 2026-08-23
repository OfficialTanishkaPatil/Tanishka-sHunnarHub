package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricPrimary

@Composable
fun CategoryChipRow(
    categories: List<CategoryEntity>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" item
        item {
            CategoryChip(
                name = "All",
                label = "ALL CRAFTS",
                icon = Icons.Default.AllInclusive,
                isSelected = selectedCategory == "All",
                onClick = { onCategorySelected("All") }
            )
        }

        items(categories.filter { it.isActive }) { category ->
            val icon = getCategoryIcon(category.iconName)
            CategoryChip(
                name = category.name,
                label = category.name.uppercase(),
                icon = icon,
                isSelected = selectedCategory.equals(category.name, ignoreCase = true),
                onClick = { onCategorySelected(category.name) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) GeometricPrimary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
    val iconColor = if (isSelected) Color.White else GeometricPrimary
    val borderColor = if (isSelected) GeometricPrimary else GeometricBorder

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp)
            .testTag("category_chip_$name"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "pottery" -> Icons.Default.Spa
        "scissors" -> Icons.Default.ContentCut
        "shoe" -> Icons.Default.ShoppingBag
        "palette" -> Icons.Default.Palette
        "storefront" -> Icons.Default.LocalOffer
        else -> Icons.Default.Palette
    }
}

