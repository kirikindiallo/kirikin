package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AgencyEntity
import com.example.data.local.AppDatabase
import com.example.data.local.BookingEntity
import com.example.data.local.DriverEntity
import com.example.data.local.IncidentEntity
import com.example.data.local.ShipmentEntity
import com.example.data.local.ShipmentEventEntity
import com.example.data.local.SupportMessageEntity
import com.example.data.local.TripEntity
import com.example.data.local.VehicleEntity
import com.example.data.model.DeliveryType
import com.example.data.model.PaymentMethod
import com.example.data.model.ShipmentStatus
import com.example.data.model.ShipmentType
import com.example.data.model.UserRole
import com.example.data.repository.GuineeTransitRepository
import com.example.domain.payment.PaymentGatewayRegistry
import com.example.domain.payment.PaymentRequest
import com.example.domain.pricing.PricingBreakdown
import com.example.domain.pricing.PricingConfig
import com.example.domain.pricing.PricingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String) {
    HOME("Accueil"),
    TRIPS("Voyages"),
    SHIPMENTS("Colis"),
    AGENCIES("Agences"),
    ADMIN("Espace Pro")
}

data class DashboardKpis(
    val totalShipments: Int = 0,
    val inTransitShipments: Int = 0,
    val deliveredShipments: Int = 0,
    val totalBookings: Int = 0,
    val activeIncidents: Int = 0,
    val totalRevenueGnf: Long = 0L
)

class GuineeTransitViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = GuineeTransitRepository(db.dao(), viewModelScope)
    val pricingService = PricingService()

    // --- Active User Role & Navigation ---
    private val _currentRole = MutableStateFlow(UserRole.CLIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // --- Sandbox Mode State ---
    private val _isSandboxMode = MutableStateFlow(true)
    val isSandboxMode: StateFlow<Boolean> = _isSandboxMode.asStateFlow()

    fun toggleSandboxMode() {
        _isSandboxMode.value = !_isSandboxMode.value
    }

    // --- Search & Trips State ---
    val allTrips: StateFlow<List<TripEntity>> = repository.allTrips
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val originFilter = MutableStateFlow("Conakry")
    val destinationFilter = MutableStateFlow("Labé")
    val selectedDate = MutableStateFlow("2026-10-02")
    val passengersCount = MutableStateFlow(1)

    private val _selectedTripForBooking = MutableStateFlow<TripEntity?>(null)
    val selectedTripForBooking: StateFlow<TripEntity?> = _selectedTripForBooking.asStateFlow()

    private val _selectedSeatNumber = MutableStateFlow<Int?>(null)
    val selectedSeatNumber: StateFlow<Int?> = _selectedSeatNumber.asStateFlow()

    private val _lastConfirmedBooking = MutableStateFlow<BookingEntity?>(null)
    val lastConfirmedBooking: StateFlow<BookingEntity?> = _lastConfirmedBooking.asStateFlow()

    fun selectTrip(trip: TripEntity?) {
        _selectedTripForBooking.value = trip
        _selectedSeatNumber.value = null
    }

    fun selectSeat(seat: Int) {
        _selectedSeatNumber.value = seat
    }

    // --- Shipments State ---
    val allShipments: StateFlow<List<ShipmentEntity>> = repository.allShipments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchTrackingInput = MutableStateFlow("GT-2026-00018452")

    private val _activeTrackedShipment = MutableStateFlow<ShipmentEntity?>(null)
    val activeTrackedShipment: StateFlow<ShipmentEntity?> = _activeTrackedShipment.asStateFlow()

    private val _activeShipmentEvents = MutableStateFlow<List<ShipmentEventEntity>>(emptyList())
    val activeShipmentEvents: StateFlow<List<ShipmentEventEntity>> = _activeShipmentEvents.asStateFlow()

    private val _activeShipmentMessages = MutableStateFlow<List<SupportMessageEntity>>(emptyList())
    val activeShipmentMessages: StateFlow<List<SupportMessageEntity>> = _activeShipmentMessages.asStateFlow()

    // --- Agencies, Vehicles, Drivers, Incidents ---
    val allAgencies: StateFlow<List<AgencyEntity>> = repository.allAgencies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVehicles: StateFlow<List<VehicleEntity>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDrivers: StateFlow<List<DriverEntity>> = repository.allDrivers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allIncidents: StateFlow<List<IncidentEntity>> = repository.allIncidents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Feedback Toast / SnackBar ---
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun clearToast() {
        _toastMessage.value = null
    }

    // --- Dashboard KPIs ---
    val dashboardKpis: StateFlow<DashboardKpis> = combine(
        combine(repository.totalShipmentsCount, repository.inTransitShipmentsCount, repository.deliveredShipmentsCount) { s, t, d -> Triple(s, t, d) },
        combine(repository.totalBookingsCount, repository.activeIncidentsCount) { b, i -> Pair(b, i) },
        combine(repository.shipmentRevenueSum, repository.bookingRevenueSum) { sr, br -> Pair(sr, br) }
    ) { (s, t, d), (b, i), (sr, br) ->
        val totalRev = (sr ?: 0L) + (br ?: 0L)
        DashboardKpis(
            totalShipments = s,
            inTransitShipments = t,
            deliveredShipments = d,
            totalBookings = b,
            activeIncidents = i,
            totalRevenueGnf = totalRev
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardKpis())

    init {
        // Track the first shipment by default so tracking page has rich content
        viewModelScope.launch {
            loadShipmentForTracking("GT-2026-00018452")
        }
    }

    fun loadShipmentForTracking(trackingNumber: String) {
        searchTrackingInput.value = trackingNumber
        viewModelScope.launch {
            repository.getShipment(trackingNumber.trim()).collect { shipment ->
                _activeTrackedShipment.value = shipment
            }
        }
        viewModelScope.launch {
            repository.getShipmentEvents(trackingNumber.trim()).collect { events ->
                _activeShipmentEvents.value = events
            }
        }
        viewModelScope.launch {
            repository.getMessagesForShipment(trackingNumber.trim()).collect { messages ->
                _activeShipmentMessages.value = messages
            }
        }
    }

    fun bookSelectedSeat(
        passengerName: String,
        passengerPhone: String,
        paymentMethod: PaymentMethod,
        onSuccess: (BookingEntity) -> Unit
    ) {
        val trip = _selectedTripForBooking.value ?: return
        val seat = _selectedSeatNumber.value ?: return

        viewModelScope.launch {
            // Initiate via Payment Provider Adapter
            val provider = PaymentGatewayRegistry.getProvider(paymentMethod)
            val paymentRes = provider.initiatePayment(
                PaymentRequest(
                    reference = "TRIP-${trip.tripNumber}-$seat",
                    amountGnf = trip.priceGnf,
                    customerPhone = passengerPhone,
                    customerName = passengerName,
                    description = "Billet ${trip.originCity} -> ${trip.destinationCity}, Siège $seat",
                    method = paymentMethod
                )
            )

            val booking = repository.bookSeat(
                tripNumber = trip.tripNumber,
                passengerName = passengerName,
                passengerPhone = passengerPhone,
                seatNumber = seat,
                paymentMethod = paymentMethod
            )

            if (booking != null) {
                _lastConfirmedBooking.value = booking
                _toastMessage.value = "Réservation confirmée ! Siège N°$seat réservé avec succès."
                onSuccess(booking)
            } else {
                _toastMessage.value = "Erreur: Ce siège est déjà occupé."
            }
        }
    }

    fun verifyTicket(bookingNumber: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repository.verifyAndBoardTicket(bookingNumber)
            _toastMessage.value = res.second
            onResult(res.first, res.second)
        }
    }

    fun createNewShipment(
        senderName: String,
        senderPhone: String,
        senderCity: String,
        senderAddress: String,
        recipientName: String,
        recipientPhone: String,
        recipientCity: String,
        recipientAddress: String,
        deliveryType: DeliveryType,
        parcelType: ShipmentType,
        description: String,
        weightKg: Double,
        declaredValueGnf: Long,
        isFragile: Boolean,
        isUrgent: Boolean,
        hasInsurance: Boolean,
        pricing: PricingBreakdown,
        paymentMethod: PaymentMethod,
        onSuccess: (ShipmentEntity) -> Unit
    ) {
        viewModelScope.launch {
            val provider = PaymentGatewayRegistry.getProvider(paymentMethod)
            provider.initiatePayment(
                PaymentRequest(
                    reference = "SHIPMENT-INIT",
                    amountGnf = pricing.totalGnf,
                    customerPhone = senderPhone,
                    customerName = senderName,
                    description = "Envoi colis $senderCity -> $recipientCity ($weightKg kg)",
                    method = paymentMethod
                )
            )

            val shipment = repository.createShipment(
                senderName = senderName,
                senderPhone = senderPhone,
                senderCity = senderCity,
                senderAddress = senderAddress,
                recipientName = recipientName,
                recipientPhone = recipientPhone,
                recipientCity = recipientCity,
                recipientAddress = recipientAddress,
                deliveryType = deliveryType,
                parcelType = parcelType,
                description = description,
                weightKg = weightKg,
                declaredValueGnf = declaredValueGnf,
                isFragile = isFragile,
                isUrgent = isUrgent,
                hasInsurance = hasInsurance,
                pricing = pricing,
                paymentMethod = paymentMethod
            )

            _toastMessage.value = "Colis créé avec succès ! N° de suivi : ${shipment.trackingNumber}"
            loadShipmentForTracking(shipment.trackingNumber)
            onSuccess(shipment)
        }
    }

    // Sandbox one-tap advance button
    fun advanceActiveShipmentStatus() {
        val tracking = _activeTrackedShipment.value?.trackingNumber ?: return
        viewModelScope.launch {
            val res = repository.advanceShipmentStatus(tracking, "Agent Sandbox (Démo)")
            _toastMessage.value = res.second
        }
    }

    fun deliverShipmentWithOtp(
        trackingNumber: String,
        otp: String,
        recipientName: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.verifyAndDeliverParcel(
                trackingNumber = trackingNumber,
                enteredOtp = otp,
                recipientName = recipientName,
                operatorName = "Livreur / Guichetier"
            )
            _toastMessage.value = res.second
            onComplete(res.first, res.second)
        }
    }

    fun sendSupportChat(message: String) {
        val tracking = _activeTrackedShipment.value?.trackingNumber ?: return
        if (message.isBlank()) return
        viewModelScope.launch {
            val sender = when (_currentRole.value) {
                UserRole.CLIENT -> "Client"
                UserRole.AGENCY_AGENT -> "Agent Agence"
                UserRole.DRIVER -> "Chauffeur"
                UserRole.ADMIN -> "Superviseur Admin"
            }
            repository.sendSupportMessage(
                trackingNumber = tracking,
                senderRole = _currentRole.value.name,
                senderName = sender,
                message = message.trim()
            )
        }
    }

    fun reportIncident(
        relatedTarget: String,
        type: String,
        description: String,
        priority: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.reportIncident(
                relatedTrackingOrTrip = relatedTarget,
                type = type,
                description = description,
                priority = priority,
                reportedBy = "Opérateur: ${_currentRole.value.label}"
            )
            _toastMessage.value = "Incident signalé et consigné dans le registre."
            onSuccess()
        }
    }

    fun resolveIncident(incidentNumber: String, notes: String) {
        viewModelScope.launch {
            repository.resolveIncident(incidentNumber, notes)
            _toastMessage.value = "Incident $incidentNumber résolu avec succès."
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.seedInitialData()
            loadShipmentForTracking("GT-2026-00018452")
            _toastMessage.value = "Données de démonstration réinitialisées."
        }
    }
}
