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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ShipmentEntity
import com.example.data.local.ShipmentEventEntity
import com.example.data.model.DeliveryType
import com.example.data.model.ShipmentStatus
import com.example.ui.components.GuineeRouteMapCard
import com.example.ui.components.QrCodeView
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavySurface
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrackParcelScreen(
    viewModel: GuineeTransitViewModel,
    initialTrackingNumber: String = "GT-2026-00018452",
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf(initialTrackingNumber) }
    val trackedShipment by viewModel.activeTrackedShipment.collectAsState()
    val events by viewModel.activeShipmentEvents.collectAsState()
    val messages by viewModel.activeShipmentMessages.collectAsState()
    val isSandbox by viewModel.isSandboxMode.collectAsState()

    var showOtpDialog by remember { mutableStateOf(false) }
    var enteredOtp by remember { mutableStateOf("") }
    var recipientConfirmName by remember { mutableStateOf("") }

    var showChatDialog by remember { mutableStateOf(false) }
    var chatMessageInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Suivi de Colis National",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = DeepNavy
                    )
                    Text(
                        text = "Entrez votre numéro unique (ex: GT-2026-00018452)",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = { searchInput = it.uppercase() },
                            placeholder = { Text("Numéro GT-2026-XXXXX", fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tracking_page_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GuineaGreen,
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    viewModel.loadShipmentForTracking(searchInput)
                                }
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.loadShipmentForTracking(searchInput) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("tracking_page_search_btn")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = GuineaYellow)
                        }
                    }
                }
            }
        }

        // Active Tracked Shipment View
        if (trackedShipment == null) {
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
                        Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Aucun colis trouvé avec ce numéro", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Veuillez vérifier les chiffres ou tester avec GT-2026-00018452.", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }
            }
        } else {
            val shipment = trackedShipment!!

            // Sandbox Advance Bar (Prominent requirement 46)
            if (isSandbox) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(color = DeepNavy, shape = RoundedCornerShape(4.dp)) {
                                        Text("MODE DÉMONSTRATION", color = GuineaYellow, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Test du cycle de vie", fontSize = 11.sp, color = Color(0xFF78350F), fontWeight = FontWeight.Bold)
                                }
                                Text("Avancez le colis d'une étape pour voir la timeline réagir en direct.", fontSize = 11.sp, color = Color(0xFF92400E))
                            }

                            Button(
                                onClick = { viewModel.advanceActiveShipmentStatus() },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !shipment.currentStatus.isTerminal(),
                                modifier = Modifier.testTag("sandbox_advance_btn")
                            ) {
                                Icon(Icons.Default.FastForward, contentDescription = null, tint = GuineaYellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Avancer", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Summary Header Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = shipment.trackingNumber,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${shipment.parcelType.label} • ${shipment.weightKg} kg",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            StatusBadge(status = shipment.currentStatus)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Route Progress Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Départ", fontSize = 10.sp, color = Color(0xFF64748B))
                                Text(text = shipment.senderCity, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                                Text(text = maskName(shipment.senderName), fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (shipment.deliveryType == DeliveryType.HOME_DELIVERY) "Livraison Domicile" else "Retrait Agence",
                                    fontSize = 10.sp,
                                    color = GuineaGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(GuineaGreen)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Étape: ${shipment.currentCity}", fontSize = 10.sp, color = Color(0xFF64748B))
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Destination", fontSize = 10.sp, color = Color(0xFF64748B))
                                Text(text = shipment.recipientCity, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                                Text(text = maskName(shipment.recipientName), fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                        }

                        // OTP Secret notice & QR Code preview
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = GuineaGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Code OTP de retrait :", fontSize = 10.sp, color = Color(0xFF64748B))
                                        Text(
                                            text = if (shipment.currentStatus == ShipmentStatus.DELIVERED) "Validé ✓" else "Envoyé par SMS au destinataire",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Deliver action button for agents
                            if (shipment.currentStatus != ShipmentStatus.DELIVERED) {
                                Button(
                                    onClick = {
                                        enteredOtp = shipment.deliveryOtp // Pre-fill in demo for convenience
                                        recipientConfirmName = shipment.recipientName
                                        showOtpDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                                    modifier = Modifier.testTag("open_delivery_dialog_btn")
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Livrer (OTP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Guinean Route Map
            item {
                GuineeRouteMapCard(
                    originCity = shipment.senderCity,
                    currentCity = shipment.currentCity,
                    destinationCity = shipment.recipientCity
                )
            }

            // QR Code of Parcel
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "QR Code Colis Officiel", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                            Text(text = "Scanné à chaque agence et point relais", fontSize = 11.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Token interne : ${shipment.secretToken}",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        QrCodeView(payload = "GT-PARCEL:${shipment.trackingNumber}:${shipment.secretToken}", sizeDp = 90)
                    }
                }
            }

            // Timeline Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historique d'acheminement (${events.size} événements)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DeepNavy
                    )

                    // Support chat shortcut button
                    TextButton(
                        onClick = { showChatDialog = true },
                        modifier = Modifier.testTag("open_support_chat_btn")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = GuineaGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Assistance", color = GuineaGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Timeline Events
            items(events) { ev ->
                TimelineEventCard(event = ev)
            }

            // Proof of Delivery info if delivered
            if (shipment.currentStatus == ShipmentStatus.DELIVERED) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GuineaGreen, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Preuve de Livraison Certifiée", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GuineaGreen)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Remis en main propre à : ${shipment.deliveryProofRecipientName ?: shipment.recipientName}",
                                fontSize = 12.sp,
                                color = DeepNavy,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Code OTP vérifié avec succès. Reçu de livraison archivé dans le registre national.",
                                fontSize = 11.sp,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                }
            }
        }
    }

    // OTP Delivery Confirmation Dialog
    if (showOtpDialog && trackedShipment != null) {
        val s = trackedShipment!!
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = {
                Text("Validation de Livraison & OTP", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
            },
            text = {
                Column {
                    Text(
                        text = "Veuillez saisir le code OTP à 6 chiffres communiqué par le destinataire (${maskPhone(s.recipientPhone)}).",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { enteredOtp = it },
                        label = { Text("Code OTP secret") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("otp_input_field"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = recipientConfirmName,
                        onValueChange = { recipientConfirmName = it },
                        label = { Text("Nom de la personne qui réceptionne") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deliverShipmentWithOtp(
                            trackingNumber = s.trackingNumber,
                            otp = enteredOtp,
                            recipientName = recipientConfirmName
                        ) { success, _ ->
                            if (success) showOtpDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                    modifier = Modifier.testTag("confirm_delivery_otp_btn")
                ) {
                    Text("Valider la remise", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Support Chat Dialog
    if (showChatDialog && trackedShipment != null) {
        val s = trackedShipment!!
        AlertDialog(
            onDismissRequest = { showChatDialog = false },
            title = {
                Text("Conversation — ${s.trackingNumber}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { msg ->
                            val isClient = msg.senderRole == "CLIENT"
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isClient) Color(0xFFF1F5F9) else Color(0xFFE8F5E9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = msg.senderName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isClient) DeepNavy else GuineaGreen
                                    )
                                    Text(text = msg.message, fontSize = 12.sp, color = DeepNavy)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatMessageInput,
                            onValueChange = { chatMessageInput = it },
                            placeholder = { Text("Votre message...", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("support_chat_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                if (chatMessageInput.isNotBlank()) {
                                    viewModel.sendSupportChat(chatMessageInput)
                                    chatMessageInput = ""
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(GuineaGreen)
                                .testTag("send_support_chat_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Envoyer", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChatDialog = false }) {
                    Text("Fermer")
                }
            }
        )
    }
}

@Composable
fun TimelineEventCard(event: ShipmentEventEntity, modifier: Modifier = Modifier) {
    val dateStr = remember(event.timestamp) {
        val sdf = SimpleDateFormat("dd MMM — HH:mm", Locale.FRENCH)
        sdf.format(Date(event.timestamp))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = GuineaGreen,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.status.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DeepNavy
                    )
                    Text(
                        text = dateStr,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = event.description,
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Lieu: ${event.city} (${event.agencyName}) • ${event.operatorName}",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

private fun maskName(name: String): String {
    val parts = name.split(" ")
    return parts.joinToString(" ") { p ->
        if (p.length > 2) p.first() + "..." + p.last() else p
    }
}

private fun maskPhone(phone: String): String {
    return if (phone.length > 6) {
        phone.take(6) + " XX XX " + phone.takeLast(2)
    } else phone
}
