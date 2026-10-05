package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.TripEntity
import com.example.ui.components.GuineaFlagStripe
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavySurface
import com.example.ui.theme.GuineaGold
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.GuineeTransitViewModel

@Composable
fun HomeScreen(
    viewModel: GuineeTransitViewModel,
    onNavigateToTrips: () -> Unit,
    onNavigateToSendParcel: () -> Unit,
    onNavigateToTracking: (String) -> Unit,
    onNavigateToAgencies: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quickTrackingQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Section with Image & Guinean Identity
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // Background Hero Image
                Image(
                    painter = painterResource(id = R.drawable.hero_guinee_transit),
                    contentDescription = "GuinéeTransit Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x990D1B2A),
                                    Color(0xF00D1B2A)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = GuineaGreen,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "RÉSEAU NATIONAL DE GUINÉE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Voyagez en Guinée.\nEnvoyez vos colis.\nSuivez-les à chaque étape.",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Une plateforme simple et sécurisée pour réserver vos trajets et expédier vos colis entre les différentes régions de la Guinée.",
                        color = Color(0xFFE2E8F0),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Quick Tracking Search Bar (Card overlay)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = GuineaGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Où est mon colis ?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DeepNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = quickTrackingQuery,
                            onValueChange = { quickTrackingQuery = it.uppercase() },
                            placeholder = { Text("Ex: GT-2026-00018452", fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_tracking_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GuineaGreen,
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    val query = quickTrackingQuery.ifBlank { "GT-2026-00018452" }
                                    viewModel.loadShipmentForTracking(query)
                                    onNavigateToTracking(query)
                                }
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val query = quickTrackingQuery.ifBlank { "GT-2026-00018452" }
                                viewModel.loadShipmentForTracking(query)
                                onNavigateToTracking(query)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("quick_tracking_btn")
                        ) {
                            Text("Suivre", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    // Quick suggestion pill
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            quickTrackingQuery = "GT-2026-00018452"
                            viewModel.loadShipmentForTracking("GT-2026-00018452")
                            onNavigateToTracking("GT-2026-00018452")
                        }
                    ) {
                        Text(
                            text = "Exemple démo : ",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "GT-2026-00018452 (Conakry → Labé)",
                            fontSize = 11.sp,
                            color = GuineaGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Main Actions (3 Buttons)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Services Principaux",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Action 1: Réserver un trajet
                    MainActionCard(
                        title = "Réserver\nun trajet",
                        subtitle = "Voyages interurbains",
                        icon = Icons.Default.DirectionsBus,
                        iconTint = GuineaGreen,
                        badgeColor = Color(0xFFE8F5E9),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("home_book_trip_btn"),
                        onClick = onNavigateToTrips
                    )

                    // Action 2: Envoyer un colis
                    MainActionCard(
                        title = "Envoyer\nun colis",
                        subtitle = "Colis & fret express",
                        icon = Icons.Default.LocalShipping,
                        iconTint = GuineaRed,
                        badgeColor = Color(0xFFFFEBEE),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("home_send_parcel_btn"),
                        onClick = onNavigateToSendParcel
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action 3: Suivre mon colis
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DeepNavy,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTracking("GT-2026-00018452") }
                        .testTag("home_track_parcel_action")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(GuineaYellow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = DeepNavy,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Suivre un colis",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Consultez l'historique et la localisation en direct",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = GuineaYellow
                        )
                    }
                }
            }
        }

        // Popular Routes in Guinea
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trajets Populaires en Guinée",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Voir tout",
                        color = GuineaGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToTrips() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val popularRoutes = listOf(
                    Triple("Conakry", "Labé", "150 000 GNF"),
                    Triple("Conakry", "Kankan", "220 000 GNF"),
                    Triple("Conakry", "Kindia", "60 000 GNF"),
                    Triple("Conakry", "Boké", "85 000 GNF"),
                    Triple("Conakry", "Nzérékoré", "300 000 GNF")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(popularRoutes) { route ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .width(180.dp)
                                .clickable {
                                    viewModel.originFilter.value = route.first
                                    viewModel.destinationFilter.value = route.second
                                    onNavigateToTrips()
                                }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${route.first} → ${route.second}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DeepNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Départs quotidiens",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = route.third,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GuineaGreen,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Trust & Guarantee Badges
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Pourquoi choisir GuinéeTransit ?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TrustFeatureCard(
                        icon = Icons.Default.Security,
                        title = "Sécurité & Traçabilité",
                        desc = "QR Code & code OTP secret pour chaque colis",
                        modifier = Modifier.weight(1f)
                    )
                    TrustFeatureCard(
                        icon = Icons.Default.Speed,
                        title = "Paiement Mobile",
                        desc = "Orange Money, MTN MoMo & agence",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TrustFeatureCard(
                        icon = Icons.Default.Business,
                        title = "10 Agences Nationales",
                        desc = "Présent à Conakry, Kindia, Mamou, Labé, Kankan...",
                        modifier = Modifier.weight(1f)
                    )
                    TrustFeatureCard(
                        icon = Icons.Default.VerifiedUser,
                        title = "Flotte Contrôlée",
                        desc = "Chauffeurs certifiés et minibus VIP récents",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun MainActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DeepNavy,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun TrustFeatureCard(
    icon: ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GuineaGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DeepNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                lineHeight = 15.sp
            )
        }
    }
}
