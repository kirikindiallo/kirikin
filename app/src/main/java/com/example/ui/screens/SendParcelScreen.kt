package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.example.data.local.ShipmentEntity
import com.example.data.model.DeliveryType
import com.example.data.model.GuineanCities
import com.example.data.model.PaymentMethod
import com.example.data.model.ShipmentType
import com.example.ui.components.GuineaFlagStripe
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendParcelScreen(
    viewModel: GuineeTransitViewModel,
    onShipmentCreated: (ShipmentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(1) } // 1: Expéditeur, 2: Destinataire, 3: Colis, 4: Tarification & Paiement

    // Step 1: Sender
    var senderName by remember { mutableStateOf("Mamadou Bah") }
    var senderPhone by remember { mutableStateOf("+224 622 10 20 30") }
    var senderCity by remember { mutableStateOf("Conakry") }
    var senderAddress by remember { mutableStateOf("Matam Lido, Conakry") }
    var senderCityExpanded by remember { mutableStateOf(false) }

    // Step 2: Recipient
    var recipientName by remember { mutableStateOf("Aissatou Diallo") }
    var recipientPhone by remember { mutableStateOf("+224 628 44 55 66") }
    var recipientCity by remember { mutableStateOf("Labé") }
    var recipientAddress by remember { mutableStateOf("Daka 1, Labé") }
    var deliveryType by remember { mutableStateOf(DeliveryType.AGENCY_PICKUP) }
    var recipientCityExpanded by remember { mutableStateOf(false) }

    // Step 3: Parcel Details
    var parcelType by remember { mutableStateOf(ShipmentType.PETIT_COLIS) }
    var parcelTypeExpanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("Colis vêtements et accessoires") }
    var weightText by remember { mutableStateOf("3.0") }
    var declaredValueText by remember { mutableStateOf("500000") }
    var isFragile by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(false) }
    var hasInsurance by remember { mutableStateOf(true) }

    // Step 4: Payment
    var paymentMethod by remember { mutableStateOf(PaymentMethod.ORANGE_MONEY) }
    var formError by remember { mutableStateOf<String?>(null) }

    // Real-time Pricing calculation from domain service
    val pricingBreakdown = remember(
        senderCity, recipientCity, parcelType, weightText, deliveryType, isUrgent, isFragile, declaredValueText, hasInsurance
    ) {
        val weight = weightText.toDoubleOrNull() ?: 1.0
        val value = declaredValueText.toLongOrNull() ?: 0L
        viewModel.pricingService.calculateParcelPrice(
            originCity = senderCity,
            destinationCity = recipientCity,
            parcelType = parcelType,
            weightKg = weight,
            deliveryType = deliveryType,
            isUrgent = isUrgent,
            isFragile = isFragile,
            declaredValueGnf = value,
            hasInsurance = hasInsurance
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Step Indicator Progress
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                GuineaFlagStripe(height = 3)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Expédier un Colis en Guinée",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GuineaYellow
                    ) {
                        Text(
                            text = "Étape $step / 4",
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Step Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepDot(stepIndex = 1, currentStep = step, label = "Expéditeur")
                    StepDivider(isActive = step >= 2)
                    StepDot(stepIndex = 2, currentStep = step, label = "Destinataire")
                    StepDivider(isActive = step >= 3)
                    StepDot(stepIndex = 3, currentStep = step, label = "Colis")
                    StepDivider(isActive = step >= 4)
                    StepDot(stepIndex = 4, currentStep = step, label = "Paiement")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Content Cards
        when (step) {
            1 -> {
                // STEP 1: Expéditeur
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GuineaGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Étape 1 : Expéditeur", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = senderName,
                            onValueChange = { senderName = it },
                            label = { Text("Prénom & Nom") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sender_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = senderPhone,
                            onValueChange = { senderPhone = it },
                            label = { Text("Téléphone") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sender_phone_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sender City Selector
                        ExposedDropdownMenuBox(
                            expanded = senderCityExpanded,
                            onExpandedChange = { senderCityExpanded = !senderCityExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = senderCity,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Ville d'expédition") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = senderCityExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = senderCityExpanded,
                                onDismissRequest = { senderCityExpanded = false }
                            ) {
                                GuineanCities.ALL.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.name) },
                                        onClick = {
                                            senderCity = city.name
                                            senderCityExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = senderAddress,
                            onValueChange = { senderAddress = it },
                            label = { Text("Adresse / Quartier ou Agence de dépôt") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            2 -> {
                // STEP 2: Destinataire
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = GuineaRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Étape 2 : Destinataire", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text("Prénom & Nom du destinataire") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("recipient_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = recipientPhone,
                            onValueChange = { recipientPhone = it },
                            label = { Text("Téléphone du destinataire (Reçoit l'OTP)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("recipient_phone_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        ExposedDropdownMenuBox(
                            expanded = recipientCityExpanded,
                            onExpandedChange = { recipientCityExpanded = !recipientCityExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = recipientCity,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Ville de destination") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = recipientCityExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = recipientCityExpanded,
                                onDismissRequest = { recipientCityExpanded = false }
                            ) {
                                GuineanCities.ALL.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.name) },
                                        onClick = {
                                            recipientCity = city.name
                                            recipientCityExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Mode de récupération :", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                        Spacer(modifier = Modifier.height(6.dp))

                        DeliveryType.values().forEach { dType ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { deliveryType = dType }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (deliveryType == dType),
                                    onClick = { deliveryType = dType },
                                    colors = RadioButtonDefaults.colors(selectedColor = GuineaGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = dType.label,
                                    fontSize = 13.sp,
                                    color = DeepNavy,
                                    fontWeight = if (deliveryType == dType) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = recipientAddress,
                            onValueChange = { recipientAddress = it },
                            label = { Text(if (deliveryType == DeliveryType.HOME_DELIVERY) "Adresse exacte / Quartier / Repère" else "Agence de retrait souhaitée") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            3 -> {
                // STEP 3: Colis
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = GuineaYellow)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Étape 3 : Détails du Colis", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        ExposedDropdownMenuBox(
                            expanded = parcelTypeExpanded,
                            onExpandedChange = { parcelTypeExpanded = !parcelTypeExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = parcelType.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Catégorie de colis") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = parcelTypeExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = parcelTypeExpanded,
                                onDismissRequest = { parcelTypeExpanded = false }
                            ) {
                                ShipmentType.values().forEach { st ->
                                    DropdownMenuItem(
                                        text = { Text(st.label) },
                                        onClick = {
                                            parcelType = st
                                            parcelTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description du contenu") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = weightText,
                                onValueChange = { weightText = it },
                                label = { Text("Poids (kg)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = declaredValueText,
                                onValueChange = { declaredValueText = it },
                                label = { Text("Valeur (GNF)") },
                                modifier = Modifier.weight(1.4f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Options de transport :", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isFragile = !isFragile }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isFragile,
                                onCheckedChange = { isFragile = it },
                                colors = CheckboxDefaults.colors(checkedColor = GuineaGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Colis fragile (+15 000 GNF)", fontSize = 13.sp, color = DeepNavy)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isUrgent = !isUrgent }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isUrgent,
                                onCheckedChange = { isUrgent = it },
                                colors = CheckboxDefaults.colors(checkedColor = GuineaGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Envoi express / urgent (+20 000 GNF)", fontSize = 13.sp, color = DeepNavy)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { hasInsurance = !hasInsurance }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = hasInsurance,
                                onCheckedChange = { hasInsurance = it },
                                colors = CheckboxDefaults.colors(checkedColor = GuineaGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Assurance perte/avarie (1.5% valeur déclarée)", fontSize = 13.sp, color = DeepNavy)
                        }
                    }
                }
            }

            4 -> {
                // STEP 4: Tarification & Paiement
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = GuineaGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Étape 4 : Décomposition du Tarif & Paiement", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Route summary
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "$senderCity → $recipientCity",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${parcelType.label} • ${weightText} kg • ${deliveryType.label}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Transparent Pricing Breakdown
                        PricingRow(label = "Tarif de base (${parcelType.label.take(15)}) :", amount = pricingBreakdown.basePriceGnf)
                        PricingRow(label = "Frais distance interurbaine :", amount = pricingBreakdown.distanceSurchargeGnf)
                        PricingRow(label = "Supplément poids (>2kg) :", amount = pricingBreakdown.weightSurchargeGnf)
                        if (pricingBreakdown.homeDeliverySurchargeGnf > 0) {
                            PricingRow(label = "Livraison à domicile :", amount = pricingBreakdown.homeDeliverySurchargeGnf)
                        }
                        if (pricingBreakdown.specialServicesSurchargeGnf > 0) {
                            PricingRow(label = "Options (Fragile / Express) :", amount = pricingBreakdown.specialServicesSurchargeGnf)
                        }
                        if (pricingBreakdown.insuranceSurchargeGnf > 0) {
                            PricingRow(label = "Assurance valeur déclarée :", amount = pricingBreakdown.insuranceSurchargeGnf)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE2E8F0)))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TOTAL À PAYER :", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                            Text(
                                text = "${String.format("%,d", pricingBreakdown.totalGnf)} GNF",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = GuineaGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Sélectionnez votre mode de règlement :", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethod.values().forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = method }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (paymentMethod == method),
                                    onClick = { paymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = GuineaGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = method.label,
                                    fontSize = 13.sp,
                                    color = DeepNavy,
                                    fontWeight = if (paymentMethod == method) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        if (formError != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEE2E2),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formError!!,
                    color = GuineaRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Buttons (Précédent / Continuer)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = {
                        step--
                        formError = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Précédent", fontSize = 13.sp)
                }
            }

            Button(
                onClick = {
                    formError = null
                    when (step) {
                        1 -> {
                            if (senderName.isBlank()) {
                                formError = "Veuillez renseigner le nom de l'expéditeur."
                                return@Button
                            }
                            if (senderPhone.length < 8) {
                                formError = "Veuillez renseigner le numéro de téléphone."
                                return@Button
                            }
                            step = 2
                        }
                        2 -> {
                            if (recipientName.isBlank()) {
                                formError = "Veuillez renseigner le nom du destinataire."
                                return@Button
                            }
                            if (recipientPhone.length < 8) {
                                formError = "Veuillez renseigner le téléphone du destinataire."
                                return@Button
                            }
                            step = 3
                        }
                        3 -> {
                            val w = weightText.toDoubleOrNull()
                            if (w == null || w <= 0) {
                                formError = "Veuillez renseigner un poids valide en kg."
                                return@Button
                            }
                            step = 4
                        }
                        4 -> {
                            // Finalize & Create Shipment
                            viewModel.createNewShipment(
                                senderName = senderName.trim(),
                                senderPhone = senderPhone.trim(),
                                senderCity = senderCity,
                                senderAddress = senderAddress.trim(),
                                recipientName = recipientName.trim(),
                                recipientPhone = recipientPhone.trim(),
                                recipientCity = recipientCity,
                                recipientAddress = recipientAddress.trim(),
                                deliveryType = deliveryType,
                                parcelType = parcelType,
                                description = description.trim(),
                                weightKg = weightText.toDoubleOrNull() ?: 2.0,
                                declaredValueGnf = declaredValueText.toLongOrNull() ?: 0L,
                                isFragile = isFragile,
                                isUrgent = isUrgent,
                                hasInsurance = hasInsurance,
                                pricing = pricingBreakdown,
                                paymentMethod = paymentMethod,
                                onSuccess = { created ->
                                    onShipmentCreated(created)
                                }
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (step == 4) GuineaGreen else DeepNavy),
                modifier = Modifier
                    .weight(if (step > 1) 1.5f else 1f)
                    .height(50.dp)
                    .testTag("send_parcel_next_btn")
            ) {
                Text(
                    text = if (step == 4) "Confirmer & Obtenir N° de Suivi" else "Continuer",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun StepDot(stepIndex: Int, currentStep: Int, label: String) {
    val isCompleted = currentStep > stepIndex
    val isCurrent = currentStep == stepIndex

    val bgColor = when {
        isCompleted -> GuineaGreen
        isCurrent -> GuineaYellow
        else -> Color(0xFF334155)
    }

    val textColor = when {
        isCompleted -> Color.White
        isCurrent -> DeepNavy
        else -> Color(0xFF94A3B8)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else {
                Text(text = "$stepIndex", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 9.sp, color = if (isCurrent) Color.White else Color(0xFF94A3B8))
    }
}

@Composable
fun StepDivider(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(28.dp)
            .height(2.dp)
            .background(if (isActive) GuineaGreen else Color(0xFF334155))
    )
}

@Composable
fun PricingRow(label: String, amount: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(text = "${String.format("%,d", amount)} GNF", fontSize = 12.sp, color = DeepNavy, fontWeight = FontWeight.Medium)
    }
}
