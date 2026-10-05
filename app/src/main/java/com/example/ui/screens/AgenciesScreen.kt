package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AgencyEntity
import com.example.ui.components.GuineaFlagStripe
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@Composable
fun AgenciesScreen(
    viewModel: GuineeTransitViewModel,
    modifier: Modifier = Modifier
) {
    val agencies by viewModel.allAgencies.collectAsState()
    val context = LocalContext.current
    var selectedCityFilter by remember { mutableStateOf("Toutes") }

    val citiesList = listOf("Toutes", "Conakry", "Kindia", "Mamou", "Labé", "Boké", "Kankan", "Nzérékoré", "Faranah", "Kissidougou")

    val filteredAgencies = remember(agencies, selectedCityFilter) {
        if (selectedCityFilter == "Toutes") agencies
        else agencies.filter { it.city.equals(selectedCityFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    GuineaFlagStripe(height = 3)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Nos Agences en Guinée",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "10 agences et comptoirs de fret répartis sur les 4 régions naturelles",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // City Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(citiesList) { cityName ->
                    val isSelected = selectedCityFilter == cityName
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) GuineaGreen else Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier.clickable { selectedCityFilter = cityName }
                    ) {
                        Text(
                            text = cityName,
                            color = if (isSelected) Color.White else DeepNavy,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Agencies List
        items(filteredAgencies) { agency ->
            AgencyItemCard(
                agency = agency,
                onCall = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${agency.phone.replace(" ", "")}"))
                    runCatching { context.startActivity(intent) }
                }
            )
        }
    }
}

@Composable
fun AgencyItemCard(
    agency: AgencyEntity,
    onCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val serviceList = remember(agency.services) {
        agency.services.split(",").map { it.trim() }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("agency_card_${agency.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = GuineaGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = agency.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                        Text(text = agency.city, fontSize = 12.sp, color = GuineaGreen, fontWeight = FontWeight.SemiBold)
                    }
                }

                IconButton(
                    onClick = onCall,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7))
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Appeler", tint = GuineaGreen, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = agency.address, fontSize = 12.sp, color = Color(0xFF475569))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = agency.openingHours, fontSize = 12.sp, color = Color(0xFF475569))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Services Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                serviceList.take(3).forEach { service ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "✓ $service",
                            fontSize = 10.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GPS: ${agency.latitude}, ${agency.longitude}",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )

                Text(
                    text = "📞 ${agency.phone}",
                    fontSize = 11.sp,
                    color = DeepNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
