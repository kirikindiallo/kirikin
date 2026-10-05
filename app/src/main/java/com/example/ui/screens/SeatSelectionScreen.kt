package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.data.local.BookingEntity
import com.example.data.local.TripEntity
import com.example.data.model.PaymentMethod
import com.example.data.model.SeatStatus
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeatSelectionScreen(
    trip: TripEntity,
    viewModel: GuineeTransitViewModel,
    onBack: () -> Unit,
    onBookingSuccess: (BookingEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSeat by viewModel.selectedSeatNumber.collectAsState()
    val bookedSeatsList = remember(trip.bookedSeatsCsv) {
        if (trip.bookedSeatsCsv.isBlank()) emptySet()
        else trip.bookedSeatsCsv.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
    }

    var passengerName by remember { mutableStateOf("") }
    var passengerPhone by remember { mutableStateOf("+224 ") }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.ORANGE_MONEY) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = DeepNavy)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Choisir votre place",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Text(
                    text = "${trip.originCity} → ${trip.destinationCity} (${trip.departureTime})",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vehicle Layout Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Plan du véhicule — ${trip.vehicleType}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepNavy
                )
                Text(
                    text = "Immatriculation : ${trip.vehiclePlate}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Driver & Steering Front Cockpit Indicator
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🚪 Porte passagers", fontSize = 11.sp, color = Color(0xFF64748B))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Chauffeur", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(DeepNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎮", fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Seats Grid
                val totalSeats = trip.totalSeats.coerceIn(4, 45)
                val seatCols = 4

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var seatCounter = 1
                    while (seatCounter <= totalSeats) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left side (2 seats)
                            for (c in 0 until 2) {
                                if (seatCounter <= totalSeats) {
                                    val currentSeatNum = seatCounter
                                    val isOccupied = bookedSeatsList.contains(currentSeatNum)
                                    val isSelected = selectedSeat == currentSeatNum

                                    SeatItem(
                                        seatNumber = currentSeatNum,
                                        isOccupied = isOccupied,
                                        isSelected = isSelected,
                                        onClick = {
                                            if (!isOccupied) {
                                                viewModel.selectSeat(currentSeatNum)
                                                errorMessage = null
                                            }
                                        }
                                    )
                                    seatCounter++
                                }
                            }

                            // Corridor space
                            Spacer(modifier = Modifier.width(18.dp))

                            // Right side (2 seats)
                            for (c in 0 until 2) {
                                if (seatCounter <= totalSeats) {
                                    val currentSeatNum = seatCounter
                                    val isOccupied = bookedSeatsList.contains(currentSeatNum)
                                    val isSelected = selectedSeat == currentSeatNum

                                    SeatItem(
                                        seatNumber = currentSeatNum,
                                        isOccupied = isOccupied,
                                        isSelected = isSelected,
                                        onClick = {
                                            if (!isOccupied) {
                                                viewModel.selectSeat(currentSeatNum)
                                                errorMessage = null
                                            }
                                        }
                                    )
                                    seatCounter++
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(color = Color(0xFFF1F5F9), borderColor = Color(0xFFCBD5E1), label = "Disponible")
                    LegendItem(color = GuineaGreen, borderColor = GuineaGreen, label = "Sélectionné")
                    LegendItem(color = Color(0xFFE2E8F0), borderColor = Color(0xFF94A3B8), label = "Occupé")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Passenger Details Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Informations Passager",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passengerName,
                    onValueChange = { passengerName = it },
                    label = { Text("Nom et Prénom du voyageur") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("passenger_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = passengerPhone,
                    onValueChange = { passengerPhone = it },
                    label = { Text("Téléphone (+224 XX XX XX XX)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("passenger_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Mode de Paiement",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(8.dp))

                PaymentMethod.values().forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = method }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedPaymentMethod == method),
                            onClick = { selectedPaymentMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = GuineaGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = method.label,
                            fontSize = 13.sp,
                            color = DeepNavy,
                            fontWeight = if (selectedPaymentMethod == method) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Summary & Confirmation Button
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DeepNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Place sélectionnée :", color = Color(0xFFCBD5E1), fontSize = 13.sp)
                    Text(
                        text = if (selectedSeat != null) "Siège N°$selectedSeat" else "Aucune place choisie",
                        color = if (selectedSeat != null) GuineaYellow else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Frais de gare routière :", color = Color(0xFFCBD5E1), fontSize = 13.sp)
                    Text(text = "Inclus (0 GNF)", color = Color.White, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF334155))
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Total à payer :", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        text = "${String.format("%,d", trip.priceGnf)} GNF",
                        color = GuineaGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage!!, color = Color(0xFFFCA5A5), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedSeat == null) {
                            errorMessage = "Veuillez d'abord sélectionner un siège sur le plan."
                            return@Button
                        }
                        if (passengerName.isBlank()) {
                            errorMessage = "Veuillez renseigner le nom complet du passager."
                            return@Button
                        }
                        if (passengerPhone.length < 9) {
                            errorMessage = "Veuillez renseigner un numéro de téléphone valide."
                            return@Button
                        }

                        viewModel.bookSelectedSeat(
                            passengerName = passengerName.trim(),
                            passengerPhone = passengerPhone.trim(),
                            paymentMethod = selectedPaymentMethod,
                            onSuccess = { booking ->
                                onBookingSuccess(booking)
                            }
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_booking_btn")
                ) {
                    Text("Valider la réservation & Générer le Billet QR", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun SeatItem(
    seatNumber: Int,
    isOccupied: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> GuineaGreen
        isOccupied -> Color(0xFFE2E8F0)
        else -> Color(0xFFF8FAFC)
    }

    val textColor = when {
        isSelected -> Color.White
        isOccupied -> Color(0xFF94A3B8)
        else -> DeepNavy
    }

    val borderColor = when {
        isSelected -> GuineaGreen
        isOccupied -> Color(0xFFCBD5E1)
        else -> Color(0xFFCBD5E1)
    }

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = !isOccupied) { onClick() }
            .testTag("seat_$seatNumber"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.EventSeat,
                contentDescription = "Siège $seatNumber",
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "$seatNumber",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, borderColor: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF475569))
    }
}
