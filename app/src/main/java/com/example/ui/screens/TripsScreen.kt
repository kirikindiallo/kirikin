package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TripEntity
import com.example.data.model.GuineanCities
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    viewModel: GuineeTransitViewModel,
    onTripSelected: (TripEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val trips by viewModel.allTrips.collectAsState()
    var origin by remember { mutableStateOf(viewModel.originFilter.value) }
    var destination by remember { mutableStateOf(viewModel.destinationFilter.value) }
    var travelDate by remember { mutableStateOf("02 Oct 2026") }
    var passengers by remember { mutableStateOf(1) }

    var originExpanded by remember { mutableStateOf(false) }
    var destinationExpanded by remember { mutableStateOf(false) }

    // Filter trips matching selection
    val filteredTrips = remember(trips, origin, destination) {
        trips.filter { trip ->
            (origin.isBlank() || trip.originCity.equals(origin, ignoreCase = true)) &&
            (destination.isBlank() || trip.destinationCity.equals(destination, ignoreCase = true))
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Réserver un Trajet Interurbain",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Voyages réguliers entre les capitales régionales",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Departure dropdown
                    ExposedDropdownMenuBox(
                        expanded = originExpanded,
                        onExpandedChange = { originExpanded = !originExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = origin,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ville de départ") },
                            leadingIcon = {
                                Icon(Icons.Default.Place, contentDescription = null, tint = GuineaGreen)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("trip_origin_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GuineaGreen,
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = originExpanded,
                            onDismissRequest = { originExpanded = false }
                        ) {
                            GuineanCities.ALL.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text("${city.name} (${city.region})") },
                                    onClick = {
                                        origin = city.name
                                        viewModel.originFilter.value = city.name
                                        originExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Swap button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(
                            onClick = {
                                val temp = origin
                                origin = destination
                                destination = temp
                                viewModel.originFilter.value = origin
                                viewModel.destinationFilter.value = destination
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                        ) {
                            Icon(Icons.Default.SwapVert, contentDescription = "Inverser", tint = DeepNavy)
                        }
                    }

                    // Destination dropdown
                    ExposedDropdownMenuBox(
                        expanded = destinationExpanded,
                        onExpandedChange = { destinationExpanded = !destinationExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = destination,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ville de destination") },
                            leadingIcon = {
                                Icon(Icons.Default.Place, contentDescription = null, tint = GuineaRed)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinationExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("trip_dest_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GuineaGreen,
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = destinationExpanded,
                            onDismissRequest = { destinationExpanded = false }
                        ) {
                            GuineanCities.ALL.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text("${city.name} (${city.region})") },
                                    onClick = {
                                        destination = city.name
                                        viewModel.destinationFilter.value = city.name
                                        destinationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date & Passengers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = travelDate,
                            onValueChange = { travelDate = it },
                            label = { Text("Date") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = "$passengers voy.",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Places") },
                            leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.originFilter.value = origin
                            viewModel.destinationFilter.value = destination
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("search_trips_btn")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GuineaYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rechercher les départs", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Results Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredTrips.size} départs disponibles",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepNavy
                )
                Text(
                    text = "$origin → $destination",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = GuineaGreen
                )
            }
        }

        // Trip Cards
        if (filteredTrips.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Aucun départ direct trouvé", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Essayez d'inverser l'itinéraire ou vérifiez les liaisons Conakry - Labé ou Conakry - Kankan.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        } else {
            items(filteredTrips) { trip ->
                TripResultCard(
                    trip = trip,
                    onSelect = {
                        viewModel.selectTrip(trip)
                        onTripSelected(trip)
                    }
                )
            }
        }
    }
}

@Composable
fun TripResultCard(
    trip: TripEntity,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookedList = if (trip.bookedSeatsCsv.isBlank()) emptyList() else trip.bookedSeatsCsv.split(",")
    val remainingSeats = (trip.totalSeats - bookedList.size).coerceAtLeast(0)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("trip_card_${trip.tripNumber}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Company & Vehicle Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = GuineaGreen, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = trip.companyName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                        Text(text = trip.vehicleType, fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (remainingSeats <= 3) Color(0xFFFFEBEE) else Color(0xFFE0F2FE)
                ) {
                    Text(
                        text = "$remainingSeats places restantes",
                        color = if (remainingSeats <= 3) GuineaRed else Color(0xFF0369A1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time & Route Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = trip.departureTime, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DeepNavy)
                    Text(text = trip.originCity, fontSize = 13.sp, color = Color(0xFF475569))
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = trip.durationHours, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Direct", fontSize = 10.sp, color = GuineaGreen)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = trip.estimatedArrival, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DeepNavy)
                    Text(text = trip.destinationCity, fontSize = 13.sp, color = Color(0xFF475569))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities snippet
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✓ " + trip.amenities.replace(", ", "  ✓ "),
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Price & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Tarif par passager", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(
                        text = "${String.format("%,d", trip.priceGnf)} GNF",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = GuineaGreen
                    )
                }

                Button(
                    onClick = onSelect,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                    modifier = Modifier.testTag("select_trip_${trip.tripNumber}")
                ) {
                    Icon(Icons.Default.EventSeat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Choisir ma place", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
