package com.example.data.sample

import com.example.data.local.AgencyEntity
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
import com.example.data.model.PaymentStatus
import com.example.data.model.ShipmentStatus
import com.example.data.model.ShipmentType

object GuineeSampleData {

    val agencies = listOf(
        AgencyEntity(
            id = "AG-CKY-MATAM",
            name = "Agence Centrale Matam",
            city = "Conakry",
            address = "Autoroute Fidel Castro, Quartier Matam Lido",
            phone = "+224 622 10 20 30",
            openingHours = "06:00 - 21:00 (Tous les jours)",
            latitude = 9.5489,
            longitude = -13.6621,
            services = "Départs Voyageurs,Arrivées,Dépôt Colis,Retrait Colis,Paiement Mobile"
        ),
        AgencyEntity(
            id = "AG-CKY-MADINA",
            name = "Agence Gare Routière Madina",
            city = "Conakry",
            address = "Grand Marché Madina, Face Carrefour Constantin",
            phone = "+224 624 55 66 77",
            openingHours = "06:00 - 20:00",
            latitude = 9.5412,
            longitude = -13.6705,
            services = "Départs Voyageurs,Dépôt Colis Express,Retrait Colis"
        ),
        AgencyEntity(
            id = "AG-KIN-CENTRE",
            name = "Agence Kindia Gare",
            city = "Kindia",
            address = "Avenue de la République, Face Marché Wambélé",
            phone = "+224 628 33 44 55",
            openingHours = "06:30 - 19:30",
            latitude = 10.0569,
            longitude = -12.8658,
            services = "Voyages,Dépôt Colis,Retrait Colis,Livraison Locale"
        ),
        AgencyEntity(
            id = "AG-MAM-PITA",
            name = "Agence Carrefour Mamou",
            city = "Mamou",
            address = "Carrefour Luna, Route Nationale 1",
            phone = "+224 620 99 88 77",
            openingHours = "24h/24 (Nœud de transit national)",
            latitude = 10.3755,
            longitude = -12.0915,
            services = "Transit National,Départs,Arrivées,Hub Colis,Restauration"
        ),
        AgencyEntity(
            id = "AG-LAB-DAKA",
            name = "Agence Labé Daka",
            city = "Labé",
            address = "Quartier Daka 1, Proche Stade Régional Saifoulaye Diallo",
            phone = "+224 621 44 22 11",
            openingHours = "06:00 - 20:00",
            latitude = 11.3182,
            longitude = -12.2895,
            services = "Voyages,Fret Express,Retrait Colis,Livraison Domicile"
        ),
        AgencyEntity(
            id = "AG-BOK-CENTRE",
            name = "Agence Boké Centre-Ville",
            city = "Boké",
            address = "Boulevard Central, Face Préfecture",
            phone = "+224 623 77 88 99",
            openingHours = "07:00 - 19:00",
            latitude = 10.9409,
            longitude = -14.2968,
            services = "Voyages Minibus,Colis Bauxite Express,Paiement"
        ),
        AgencyEntity(
            id = "AG-KAN-BATE",
            name = "Agence Kankan Batè",
            city = "Kankan",
            address = "Quartier Dibida, Carrefour Chérifula",
            phone = "+224 629 12 34 56",
            openingHours = "06:00 - 20:30",
            latitude = 10.3854,
            longitude = -9.3057,
            services = "Voyages Grand Bus,Hub Haute Guinée,Retrait Colis"
        ),
        AgencyEntity(
            id = "AG-NZE-COMM",
            name = "Agence Nzérékoré Commerce",
            city = "Nzérékoré",
            address = "Rue Commerciale, Face Banque Centrale",
            phone = "+224 626 98 76 54",
            openingHours = "06:00 - 19:30",
            latitude = 7.7562,
            longitude = -8.8179,
            services = "Voyages Forestière,Colis Café-Cacao,Retrait Sécurisé"
        ),
        AgencyEntity(
            id = "AG-FAR-CENTRE",
            name = "Agence Faranah Niger",
            city = "Faranah",
            address = "Carrefour du Fleuve Niger, RN2",
            phone = "+224 625 31 42 53",
            openingHours = "06:30 - 19:00",
            latitude = 10.0410,
            longitude = -10.7434,
            services = "Voyages,Dépôt Colis,Retrait Agence"
        ),
        AgencyEntity(
            id = "AG-KIS-GARE",
            name = "Agence Kissidougou Gare",
            city = "Kissidougou",
            address = "Gare Routière Centrale, Route de Guéckédou",
            phone = "+224 627 65 43 21",
            openingHours = "06:30 - 19:30",
            latitude = 9.1848,
            longitude = -10.0999,
            services = "Voyages,Dépôt Colis,Transit Forestier"
        )
    )

    val vehicles = listOf(
        VehicleEntity("RC-4521-A", "Toyota Coaster VIP", 14, "GuinéeTransit Express", "Mamadou Alpha Diallo", "En trajet", "2026-10-15"),
        VehicleEntity("RC-8910-B", "Mercedes Sprinter 316", 14, "GuinéeTransit Express", "Ibrahima Sory Camara", "Disponible", "2026-10-20"),
        VehicleEntity("RC-3104-C", "King Long Grand Tourisme", 45, "Fouta Voyage Pro", "Boubacar Barry", "Disponible", "2026-10-25"),
        VehicleEntity("RC-6672-D", "Yutong Intercity", 45, "Mandingue Trans", "Sekouba Condé", "En service", "2026-10-18"),
        VehicleEntity("RC-1290-E", "Toyota HiAce Confort", 14, "GuinéeTransit Express", "Amadou Oury Sow", "En trajet", "2026-11-01"),
        VehicleEntity("RC-9043-F", "Toyota Hilux Cargo Express", 2, "GuinéeTransit Fret", "Fodé Bangoura", "Disponible", "2026-10-22"),
        VehicleEntity("RC-5511-G", "Mercedes 413 Fourgon Fret", 3, "GuinéeTransit Logistique", "Alseny Sylla", "En service", "2026-10-30"),
        VehicleEntity("RC-7723-H", "Peugeot Expert Fret Urbain", 2, "GuinéeTransit Rapide", "Moussa Keïta", "Disponible", "2026-11-05"),
        VehicleEntity("RC-2089-J", "Hyundai County VIP", 18, "Sahel Direct", "Ousmane Traoré", "Maintenance", "2026-10-05"),
        VehicleEntity("RC-4401-K", "Toyota Prado VIP Navette", 4, "GuinéeTransit Prestige", "Cherif Haidara", "Disponible", "2026-10-28")
    )

    val drivers = listOf(
        DriverEntity("DRV-01", "Mamadou Alpha Diallo", "+224 622 89 01 23", "PC-GN-2018-8472", "RC-4521-A", "En trajet", 4.9, 320),
        DriverEntity("DRV-02", "Ibrahima Sory Camara", "+224 624 33 22 11", "PC-GN-2019-1029", "RC-8910-B", "Disponible", 4.8, 280),
        DriverEntity("DRV-03", "Boubacar Barry", "+224 628 44 55 66", "PC-GN-2016-9041", "RC-3104-C", "Disponible", 4.9, 450),
        DriverEntity("DRV-04", "Sekouba Condé", "+224 620 11 99 88", "PC-GN-2017-3820", "RC-6672-D", "En service", 4.7, 310),
        DriverEntity("DRV-05", "Amadou Oury Sow", "+224 621 77 66 55", "PC-GN-2020-5612", "RC-1290-E", "En trajet", 4.8, 190),
        DriverEntity("DRV-06", "Fodé Bangoura", "+224 623 88 44 22", "PC-GN-2018-7291", "RC-9043-F", "Disponible", 4.9, 340),
        DriverEntity("DRV-07", "Alseny Sylla", "+224 629 55 12 34", "PC-GN-2019-6184", "RC-5511-G", "En service", 4.6, 210),
        DriverEntity("DRV-08", "Moussa Keïta", "+224 626 77 33 99", "PC-GN-2021-9901", "RC-7723-H", "Disponible", 4.9, 140),
        DriverEntity("DRV-09", "Ousmane Traoré", "+224 625 22 88 44", "PC-GN-2017-4318", "RC-2089-J", "Repos", 4.7, 380),
        DriverEntity("DRV-10", "Cherif Haidara", "+224 627 90 80 70", "PC-GN-2015-1190", "RC-4401-K", "Disponible", 5.0, 520)
    )

    val trips = listOf(
        TripEntity(
            tripNumber = "TRIP-2026-00981",
            originCity = "Conakry",
            destinationCity = "Labé",
            departureDate = "2026-10-02",
            departureTime = "07:00",
            estimatedArrival = "15:30",
            durationHours = "8h 30m",
            companyName = "GuinéeTransit Express",
            vehiclePlate = "RC-4521-A",
            vehicleType = "Minibus VIP 14 places",
            totalSeats = 14,
            bookedSeatsCsv = "1,2,5,8,9",
            priceGnf = 150000L,
            amenities = "Climatisation, WiFi, Sièges inclinables, 25kg bagage"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00982",
            originCity = "Conakry",
            destinationCity = "Kankan",
            departureDate = "2026-10-02",
            departureTime = "06:30",
            estimatedArrival = "18:00",
            durationHours = "11h 30m",
            companyName = "Mandingue Trans",
            vehiclePlate = "RC-6672-D",
            vehicleType = "Bus Grand Tourisme 45 places",
            totalSeats = 45,
            bookedSeatsCsv = "1,2,3,4,7,8,12,15,16,20,21",
            priceGnf = 220000L,
            amenities = "Climatisation, Écrans TV, Prises de charge, Climatiseur"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00983",
            originCity = "Conakry",
            destinationCity = "Kindia",
            departureDate = "2026-10-02",
            departureTime = "08:30",
            estimatedArrival = "11:30",
            durationHours = "3h 00m",
            companyName = "GuinéeTransit Express",
            vehiclePlate = "RC-8910-B",
            vehicleType = "Minibus Confort 14 places",
            totalSeats = 14,
            bookedSeatsCsv = "3,4,6",
            priceGnf = 60000L,
            amenities = "Départ rapide, Climatisation, Bagages sécurisés"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00984",
            originCity = "Conakry",
            destinationCity = "Boké",
            departureDate = "2026-10-02",
            departureTime = "07:30",
            estimatedArrival = "12:00",
            durationHours = "4h 30m",
            companyName = "Boké Express",
            vehiclePlate = "RC-1290-E",
            vehicleType = "Minibus VIP 14 places",
            totalSeats = 14,
            bookedSeatsCsv = "2,3,7,10",
            priceGnf = 85000L,
            amenities = "Climatisation, Sièges velours, Eau minérale offerte"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00985",
            originCity = "Conakry",
            destinationCity = "Nzérékoré",
            departureDate = "2026-10-02",
            departureTime = "06:00",
            estimatedArrival = "21:00",
            durationHours = "15h 00m",
            companyName = "Fouta Voyage Pro",
            vehiclePlate = "RC-3104-C",
            vehicleType = "Bus Grand Confort 45 places",
            totalSeats = 45,
            bookedSeatsCsv = "1,2,5,6,9,10,14,18,22,23,24,30,35",
            priceGnf = 300000L,
            amenities = "WiFi à bord, Climatisation double zone, Arrêt repas Mamou"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00986",
            originCity = "Labé",
            destinationCity = "Conakry",
            departureDate = "2026-10-02",
            departureTime = "07:00",
            estimatedArrival = "15:30",
            durationHours = "8h 30m",
            companyName = "GuinéeTransit Express",
            vehiclePlate = "RC-4521-A",
            vehicleType = "Minibus VIP 14 places",
            totalSeats = 14,
            bookedSeatsCsv = "1,4,7",
            priceGnf = 150000L,
            amenities = "Climatisation, WiFi, Sièges inclinables"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00987",
            originCity = "Kankan",
            destinationCity = "Conakry",
            departureDate = "2026-10-02",
            departureTime = "06:30",
            estimatedArrival = "18:00",
            durationHours = "11h 30m",
            companyName = "Mandingue Trans",
            vehiclePlate = "RC-6672-D",
            vehicleType = "Bus Grand Tourisme 45 places",
            totalSeats = 45,
            bookedSeatsCsv = "3,4,8,11,15",
            priceGnf = 220000L,
            amenities = "Climatisation, Écrans TV, Prises de charge"
        ),
        TripEntity(
            tripNumber = "TRIP-2026-00988",
            originCity = "Mamou",
            destinationCity = "Labé",
            departureDate = "2026-10-02",
            departureTime = "10:00",
            estimatedArrival = "13:30",
            durationHours = "3h 30m",
            companyName = "GuinéeTransit Express",
            vehiclePlate = "RC-8910-B",
            vehicleType = "Minibus Confort 14 places",
            totalSeats = 14,
            bookedSeatsCsv = "2,5",
            priceGnf = 70000L,
            amenities = "Départ immédiat, Climatisation"
        )
    )

    val bookings = listOf(
        BookingEntity(
            bookingNumber = "RES-2026-00184",
            tripNumber = "TRIP-2026-00981",
            passengerName = "Alpha Oumar Bah",
            passengerPhone = "+224 622 45 78 90",
            seatNumber = 3,
            originCity = "Conakry",
            destinationCity = "Labé",
            departureDate = "2026-10-02",
            departureTime = "07:00",
            companyName = "GuinéeTransit Express",
            vehiclePlate = "RC-4521-A",
            amountGnf = 150000L,
            paymentMethod = PaymentMethod.ORANGE_MONEY,
            paymentStatus = PaymentStatus.PAID,
            status = "VALID"
        ),
        BookingEntity(
            bookingNumber = "RES-2026-00185",
            tripNumber = "TRIP-2026-00982",
            passengerName = "Mariama Siré Diallo",
            passengerPhone = "+224 628 12 34 56",
            seatNumber = 5,
            originCity = "Conakry",
            destinationCity = "Kankan",
            departureDate = "2026-10-02",
            departureTime = "06:30",
            companyName = "Mandingue Trans",
            vehiclePlate = "RC-6672-D",
            amountGnf = 220000L,
            paymentMethod = PaymentMethod.MTN_MOMO,
            paymentStatus = PaymentStatus.PAID,
            status = "VALID"
        )
    )

    val shipments = listOf(
        ShipmentEntity(
            trackingNumber = "GT-2026-00018452",
            secretToken = "SEC-8452-X",
            senderName = "Mamadou Bailo Diallo",
            senderPhone = "+224 622 11 22 33",
            senderCity = "Conakry",
            senderAddress = "Matam Lido, Conakry",
            recipientName = "Fatoumata Binta Barry",
            recipientPhone = "+224 628 44 55 66",
            recipientCity = "Labé",
            recipientAddress = "Quartier Daka 1, Labé",
            deliveryType = DeliveryType.AGENCY_PICKUP,
            parcelType = ShipmentType.ELECTRONIQUE,
            description = "Ordinateur portable HP + Chargeur d'origine",
            weightKg = 3.5,
            declaredValueGnf = 4500000L,
            isFragile = true,
            isUrgent = true,
            hasInsurance = true,
            basePriceGnf = 50000L,
            weightSurchargeGnf = 12000L,
            optionsSurchargeGnf = 35000L,
            totalGnf = 137000L,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = PaymentMethod.ORANGE_MONEY,
            currentStatus = ShipmentStatus.IN_TRANSIT,
            currentCity = "Mamou",
            deliveryOtp = "482917",
            tripId = "TRIP-2026-00981"
        ),
        ShipmentEntity(
            trackingNumber = "GT-2026-00018453",
            secretToken = "SEC-8453-Y",
            senderName = "Ibrahima Kalil Camara",
            senderPhone = "+224 624 99 88 77",
            senderCity = "Conakry",
            senderAddress = "Madina Marché, Conakry",
            recipientName = "Kadiatou Touré",
            recipientPhone = "+224 620 55 44 33",
            recipientCity = "Kankan",
            recipientAddress = "Quartier Timbo, Villa 12",
            deliveryType = DeliveryType.HOME_DELIVERY,
            parcelType = ShipmentType.VETEMENT,
            description = "Lot de pagnes wax et prêt-à-porter fête",
            weightKg = 8.0,
            declaredValueGnf = 2500000L,
            isFragile = false,
            isUrgent = false,
            hasInsurance = false,
            basePriceGnf = 35000L,
            weightSurchargeGnf = 48000L,
            optionsSurchargeGnf = 25000L,
            totalGnf = 143000L,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = PaymentMethod.MTN_MOMO,
            currentStatus = ShipmentStatus.READY_FOR_PICKUP,
            currentCity = "Kankan",
            deliveryOtp = "913402",
            tripId = "TRIP-2026-00982"
        ),
        ShipmentEntity(
            trackingNumber = "GT-2026-00018454",
            secretToken = "SEC-8454-Z",
            senderName = "Thierno Oumar Sow",
            senderPhone = "+224 621 34 56 78",
            senderCity = "Labé",
            senderAddress = "Daka 2, Labé",
            recipientName = "Hadja Mariama Camara",
            recipientPhone = "+224 623 78 90 12",
            recipientCity = "Conakry",
            recipientAddress = "Kipé Centre Émetteur",
            deliveryType = DeliveryType.HOME_DELIVERY,
            parcelType = ShipmentType.NOURRITURE,
            description = "Bidon de miel pur du Fouta + Fonio précuit",
            weightKg = 6.0,
            declaredValueGnf = 800000L,
            isFragile = false,
            isUrgent = false,
            hasInsurance = false,
            basePriceGnf = 40000L,
            weightSurchargeGnf = 32000L,
            optionsSurchargeGnf = 25000L,
            totalGnf = 117000L,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = PaymentMethod.CASH_AGENCY,
            currentStatus = ShipmentStatus.DELIVERED,
            currentCity = "Conakry",
            deliveryOtp = "635189",
            tripId = "TRIP-2026-00986",
            deliveredAt = System.currentTimeMillis() - 7200000L,
            deliveryProofRecipientName = "Hadja Mariama Camara",
            deliveryProofSignatureDate = System.currentTimeMillis() - 7200000L
        ),
        ShipmentEntity(
            trackingNumber = "GT-2026-00018455",
            secretToken = "SEC-8455-W",
            senderName = "Mohamed Lamine Keita",
            senderPhone = "+224 626 55 44 22",
            senderCity = "Boké",
            senderAddress = "Quartier Baralandé",
            recipientName = "Aboubacar Soumah",
            recipientPhone = "+224 629 11 33 55",
            recipientCity = "Conakry",
            recipientAddress = "Dixinn Bora",
            deliveryType = DeliveryType.AGENCY_PICKUP,
            parcelType = ShipmentType.DOCUMENT,
            description = "Dossiers d'appel d'offres originaux signés",
            weightKg = 0.8,
            declaredValueGnf = 500000L,
            isFragile = false,
            isUrgent = true,
            hasInsurance = false,
            basePriceGnf = 25000L,
            weightSurchargeGnf = 0L,
            optionsSurchargeGnf = 20000L,
            totalGnf = 55000L,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = PaymentMethod.ORANGE_MONEY,
            currentStatus = ShipmentStatus.RECEIVED_AT_ORIGIN,
            currentCity = "Boké",
            deliveryOtp = "724915"
        )
    )

    val shipmentEvents = listOf(
        // Events for GT-2026-00018452 (Conakry -> Labé)
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018452",
            timestamp = System.currentTimeMillis() - 18000000L,
            status = ShipmentStatus.CREATED,
            city = "Conakry",
            agencyName = "Agence Centrale Matam",
            description = "Colis créé et enregistré dans le système GuinéeTransit",
            operatorName = "Système / Client"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018452",
            timestamp = System.currentTimeMillis() - 17000000L,
            status = ShipmentStatus.PAID,
            city = "Conakry",
            agencyName = "Agence Centrale Matam",
            description = "Paiement de 137 000 GNF confirmé via Orange Money",
            operatorName = "Passerelle Orange Money"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018452",
            timestamp = System.currentTimeMillis() - 15000000L,
            status = ShipmentStatus.RECEIVED_AT_ORIGIN,
            city = "Conakry",
            agencyName = "Agence Centrale Matam",
            description = "Colis réceptionné au guichet, pesé (3.5 kg) et étiqueté QR",
            operatorName = "Agent Ousmane Camara"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018452",
            timestamp = System.currentTimeMillis() - 10000000L,
            status = ShipmentStatus.PROCESSING,
            city = "Conakry",
            agencyName = "Agence Centrale Matam",
            description = "Colis assigné au véhicule RC-4521-A pour Labé",
            operatorName = "Superviseur Fret"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018452",
            timestamp = System.currentTimeMillis() - 4000000L,
            status = ShipmentStatus.IN_TRANSIT,
            city = "Mamou",
            agencyName = "Agence Carrefour Mamou",
            description = "Passage au point de contrôle et transit Mamou Luna",
            operatorName = "Agent Mamou"
        ),

        // Events for GT-2026-00018453 (Conakry -> Kankan)
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018453",
            timestamp = System.currentTimeMillis() - 25000000L,
            status = ShipmentStatus.RECEIVED_AT_ORIGIN,
            city = "Conakry",
            agencyName = "Agence Gare Madina",
            description = "Colis déposé et emballé",
            operatorName = "Agent Madina"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018453",
            timestamp = System.currentTimeMillis() - 12000000L,
            status = ShipmentStatus.IN_TRANSIT,
            city = "Kouroussa",
            agencyName = "En route",
            description = "Colis en transit dans le bus RC-6672-D",
            operatorName = "Chauffeur Sekouba"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018453",
            timestamp = System.currentTimeMillis() - 3600000L,
            status = ShipmentStatus.READY_FOR_PICKUP,
            city = "Kankan",
            agencyName = "Agence Kankan Batè",
            description = "Colis arrivé à l'agence de destination. Prêt pour retrait ou tournée de livraison",
            operatorName = "Agent Kankan"
        ),

        // Events for GT-2026-00018454 (Delivered)
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018454",
            timestamp = System.currentTimeMillis() - 40000000L,
            status = ShipmentStatus.RECEIVED_AT_ORIGIN,
            city = "Labé",
            agencyName = "Agence Labé Daka",
            description = "Dépôt de 6 kg de produits du terroir",
            operatorName = "Agent Labé"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018454",
            timestamp = System.currentTimeMillis() - 20000000L,
            status = ShipmentStatus.ARRIVED_AT_DESTINATION,
            city = "Conakry",
            agencyName = "Agence Centrale Matam",
            description = "Arrivée à Conakry Matam",
            operatorName = "Agent Conakry"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018454",
            timestamp = System.currentTimeMillis() - 10000000L,
            status = ShipmentStatus.OUT_FOR_DELIVERY,
            city = "Conakry",
            agencyName = "Tournée Kipé",
            description = "Pris en charge par le livreur urbain Moussa",
            operatorName = "Livreur Moussa"
        ),
        ShipmentEventEntity(
            trackingNumber = "GT-2026-00018454",
            timestamp = System.currentTimeMillis() - 7200000L,
            status = ShipmentStatus.DELIVERED,
            city = "Conakry",
            agencyName = "Domicile Destinataire",
            description = "Colis remis à Hadja Mariama Camara après validation du code OTP secret",
            operatorName = "Livreur Moussa"
        )
    )

    val incidents = listOf(
        IncidentEntity(
            incidentNumber = "INC-2026-0041",
            relatedTrackingOrTrip = "TRIP-2026-00985",
            type = "Ralentissement travaux routiers",
            description = "Retard estimé de 1h30 entre Mamou et Faranah en raison de travaux de terrassement sur la RN2.",
            priority = "MOYENNE",
            status = "INVESTIGATING",
            reportedBy = "Chauffeur Boubacar Barry",
            timestamp = System.currentTimeMillis() - 14400000L,
            resolutionNotes = "Équipe prévenue, passagers informés par notification SMS."
        ),
        IncidentEntity(
            incidentNumber = "INC-2026-0042",
            relatedTrackingOrTrip = "GT-2026-00018452",
            type = "Inspection de sécurité préventive",
            description = "Vérification emballage renforcé pour équipement électronique fragile.",
            priority = "FAIBLE",
            status = "RESOLVED",
            reportedBy = "Agent Matam Ousmane",
            timestamp = System.currentTimeMillis() - 16000000L,
            resolutionNotes = "Protection papier bulle supplémentaire ajoutée sans frais."
        )
    )

    val supportMessages = listOf(
        SupportMessageEntity(
            trackingNumber = "GT-2026-00018452",
            senderRole = "CLIENT",
            senderName = "Mamadou Bailo (Expéditeur)",
            message = "Bonjour, l'ordinateur est-il bien parti de Conakry ce matin ?",
            timestamp = System.currentTimeMillis() - 11000000L
        ),
        SupportMessageEntity(
            trackingNumber = "GT-2026-00018452",
            senderRole = "SUPPORT",
            senderName = "Support GuinéeTransit",
            message = "Bonjour M. Diallo, oui tout à fait. Le colis est chargé dans le minibus VIP RC-4521-A et est actuellement au niveau de Mamou. Arrivée prévue à Labé vers 16h.",
            timestamp = System.currentTimeMillis() - 10500000L
        )
    )
}
