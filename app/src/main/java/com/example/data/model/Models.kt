package com.example.data.model

enum class UserRole(val label: String) {
    CLIENT("Client Voyageur & Expéditeur"),
    AGENCY_AGENT("Agent d'Agence"),
    DRIVER("Chauffeur / Livreur"),
    ADMIN("Administrateur Général")
}

data class GuineanCity(
    val name: String,
    val region: String,
    val distanceKmFromConakry: Int,
    val latitude: Double,
    val longitude: Double
)

object GuineanCities {
    val ALL = listOf(
        GuineanCity("Conakry", "Zone Spéciale de Conakry", 0, 9.5370, -13.6785),
        GuineanCity("Coyah", "Basse Guinée", 50, 9.7042, -13.3853),
        GuineanCity("Dubréka", "Basse Guinée", 52, 9.7911, -13.5233),
        GuineanCity("Kindia", "Basse Guinée", 135, 10.0569, -12.8658),
        GuineanCity("Boké", "Basse Guinée", 250, 10.9409, -14.2968),
        GuineanCity("Mamou", "Moyenne Guinée", 270, 10.3755, -12.0915),
        GuineanCity("Labé", "Moyenne Guinée", 400, 11.3182, -12.2895),
        GuineanCity("Faranah", "Haute Guinée", 440, 10.0410, -10.7434),
        GuineanCity("Kissidougou", "Guinée Forestière", 570, 9.1848, -10.0999),
        GuineanCity("Kankan", "Haute Guinée", 655, 10.3854, -9.3057),
        GuineanCity("Siguiri", "Haute Guinée", 780, 11.4228, -9.1685),
        GuineanCity("Nzérékoré", "Guinée Forestière", 860, 7.7562, -8.8179),
        GuineanCity("Guéckédou", "Guinée Forestière", 670, 8.5674, -10.1336)
    )

    fun findCity(name: String): GuineanCity {
        return ALL.find { it.name.equals(name, ignoreCase = true) }
            ?: GuineanCity(name, "Guinée", 300, 9.9, -12.0)
    }
}

enum class ShipmentStatus(val label: String, val stepIndex: Int) {
    CREATED("Enregistré", 0),
    AWAITING_PAYMENT("En attente de paiement", 1),
    PAID("Payé", 2),
    RECEIVED_AT_ORIGIN("Déposé à l'agence d'origine", 3),
    PROCESSING("En cours de traitement", 4),
    IN_TRANSIT("En transit interurbain", 5),
    ARRIVED_AT_DESTINATION("Arrivé à destination", 6),
    READY_FOR_PICKUP("Prêt pour retrait en agence", 7),
    OUT_FOR_DELIVERY("En cours de livraison à domicile", 7),
    DELIVERED("Livré au destinataire", 8),
    CANCELLED("Annulé", -1),
    FAILED_DELIVERY("Échec de livraison", -1),
    RETURNED("Retourné à l'expéditeur", -1);

    fun isTerminal(): Boolean = this == DELIVERED || this == CANCELLED || this == RETURNED
}

enum class ShipmentType(val label: String, val baseGnf: Long) {
    DOCUMENT("Documents & Papiers officiels", 25000L),
    VETEMENT("Textile & Vêtements", 35000L),
    NOURRITURE("Denrées alimentaires autorisées", 40000L),
    ELECTRONIQUE("Appareils électroniques & Téléphones", 50000L),
    PETIT_COLIS("Petit colis (< 5 kg)", 35000L),
    VOLUMINEUX("Colis volumineux (> 10 kg)", 65000L),
    AUTRE("Autres marchandises", 40000L)
}

enum class DeliveryType(val label: String) {
    AGENCY_PICKUP("Retrait en agence"),
    HOME_DELIVERY("Livraison à domicile (+25 000 GNF)")
}

enum class PaymentMethod(val label: String, val isOnline: Boolean) {
    ORANGE_MONEY("Orange Money Guinée (*144#)", true),
    MTN_MOMO("MTN Mobile Money (*440#)", true),
    CASH_AGENCY("Espèces au comptoir de l'agence", false),
    CARTE_BANCAIRE("Carte Bancaire (Visa/Mastercard)", true)
}

enum class PaymentStatus(val label: String) {
    PENDING("En attente"),
    PAID("Payé avec succès"),
    PAY_ON_DELIVERY("À payer à la livraison"),
    FAILED("Échec")
}

data class Seat(
    val seatNumber: Int,
    val status: SeatStatus
)

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    OCCUPIED,
    RESERVED
}
