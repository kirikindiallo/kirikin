package com.example.data.repository

import com.example.data.local.AgencyEntity
import com.example.data.local.BookingEntity
import com.example.data.local.DriverEntity
import com.example.data.local.GuineeTransitDao
import com.example.data.local.IncidentEntity
import com.example.data.local.ShipmentEntity
import com.example.data.local.ShipmentEventEntity
import com.example.data.local.SupportMessageEntity
import com.example.data.local.TripEntity
import com.example.data.local.VehicleEntity
import com.example.data.model.DeliveryType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentStatus
import com.example.data.model.ShipmentStatus
import com.example.data.model.ShipmentType
import com.example.data.sample.GuineeSampleData
import com.example.domain.pricing.PricingBreakdown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class GuineeTransitRepository(
    private val dao: GuineeTransitDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    init {
        // Pre-populate with realistic Guinean seed data if database is fresh
        scope.launch {
            val existingTrips = dao.getAllTrips().first()
            if (existingTrips.isEmpty()) {
                seedInitialData()
            }
        }
    }

    suspend fun seedInitialData() {
        dao.insertAgencies(GuineeSampleData.agencies)
        dao.insertVehicles(GuineeSampleData.vehicles)
        dao.insertDrivers(GuineeSampleData.drivers)
        dao.insertTrips(GuineeSampleData.trips)
        dao.insertBookings(GuineeSampleData.bookings)
        dao.insertShipments(GuineeSampleData.shipments)
        dao.insertShipmentEvents(GuineeSampleData.shipmentEvents)
        dao.insertIncidents(GuineeSampleData.incidents)
        for (msg in GuineeSampleData.supportMessages) {
            dao.insertSupportMessage(msg)
        }
    }

    // --- Trips & Bookings ---
    val allTrips: Flow<List<TripEntity>> = dao.getAllTrips()
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()

    fun searchTrips(origin: String, destination: String): Flow<List<TripEntity>> {
        return if (origin.isBlank() && destination.isBlank()) {
            dao.getAllTrips()
        } else if (origin.isNotBlank() && destination.isBlank()) {
            dao.getAllTrips() // Filtered in VM or query
        } else {
            dao.searchTrips(origin, destination)
        }
    }

    fun getTrip(tripNumber: String): Flow<TripEntity?> = dao.getTrip(tripNumber)

    suspend fun bookSeat(
        tripNumber: String,
        passengerName: String,
        passengerPhone: String,
        seatNumber: Int,
        paymentMethod: PaymentMethod
    ): BookingEntity? {
        val trip = dao.getTripSync(tripNumber) ?: return null
        val currentBooked = if (trip.bookedSeatsCsv.isBlank()) emptyList() else trip.bookedSeatsCsv.split(",")
        if (currentBooked.contains(seatNumber.toString())) {
            return null // Seat already occupied
        }

        val updatedBooked = (currentBooked + seatNumber.toString()).distinct().joinToString(",")
        dao.updateTrip(trip.copy(bookedSeatsCsv = updatedBooked))

        val bookingNumber = "RES-2026-00" + Random.nextInt(1000, 9999)
        val booking = BookingEntity(
            bookingNumber = bookingNumber,
            tripNumber = trip.tripNumber,
            passengerName = passengerName,
            passengerPhone = passengerPhone,
            seatNumber = seatNumber,
            originCity = trip.originCity,
            destinationCity = trip.destinationCity,
            departureDate = trip.departureDate,
            departureTime = trip.departureTime,
            companyName = trip.companyName,
            vehiclePlate = trip.vehiclePlate,
            amountGnf = trip.priceGnf,
            paymentMethod = paymentMethod,
            paymentStatus = PaymentStatus.PAID,
            status = "VALID"
        )
        dao.insertBooking(booking)
        return booking
    }

    suspend fun verifyAndBoardTicket(bookingNumber: String): Pair<Boolean, String> {
        val booking = dao.getBookingSync(bookingNumber)
            ?: return Pair(false, "Billet introuvable dans le système.")
        return when (booking.status) {
            "BOARDED" -> Pair(false, "Ce billet a DÉJÀ ÉTÉ UTILISÉ pour l'embarquement.")
            "CANCELLED" -> Pair(false, "Ce billet a été ANNULÉ.")
            "VALID" -> {
                dao.updateBooking(booking.copy(status = "BOARDED"))
                Pair(true, "Billet VALIDE. Embarquement confirmé pour ${booking.passengerName}, Siège ${booking.seatNumber}.")
            }
            else -> Pair(false, "Statut du billet non valide: ${booking.status}")
        }
    }

    // --- Shipments ---
    val allShipments: Flow<List<ShipmentEntity>> = dao.getAllShipments()

    fun getShipment(trackingNumber: String): Flow<ShipmentEntity?> =
        dao.getShipmentByTrackingNumber(trackingNumber)

    fun getShipmentEvents(trackingNumber: String): Flow<List<ShipmentEventEntity>> =
        dao.getEventsForShipment(trackingNumber)

    fun getShipmentsByPhone(phone: String): Flow<List<ShipmentEntity>> =
        dao.getShipmentsByPhone(phone)

    suspend fun createShipment(
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
        paymentMethod: PaymentMethod
    ): ShipmentEntity {
        val trackingNumber = "GT-2026-000" + Random.nextInt(10000, 99999)
        val secretToken = "SEC-" + Random.nextInt(1000, 9999) + "-GN"
        val otp = String.format("%06d", Random.nextInt(100000, 999999))

        val shipment = ShipmentEntity(
            trackingNumber = trackingNumber,
            secretToken = secretToken,
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
            basePriceGnf = pricing.basePriceGnf,
            weightSurchargeGnf = pricing.weightSurchargeGnf,
            optionsSurchargeGnf = pricing.homeDeliverySurchargeGnf + pricing.specialServicesSurchargeGnf + pricing.insuranceSurchargeGnf,
            totalGnf = pricing.totalGnf,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = paymentMethod,
            currentStatus = ShipmentStatus.CREATED,
            currentCity = senderCity,
            deliveryOtp = otp
        )

        dao.insertShipment(shipment)

        // Event 1: Creation
        dao.insertShipmentEvent(
            ShipmentEventEntity(
                trackingNumber = trackingNumber,
                timestamp = System.currentTimeMillis(),
                status = ShipmentStatus.CREATED,
                city = senderCity,
                agencyName = "GuinéeTransit Web/Mobile",
                description = "Envoi créé et enregistré. N° de suivi généré: $trackingNumber",
                operatorName = "Expéditeur: $senderName"
            )
        )

        // Event 2: Payment confirmation
        dao.insertShipmentEvent(
            ShipmentEventEntity(
                trackingNumber = trackingNumber,
                timestamp = System.currentTimeMillis() + 1000,
                status = ShipmentStatus.PAID,
                city = senderCity,
                agencyName = "Passerelle ${paymentMethod.label}",
                description = "Paiement de ${pricing.totalGnf} GNF validé avec succès.",
                operatorName = "Système Automatique"
            )
        )

        return shipment
    }

    suspend fun advanceShipmentStatus(
        trackingNumber: String,
        operatorName: String = "Agent Opérations",
        customNote: String? = null
    ): Pair<Boolean, String> {
        val shipment = dao.getShipmentSync(trackingNumber)
            ?: return Pair(false, "Colis introuvable.")

        val nextStatus = when (shipment.currentStatus) {
            ShipmentStatus.CREATED -> ShipmentStatus.PAID
            ShipmentStatus.AWAITING_PAYMENT -> ShipmentStatus.PAID
            ShipmentStatus.PAID -> ShipmentStatus.RECEIVED_AT_ORIGIN
            ShipmentStatus.RECEIVED_AT_ORIGIN -> ShipmentStatus.PROCESSING
            ShipmentStatus.PROCESSING -> ShipmentStatus.IN_TRANSIT
            ShipmentStatus.IN_TRANSIT -> ShipmentStatus.ARRIVED_AT_DESTINATION
            ShipmentStatus.ARRIVED_AT_DESTINATION -> {
                if (shipment.deliveryType == DeliveryType.HOME_DELIVERY) {
                    ShipmentStatus.OUT_FOR_DELIVERY
                } else {
                    ShipmentStatus.READY_FOR_PICKUP
                }
            }
            ShipmentStatus.READY_FOR_PICKUP -> ShipmentStatus.DELIVERED
            ShipmentStatus.OUT_FOR_DELIVERY -> ShipmentStatus.DELIVERED
            ShipmentStatus.DELIVERED -> return Pair(false, "Le colis est déjà marqué comme LIVRÉ.")
            ShipmentStatus.CANCELLED -> return Pair(false, "Ce colis a été annulé.")
            ShipmentStatus.FAILED_DELIVERY -> ShipmentStatus.READY_FOR_PICKUP
            ShipmentStatus.RETURNED -> return Pair(false, "Ce colis est retourné.")
        }

        val updatedCity = when (nextStatus) {
            ShipmentStatus.IN_TRANSIT -> "En route (RN1/RN2)"
            ShipmentStatus.ARRIVED_AT_DESTINATION,
            ShipmentStatus.READY_FOR_PICKUP,
            ShipmentStatus.OUT_FOR_DELIVERY,
            ShipmentStatus.DELIVERED -> shipment.recipientCity
            else -> shipment.senderCity
        }

        val updatedShipment = shipment.copy(
            currentStatus = nextStatus,
            currentCity = updatedCity,
            deliveredAt = if (nextStatus == ShipmentStatus.DELIVERED) System.currentTimeMillis() else shipment.deliveredAt,
            deliveryProofRecipientName = if (nextStatus == ShipmentStatus.DELIVERED) shipment.recipientName else shipment.deliveryProofRecipientName,
            deliveryProofSignatureDate = if (nextStatus == ShipmentStatus.DELIVERED) System.currentTimeMillis() else shipment.deliveryProofSignatureDate
        )

        dao.updateShipment(updatedShipment)

        val description = customNote ?: when (nextStatus) {
            ShipmentStatus.PAID -> "Paiement confirmé"
            ShipmentStatus.RECEIVED_AT_ORIGIN -> "Colis déposé et scanné à l'agence de ${shipment.senderCity}"
            ShipmentStatus.PROCESSING -> "Colis vérifié et assigné au manifeste de transport"
            ShipmentStatus.IN_TRANSIT -> "Colis chargé et en transit interurbain vers ${shipment.recipientCity}"
            ShipmentStatus.ARRIVED_AT_DESTINATION -> "Colis déchargé et vérifié à l'agence de ${shipment.recipientCity}"
            ShipmentStatus.READY_FOR_PICKUP -> "Disponible au guichet pour retrait par le destinataire avec pièce d'identité et code OTP"
            ShipmentStatus.OUT_FOR_DELIVERY -> "Pris en charge par le livreur pour livraison à domicile à ${shipment.recipientAddress}"
            ShipmentStatus.DELIVERED -> "Colis remis au destinataire (${shipment.recipientName}) avec validation du code OTP de sécurité"
            else -> "Changement de statut: ${nextStatus.label}"
        }

        dao.insertShipmentEvent(
            ShipmentEventEntity(
                trackingNumber = trackingNumber,
                timestamp = System.currentTimeMillis(),
                status = nextStatus,
                city = updatedCity,
                agencyName = "Agence de $updatedCity",
                description = description,
                operatorName = operatorName
            )
        )

        return Pair(true, "Statut avancé vers: ${nextStatus.label}")
    }

    suspend fun verifyAndDeliverParcel(
        trackingNumber: String,
        enteredOtp: String,
        recipientName: String,
        operatorName: String = "Livreur / Agent"
    ): Pair<Boolean, String> {
        val shipment = dao.getShipmentSync(trackingNumber)
            ?: return Pair(false, "Colis introuvable.")

        if (shipment.currentStatus == ShipmentStatus.DELIVERED) {
            return Pair(false, "Ce colis a déjà été livré le ${shipment.deliveredAt}.")
        }

        if (shipment.deliveryOtp.trim() != enteredOtp.trim()) {
            return Pair(false, "Code OTP invalide. Veuillez vérifier le code à 6 chiffres reçu par le destinataire.")
        }

        val updated = shipment.copy(
            currentStatus = ShipmentStatus.DELIVERED,
            deliveredAt = System.currentTimeMillis(),
            deliveryProofRecipientName = recipientName.ifBlank { shipment.recipientName },
            deliveryProofSignatureDate = System.currentTimeMillis()
        )
        dao.updateShipment(updated)

        dao.insertShipmentEvent(
            ShipmentEventEntity(
                trackingNumber = trackingNumber,
                timestamp = System.currentTimeMillis(),
                status = ShipmentStatus.DELIVERED,
                city = shipment.recipientCity,
                agencyName = if (shipment.deliveryType == DeliveryType.HOME_DELIVERY) "Livraison Domicile" else "Guichet Agence",
                description = "Colis remis avec succès à $recipientName. Preuve de livraison signée électroniquement et OTP validé.",
                operatorName = operatorName
            )
        )

        return Pair(true, "Livraison confirmée avec succès !")
    }

    // --- Agencies, Vehicles, Drivers ---
    val allAgencies: Flow<List<AgencyEntity>> = dao.getAllAgencies()
    val allVehicles: Flow<List<VehicleEntity>> = dao.getAllVehicles()
    val allDrivers: Flow<List<DriverEntity>> = dao.getAllDrivers()

    // --- Incidents ---
    val allIncidents: Flow<List<IncidentEntity>> = dao.getAllIncidents()

    suspend fun reportIncident(
        relatedTrackingOrTrip: String,
        type: String,
        description: String,
        priority: String,
        reportedBy: String
    ): IncidentEntity {
        val incidentNumber = "INC-2026-00" + Random.nextInt(100, 999)
        val incident = IncidentEntity(
            incidentNumber = incidentNumber,
            relatedTrackingOrTrip = relatedTrackingOrTrip,
            type = type,
            description = description,
            priority = priority,
            status = "OPEN",
            reportedBy = reportedBy,
            timestamp = System.currentTimeMillis()
        )
        dao.insertIncident(incident)
        return incident
    }

    suspend fun resolveIncident(incidentNumber: String, notes: String) {
        val all = dao.getAllIncidents().first()
        val found = all.find { it.incidentNumber == incidentNumber } ?: return
        dao.updateIncident(found.copy(status = "RESOLVED", resolutionNotes = notes))
    }

    // --- Support Chat ---
    fun getMessagesForShipment(trackingNumber: String): Flow<List<SupportMessageEntity>> =
        dao.getMessagesForShipment(trackingNumber)

    suspend fun sendSupportMessage(
        trackingNumber: String,
        senderRole: String,
        senderName: String,
        message: String
    ) {
        dao.insertSupportMessage(
            SupportMessageEntity(
                trackingNumber = trackingNumber,
                senderRole = senderRole,
                senderName = senderName,
                message = message
            )
        )
    }

    // --- Dashboard KPIs ---
    val totalShipmentsCount: Flow<Int> = dao.countTotalShipments()
    val inTransitShipmentsCount: Flow<Int> = dao.countShipmentsInTransit()
    val deliveredShipmentsCount: Flow<Int> = dao.countShipmentsDelivered()
    val totalBookingsCount: Flow<Int> = dao.countTotalBookings()
    val activeIncidentsCount: Flow<Int> = dao.countActiveIncidents()
    val shipmentRevenueSum: Flow<Long?> = dao.sumShipmentRevenue()
    val bookingRevenueSum: Flow<Long?> = dao.sumBookingRevenue()
}
