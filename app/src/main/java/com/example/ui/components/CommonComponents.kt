package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GuineanCities
import com.example.data.model.ShipmentStatus
import com.example.data.model.UserRole
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavySurface
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusInTransit
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusReady

@Composable
fun GuineaFlagStripe(modifier: Modifier = Modifier, height: Int = 4) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height.dp)
                .background(GuineaRed)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height.dp)
                .background(GuineaYellow)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height.dp)
                .background(GuineaGreen)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuineeTransitTopAppBar(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    isSandbox: Boolean,
    onToggleSandbox: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column {
        GuineaFlagStripe(height = 3)
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GuineaGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBus,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Guinée",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Transit",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = GuineaYellow
                            )
                        }
                        Text(
                            text = "Transport & Logistique Nationale",
                            fontSize = 10.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DeepNavy,
                titleContentColor = Color.White
            ),
            actions = {
                // Role Picker button
                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DeepNavySurface,
                        modifier = Modifier
                            .clickable { menuExpanded = true }
                            .padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Rôle",
                                tint = GuineaYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (currentRole) {
                                    UserRole.CLIENT -> "Client"
                                    UserRole.AGENCY_AGENT -> "Agent"
                                    UserRole.DRIVER -> "Chauffeur"
                                    UserRole.ADMIN -> "Admin"
                                },
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = role.label, fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal)
                                    }
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Sandbox Indicator Icon
                IconButton(
                    onClick = onToggleSandbox,
                    modifier = Modifier.testTag("sandbox_toggle_btn")
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSandbox) GuineaYellow else Color.Transparent)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isSandbox) "DÉMO" else "PROD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSandbox) DeepNavy else Color.White
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun StatusBadge(status: ShipmentStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        ShipmentStatus.CREATED, ShipmentStatus.AWAITING_PAYMENT -> Pair(Color(0xFFEDE9FE), StatusPending)
        ShipmentStatus.PAID -> Pair(Color(0xFFE0F2FE), Color(0xFF0369A1))
        ShipmentStatus.RECEIVED_AT_ORIGIN, ShipmentStatus.PROCESSING -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
        ShipmentStatus.IN_TRANSIT -> Pair(Color(0xFFFFFBEB), StatusInTransit)
        ShipmentStatus.ARRIVED_AT_DESTINATION -> Pair(Color(0xFFEFF6FF), StatusReady)
        ShipmentStatus.READY_FOR_PICKUP, ShipmentStatus.OUT_FOR_DELIVERY -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
        ShipmentStatus.DELIVERED -> Pair(Color(0xFFDCFCE7), StatusDelivered)
        ShipmentStatus.CANCELLED, ShipmentStatus.FAILED_DELIVERY, ShipmentStatus.RETURNED -> Pair(Color(0xFFFEE2E2), StatusCancelled)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.label,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Procedural Vector QR Code pattern generator for tickets and parcel manifests.
 * Renders authentic 2D matrix squares with position detection anchors.
 */
@Composable
fun QrCodeView(
    payload: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 140,
    primaryColor: Color = DeepNavy,
    backgroundColor: Color = Color.White
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.size(sizeDp.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size((sizeDp - 24).dp)) {
                val canvasSize = size.minDimension
                val gridCount = 21 // Standard Version 1 QR matrix 21x21
                val cellSize = canvasSize / gridCount

                // Background
                drawRect(color = backgroundColor)

                // Deterministic pseudo-random pattern based on payload hash
                val hash = payload.hashCode()

                // Corner Finder Pattern Top-Left
                drawCornerFinder(0, 0, cellSize, primaryColor)
                // Corner Finder Pattern Top-Right
                drawCornerFinder(gridCount - 7, 0, cellSize, primaryColor)
                // Corner Finder Pattern Bottom-Left
                drawCornerFinder(0, gridCount - 7, cellSize, primaryColor)

                // Data module filler
                for (row in 0 until gridCount) {
                    for (col in 0 until gridCount) {
                        // Skip finder patterns
                        val inTopLeft = row < 7 && col < 7
                        val inTopRight = row < 7 && col >= gridCount - 7
                        val inBottomLeft = row >= gridCount - 7 && col < 7
                        if (!inTopLeft && !inTopRight && !inBottomLeft) {
                            // Seeded module determination
                            val isFilled = ((row * 31 + col * 17 + hash) % 3) != 0
                            if (isFilled) {
                                drawRect(
                                    color = primaryColor,
                                    topLeft = Offset(col * cellSize, row * cellSize),
                                    size = androidx.compose.ui.geometry.Size(cellSize * 0.95f, cellSize * 0.95f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCornerFinder(
    startCol: Int,
    startRow: Int,
    cellSize: Float,
    color: Color
) {
    // Outer 7x7 box
    drawRect(
        color = color,
        topLeft = Offset(startCol * cellSize, startRow * cellSize),
        size = androidx.compose.ui.geometry.Size(7 * cellSize, 7 * cellSize)
    )
    // Inner 5x5 clear space
    drawRect(
        color = Color.White,
        topLeft = Offset((startCol + 1) * cellSize, (startRow + 1) * cellSize),
        size = androidx.compose.ui.geometry.Size(5 * cellSize, 5 * cellSize)
    )
    // Center 3x3 solid box
    drawRect(
        color = color,
        topLeft = Offset((startCol + 2) * cellSize, (startRow + 2) * cellSize),
        size = androidx.compose.ui.geometry.Size(3 * cellSize, 3 * cellSize)
    )
}

/**
 * Interactive Guinean Route Map graphic
 * Visualizes the 4 main regions of Guinea and the journey checkpoints
 */
@Composable
fun GuineeRouteMapCard(
    originCity: String,
    currentCity: String,
    destinationCity: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepNavy),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AltRoute,
                        contentDescription = null,
                        tint = GuineaYellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Itinéraire & Géolocalisation Guinée",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Surface(
                    color = DeepNavySurface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "RN1 / Fouta / Haute Guinée",
                        color = GuineaGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stylized Route Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            ) {
                val w = size.width
                val h = size.height

                // Waypoints across canvas representing Conakry -> Kindia -> Mamou -> Labé / Kankan
                val pOrigin = Offset(w * 0.12f, h * 0.65f)
                val pKindia = Offset(w * 0.35f, h * 0.50f)
                val pMamou = Offset(w * 0.55f, h * 0.40f)
                val pDestination = Offset(w * 0.88f, h * 0.25f)

                // Draw connecting road network
                drawLine(
                    color = Color(0xFF334155),
                    start = pOrigin,
                    end = pKindia,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF334155),
                    start = pKindia,
                    end = pMamou,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF334155),
                    start = pMamou,
                    end = pDestination,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )

                // Active progress line
                drawLine(
                    color = GuineaGreen,
                    start = pOrigin,
                    end = pMamou,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )

                // Draw checkpoints
                drawCircle(color = GuineaYellow, radius = 9f, center = pOrigin)
                drawCircle(color = Color(0xFF0F172A), radius = 4f, center = pOrigin)

                drawCircle(color = GuineaGreen, radius = 8f, center = pKindia)
                drawCircle(color = GuineaGreen, radius = 12f, center = pMamou) // Current position
                drawCircle(color = Color.White, radius = 5f, center = pMamou)

                drawCircle(color = GuineaRed, radius = 10f, center = pDestination)
                drawCircle(color = Color.White, radius = 4f, center = pDestination)
            }

            // Labels below route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "Départ", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text(text = originCity, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Dernière étape", color = GuineaYellow, fontSize = 11.sp)
                    Text(text = currentCity, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Destination", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text(text = destinationCity, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
