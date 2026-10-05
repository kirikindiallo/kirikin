package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.BookingEntity
import com.example.data.local.TripEntity
import com.example.ui.components.GuineeTransitTopAppBar
import com.example.ui.screens.AdminFleetScreen
import com.example.ui.screens.AgenciesScreen
import com.example.ui.screens.DigitalTicketScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SeatSelectionScreen
import com.example.ui.screens.SendParcelScreen
import com.example.ui.screens.TrackParcelScreen
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GuineaGreen
import com.example.ui.theme.GuineeTransitTheme
import com.example.ui.theme.GuineaYellow
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.GuineeTransitViewModel

sealed class SubScreen {
    object None : SubScreen()
    data class SeatSelection(val trip: TripEntity) : SubScreen()
    data class DigitalTicket(val booking: BookingEntity) : SubScreen()
    object SendParcel : SubScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuineeTransitTheme {
                GuineeTransitApp()
            }
        }
    }
}

@Composable
fun GuineeTransitApp(viewModel: GuineeTransitViewModel = viewModel()) {
    val currentRole by viewModel.currentRole.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val isSandbox by viewModel.isSandboxMode.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var activeSubScreen by remember { mutableStateOf<SubScreen>(SubScreen.None) }
    var trackingSearchTarget by remember { mutableStateOf("GT-2026-00018452") }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Handle back button for sub-screens
    BackHandler(enabled = activeSubScreen !is SubScreen.None) {
        activeSubScreen = SubScreen.None
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GuineeTransitTopAppBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.setRole(it) },
                isSandbox = isSandbox,
                onToggleSandbox = { viewModel.toggleSandboxMode() }
            )
        },
        bottomBar = {
            if (activeSubScreen is SubScreen.None) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppNavTab.HOME,
                        onClick = { viewModel.setTab(AppNavTab.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                        label = { Text("Accueil", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.HOME) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = DeepNavy,
                            indicatorColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.TRIPS,
                        onClick = { viewModel.setTab(AppNavTab.TRIPS) },
                        icon = { Icon(Icons.Default.DirectionsBus, contentDescription = "Voyages") },
                        label = { Text("Voyages", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.TRIPS) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = DeepNavy,
                            indicatorColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.testTag("nav_item_trips")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.SHIPMENTS,
                        onClick = { viewModel.setTab(AppNavTab.SHIPMENTS) },
                        icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Colis") },
                        label = { Text("Colis", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.SHIPMENTS) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = DeepNavy,
                            indicatorColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.testTag("nav_item_shipments")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.AGENCIES,
                        onClick = { viewModel.setTab(AppNavTab.AGENCIES) },
                        icon = { Icon(Icons.Default.Business, contentDescription = "Agences") },
                        label = { Text("Agences", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.AGENCIES) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = DeepNavy,
                            indicatorColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.testTag("nav_item_agencies")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.ADMIN,
                        onClick = { viewModel.setTab(AppNavTab.ADMIN) },
                        icon = { Icon(Icons.Default.SupervisorAccount, contentDescription = "Espace Pro") },
                        label = { Text("Espace Pro", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.ADMIN) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = DeepNavy,
                            indicatorColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.testTag("nav_item_admin")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val sub = activeSubScreen) {
                is SubScreen.SeatSelection -> {
                    SeatSelectionScreen(
                        trip = sub.trip,
                        viewModel = viewModel,
                        onBack = { activeSubScreen = SubScreen.None },
                        onBookingSuccess = { booking ->
                            activeSubScreen = SubScreen.DigitalTicket(booking)
                        }
                    )
                }

                is SubScreen.DigitalTicket -> {
                    DigitalTicketScreen(
                        booking = sub.booking,
                        viewModel = viewModel,
                        onBack = { activeSubScreen = SubScreen.None }
                    )
                }

                is SubScreen.SendParcel -> {
                    SendParcelScreen(
                        viewModel = viewModel,
                        onShipmentCreated = { shipment ->
                            trackingSearchTarget = shipment.trackingNumber
                            activeSubScreen = SubScreen.None
                            viewModel.setTab(AppNavTab.SHIPMENTS)
                        }
                    )
                }

                SubScreen.None -> {
                    when (currentTab) {
                        AppNavTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToTrips = { viewModel.setTab(AppNavTab.TRIPS) },
                                onNavigateToSendParcel = { activeSubScreen = SubScreen.SendParcel },
                                onNavigateToTracking = { trackingNum ->
                                    trackingSearchTarget = trackingNum
                                    viewModel.setTab(AppNavTab.SHIPMENTS)
                                },
                                onNavigateToAgencies = { viewModel.setTab(AppNavTab.AGENCIES) }
                            )
                        }

                        AppNavTab.TRIPS -> {
                            com.example.ui.screens.TripsScreen(
                                viewModel = viewModel,
                                onTripSelected = { trip ->
                                    activeSubScreen = SubScreen.SeatSelection(trip)
                                }
                            )
                        }

                        AppNavTab.SHIPMENTS -> {
                            TrackParcelScreen(
                                viewModel = viewModel,
                                initialTrackingNumber = trackingSearchTarget
                            )
                        }

                        AppNavTab.AGENCIES -> {
                            AgenciesScreen(viewModel = viewModel)
                        }

                        AppNavTab.ADMIN -> {
                            AdminFleetScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
