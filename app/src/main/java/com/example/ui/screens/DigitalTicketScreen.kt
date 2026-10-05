package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookingEntity
import com.example.ui.components.GuineaFlagStripe
import com.example.ui.components.QrCodeView
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@Composable
fun DigitalTicketScreen(
    booking: BookingEntity,
    viewModel: GuineeTransitViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val currentBooking = allBookings.find { it.bookingNumber == booking.bookingNumber } ?: booking
    var scanResultModal by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Text(
                text = "Billet Numérique d'Embarquement",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Printable / Boarding Pass Style Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                GuineaFlagStripe(height = 5)

                // Ticket Top Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DeepNavy)
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "GUINÉE TRANSIT", color = GuineaYellow, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(text = currentBooking.companyName, color = Color.White, fontSize = 12.sp)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (currentBooking.status) {
                                "VALID" -> GuineaGreen
                                "BOARDED" -> Color(0xFFD97706)
                                else -> GuineaRed
                            }
                        ) {
                            Text(
                                text = when (currentBooking.status) {
                                    "VALID" -> "VALIDE"
                                    "BOARDED" -> "DÉJÀ EMBARQUÉ"
                                    else -> currentBooking.status
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Route Big Banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "DÉPART", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Text(text = currentBooking.originCity, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(text = currentBooking.departureTime, color = GuineaYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "➔", color = Color(0xFF94A3B8), fontSize = 20.sp)
                            Text(text = currentBooking.departureDate, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "DESTINATION", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Text(text = currentBooking.destinationCity, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(text = "Arrivée garantie", color = GuineaGreen, fontSize = 11.sp)
                        }
                    }
                }

                // Perforated Divider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color(0xFFE2E8F0))
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    )
                }

                // Middle Details: Passenger & Seat
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "PASSAGER", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = currentBooking.passengerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                            Text(text = currentBooking.passengerPhone, fontSize = 12.sp, color = Color(0xFF64748B))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "SIÈGE RÉSERVÉ", fontSize = 10.sp, color = Color(0xFF64748B))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFE8F5E9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GuineaGreen)
                            ) {
                                Text(
                                    text = "N° ${currentBooking.seatNumber}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = GuineaGreen,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "N° RÉSERVATION", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = currentBooking.bookingNumber, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DeepNavy)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "VÉHICULE", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = currentBooking.vehiclePlate, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DeepNavy)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "PRIX DU BILLET", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(
                                text = "${String.format("%,d", currentBooking.amountGnf)} GNF",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = GuineaGreen
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "PAIEMENT", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = currentBooking.paymentMethod.label.take(15), fontSize = 12.sp, color = Color(0xFF475569))
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // QR Code Canvas Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Présentez ce QR Code lors de l'embarquement",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        QrCodeView(
                            payload = "GT-TICKET:${currentBooking.bookingNumber}:${currentBooking.seatNumber}:${currentBooking.tripNumber}",
                            sizeDp = 150
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentBooking.bookingNumber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Actions: Download & Add to Tickets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { /* Simulated download */ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Télécharger", fontSize = 12.sp)
            }

            Button(
                onClick = { onBack() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                modifier = Modifier.weight(1f)
            ) {
                Text("Mes Billets", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Station Agent Simulator: Scan and Verify Boarding
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Contrôle Agent à la Gare Routière",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DeepNavy
                )
                Text(
                    text = "Simulation du scan par le contrôleur au départ du bus",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.verifyTicket(currentBooking.bookingNumber) { success, message ->
                            scanResultModal = Pair(success, message)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (currentBooking.status == "BOARDED") Color(0xFF64748B) else GuineaGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("scan_ticket_agent_btn")
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentBooking.status == "BOARDED") "Billet déjà validé" else "Scanner & Valider l'embarquement",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (scanResultModal != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (scanResultModal!!.first) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (scanResultModal!!.first) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (scanResultModal!!.first) GuineaGreen else GuineaRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = scanResultModal!!.second,
                                fontSize = 12.sp,
                                color = if (scanResultModal!!.first) GuineaGreen else GuineaRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
