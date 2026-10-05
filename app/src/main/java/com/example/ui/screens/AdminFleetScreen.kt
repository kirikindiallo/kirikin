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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DriverEntity
import com.example.data.local.IncidentEntity
import com.example.data.local.VehicleEntity
import com.example.ui.components.GuineaFlagStripe
import com.example.ui.components.QrCodeView
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavySurface
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineaRed
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.GuineeTransitViewModel

@Composable
fun AdminFleetScreen(
    viewModel: GuineeTransitViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) } // 0: Dashboard KPIs, 1: Véhicules & Chauffeurs, 2: Manifeste, 3: Incidents
    val kpis by viewModel.dashboardKpis.collectAsState()
    val vehicles by viewModel.allVehicles.collectAsState()
    val drivers by viewModel.allDrivers.collectAsState()
    val incidents by viewModel.allIncidents.collectAsState()

    var showReportIncidentDialog by remember { mutableStateOf(false) }
    var incidentType by remember { mutableStateOf("Retard de route") }
    var incidentTarget by remember { mutableStateOf("TRIP-2026-00981") }
    var incidentDesc by remember { mutableStateOf("") }
    var incidentPriority by remember { mutableStateOf("MOYENNE") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    GuineaFlagStripe(height = 3)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tableau de Bord & Gestion Flotte",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "Supervision logistique nationale",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.resetDemoData() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DeepNavySurface)
                                .testTag("reset_demo_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Réinitialiser démo", tint = GuineaYellow)
                        }
                    }
                }
            }
        }

        // Sub-tabs
        item {
            TabRow(
                selectedTabIndex = selectedSection,
                containerColor = Color.White,
                contentColor = GuineaGreen,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedSection == 0,
                    onClick = { selectedSection = 0 },
                    text = { Text("KPIs", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 1,
                    onClick = { selectedSection = 1 },
                    text = { Text("Flotte", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 2,
                    onClick = { selectedSection = 2 },
                    text = { Text("Manifeste", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 3,
                    onClick = { selectedSection = 3 },
                    text = { Text("Incidents", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedSection) {
            0 -> {
                // Section 0: KPIs & Financial Summary
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            KpiMetricCard(
                                title = "Revenus Cumulés",
                                value = "${String.format("%,d", kpis.totalRevenueGnf)} GNF",
                                icon = Icons.Default.AttachMoney,
                                tint = GuineaGreen,
                                modifier = Modifier.weight(1f)
                            )
                            KpiMetricCard(
                                title = "Passagers Réservés",
                                value = "${kpis.totalBookings}",
                                icon = Icons.Default.People,
                                tint = Color(0xFF0369A1),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            KpiMetricCard(
                                title = "Colis en Transit",
                                value = "${kpis.inTransitShipments}",
                                icon = Icons.Default.LocalShipping,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.weight(1f)
                            )
                            KpiMetricCard(
                                title = "Colis Livrés",
                                value = "${kpis.deliveredShipments}",
                                icon = Icons.Default.CheckCircle,
                                tint = GuineaGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            KpiMetricCard(
                                title = "Total Colis Enregistrés",
                                value = "${kpis.totalShipments}",
                                icon = Icons.Default.LocalShipping,
                                tint = DeepNavy,
                                modifier = Modifier.weight(1f)
                            )
                            KpiMetricCard(
                                title = "Incidents Actifs",
                                value = "${kpis.activeIncidents}",
                                icon = Icons.Default.Warning,
                                tint = GuineaRed,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Sandbox shortcut in KPIs
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Outils de Démonstration & Tarification", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                            Text("Le moteur PricingService applique automatiquement les règles selon la distance et le poids.", fontSize = 11.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.resetDemoData() },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Recharger les données fictives de test", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            1 -> {
                // Section 1: Véhicules & Chauffeurs
                item {
                    Text("Véhicules de la Flotte (${vehicles.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                }

                items(vehicles) { vehicle ->
                    VehicleCard(vehicle = vehicle)
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Chauffeurs Partenaires (${drivers.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                }

                items(drivers) { driver ->
                    DriverCard(driver = driver)
                }
            }

            2 -> {
                // Section 2: Manifeste Numérique de Transport
                item {
                    ManifestCard()
                }
            }

            3 -> {
                // Section 3: Gestion des Incidents
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Registre des Incidents (${incidents.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy)
                        Button(
                            onClick = { showReportIncidentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GuineaRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Signaler", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                items(incidents) { incident ->
                    IncidentCard(
                        incident = incident,
                        onResolve = {
                            viewModel.resolveIncident(incident.incidentNumber, "Problème vérifié et résolu par l'administration centrale.")
                        }
                    )
                }
            }
        }
    }

    // Incident Report Dialog
    if (showReportIncidentDialog) {
        AlertDialog(
            onDismissRequest = { showReportIncidentDialog = false },
            title = { Text("Signaler un Incident Opérationnel", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepNavy) },
            text = {
                Column {
                    OutlinedTextField(
                        value = incidentTarget,
                        onValueChange = { incidentTarget = it },
                        label = { Text("N° Colis ou Trajet") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = incidentType,
                        onValueChange = { incidentType = it },
                        label = { Text("Type d'incident") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = incidentDesc,
                        onValueChange = { incidentDesc = it },
                        label = { Text("Description détaillée") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (incidentDesc.isNotBlank()) {
                            viewModel.reportIncident(
                                relatedTarget = incidentTarget,
                                type = incidentType,
                                description = incidentDesc,
                                priority = incidentPriority,
                                onSuccess = {
                                    showReportIncidentDialog = false
                                    incidentDesc = ""
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GuineaRed)
                ) {
                    Text("Enregistrer", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportIncidentDialog = false }) { Text("Annuler") }
            }
        )
    }
}

@Composable
fun KpiMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = DeepNavy)
        }
    }
}

@Composable
fun VehicleCard(vehicle: VehicleEntity) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = vehicle.plate, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE8F5E9)) {
                        Text(text = vehicle.model, fontSize = 10.sp, color = GuineaGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Chauffeur assigné : ${vehicle.driverName}", fontSize = 11.sp, color = Color(0xFF64748B))
                Text(text = "Capacité : ${vehicle.capacity} places • Maintenance : ${vehicle.nextMaintenance}", fontSize = 10.sp, color = Color(0xFF94A3B8))
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (vehicle.status) {
                    "Disponible" -> Color(0xFFDCFCE7)
                    "En trajet" -> Color(0xFFFEF3C7)
                    else -> Color(0xFFFEE2E2)
                }
            ) {
                Text(
                    text = vehicle.status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (vehicle.status) {
                        "Disponible" -> GuineaGreen
                        "En trajet" -> Color(0xFFB45309)
                        else -> GuineaRed
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun DriverCard(driver: DriverEntity) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                Text(text = "Permis : ${driver.licenseNumber} • Véhicule : ${driver.assignedVehiclePlate}", fontSize = 11.sp, color = Color(0xFF64748B))
                Text(text = "📞 ${driver.phone}", fontSize = 11.sp, color = GuineaGreen, fontWeight = FontWeight.SemiBold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GuineaYellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "${driver.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
                }
                Text(text = "${driver.completedTrips} voyages", fontSize = 10.sp, color = Color(0xFF94A3B8))
            }
        }
    }
}

@Composable
fun ManifestCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MANIFESTE DE TRANSPORT NUMÉRIQUE", fontWeight = FontWeight.Black, fontSize = 13.sp, color = DeepNavy)
                    Text("Réf : TRIP-2026-00981 (Conakry → Labé)", fontSize = 11.sp, color = Color(0xFF64748B))
                }
                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                    Text("CLÔTURÉ & SCELLÉ", color = GuineaGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Véhicule : RC-4521-A", fontSize = 11.sp, color = Color(0xFF475569))
                Text("Chauffeur : Mamadou Alpha", fontSize = 11.sp, color = Color(0xFF475569))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Colis embarqués : 12 colis", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
                Text("Poids total : 145.5 kg", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GuineaGreen)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("• GT-2026-00018452 (Ordinateur - 3.5kg)", fontSize = 11.sp, color = Color(0xFF334155))
                    Text("• GT-2026-00018453 (Textile - 8.0kg)", fontSize = 11.sp, color = Color(0xFF334155))
                    Text("• GT-2026-00018455 (Dossiers - 0.8kg)", fontSize = 11.sp, color = Color(0xFF334155))
                    Text("• + 9 autres colis scannés au départ", fontSize = 10.sp, color = Color(0xFF94A3B8))
                }

                QrCodeView(payload = "GT-MANIFEST:TRIP-2026-00981:RC-4521-A", sizeDp = 75)
            }
        }
    }
}

@Composable
fun IncidentCard(incident: IncidentEntity, onResolve: () -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = if (incident.status == "RESOLVED") GuineaGreen else GuineaRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = incident.incidentNumber, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (incident.status == "RESOLVED") Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = incident.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (incident.status == "RESOLVED") GuineaGreen else GuineaRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "${incident.type} (${incident.relatedTrackingOrTrip})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = DeepNavy)
            Text(text = incident.description, fontSize = 11.sp, color = Color(0xFF475569))

            if (incident.resolutionNotes != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Résolution : ${incident.resolutionNotes}", fontSize = 10.sp, color = GuineaGreen, fontWeight = FontWeight.Medium)
            }

            if (incident.status != "RESOLVED") {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onResolve,
                    colors = ButtonDefaults.buttonColors(containerColor = GuineaGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Marquer comme résolu", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}
