package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueInfo
import com.example.ui.theme.BlueInfoContainer
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSage
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer

@Composable
fun OrderStatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label, icon) = when (status.uppercase()) {
        "PENDING" -> Quadruple(
            PendingAmberContainer,
            PendingAmber,
            "Pending Approval",
            Icons.Default.HourglassTop
        )
        "ACCEPTED" -> Quadruple(
            BlueInfoContainer,
            BlueInfo,
            "Confirmed",
            Icons.Default.CheckCircle
        )
        "IN_PROGRESS" -> Quadruple(
            Color(0xFFEDE9FE),
            Color(0xFF6D28D9),
            "In Progress",
            Icons.Default.PlayArrow
        )
        "COMPLETED" -> Quadruple(
            SuccessGreenContainer,
            SuccessGreen,
            "Completed",
            Icons.Default.CheckCircle
        )
        "REJECTED" -> Quadruple(
            Color(0xFFFFE4E6),
            Color(0xFFE11D48),
            "Declined",
            Icons.Outlined.Cancel
        )
        else -> Quadruple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status,
            Icons.Default.HourglassTop
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEFF6FF))
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Verified,
            contentDescription = "Verified Artisan",
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "VERIFIED",
            color = Color(0xFF1D4ED8),
            fontSize = 9.sp,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AvailabilityDot(isAvailable: Boolean, modifier: Modifier = Modifier) {
    val color = if (isAvailable) SuccessGreen else Color(0xFF9CA3AF)
    val text = if (isAvailable) "Available Now" else "Busy / Away"

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = if (isAvailable) SuccessGreen else Color(0xFF6B7280),
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

