package com.example.data.local

import com.example.data.local.entity.ArtisanEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderRequestEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun populateInitialData(database: AppDatabase) = withContext(Dispatchers.IO) {
        val categoryDao = database.categoryDao()
        val artisanDao = database.artisanDao()
        val serviceDao = database.serviceDao()
        val productDao = database.productDao()
        val reviewDao = database.reviewDao()
        val orderDao = database.orderRequestDao()

        // 1. Prepopulate Categories
        val initialCategories = listOf(
            CategoryEntity(
                id = 1,
                name = "Potters",
                localizedName = "Kumhar / Clay Potters",
                iconName = "pottery",
                description = "Handcrafted terracotta cookware, traditional surahi, matkas, and festive diyas.",
                isActive = true,
                artisanCount = 4
            ),
            CategoryEntity(
                id = 2,
                name = "Tailors",
                localizedName = "Darzi / Master Tailors",
                iconName = "scissors",
                description = "Bespoke custom tailoring, suit alteration, ethnic kurtas, and traditional embroidery.",
                isActive = true,
                artisanCount = 5
            ),
            CategoryEntity(
                id = 3,
                name = "Cobblers",
                localizedName = "Mochi / Leather Artisans",
                iconName = "shoe",
                description = "Shoe restoration, sole replacement, handcrafted leather juttis, and bag repairs.",
                isActive = true,
                artisanCount = 3
            ),
            CategoryEntity(
                id = 4,
                name = "Artisans",
                localizedName = "Karigar / Traditional Crafts",
                iconName = "palette",
                description = "Handloom weaving, brass metal engraving, wood carving, and bamboo crafts.",
                isActive = true,
                artisanCount = 6
            ),
            CategoryEntity(
                id = 5,
                name = "Vendors",
                localizedName = "Local Micro-Vendors",
                iconName = "storefront",
                description = "Stone-ground organic spices, wood-pressed cold oils, artisanal pickles, and papads.",
                isActive = true,
                artisanCount = 4
            )
        )
        categoryDao.insertCategories(initialCategories)

        // 2. Prepopulate Micro-Entrepreneurs
        val initialArtisans = listOf(
            ArtisanEntity(
                id = 1,
                name = "Ramu Prajapati (Kumhar)",
                craftTitle = "Master Clay Potter & Terracotta Artisan",
                category = "Potters",
                location = "Khurja & Old Delhi Bazaar",
                distanceKm = 1.4,
                bio = "4th generation traditional potter. We specialize in natural red-clay water coolers (Matkas), earthen handi for slow dum cooking, and festival decorative diyas with zero chemical glazing.",
                phone = "+91 98765 43210",
                experienceYears = 28,
                rating = 4.9,
                reviewsCount = 142,
                startingPrice = 120.0,
                isVerified = true,
                isAvailable = true,
                verificationNotes = "Artisan ID #UP-KHU-8821 verified by UP Khadi & Village Industries",
                avatarColorHex = 0xFFC85A32,
                specialization = "Authentic Terracotta Handis & Natural Coolers"
            ),
            ArtisanEntity(
                id = 2,
                name = "Master Aslam Khan",
                craftTitle = "Bespoke Master Tailor & Embroidery Expert",
                category = "Tailors",
                location = "Chowk, Lucknow",
                distanceKm = 2.1,
                bio = "Over 22 years of mastery in tailored sherwanis, bespoke cotton kurtas, intricate Chikankari pattern stitching, and precise suit alterations with doorstep fitting options.",
                phone = "+91 98112 34567",
                experienceYears = 22,
                rating = 4.8,
                reviewsCount = 118,
                startingPrice = 250.0,
                isVerified = true,
                isAvailable = true,
                verificationNotes = "Trade License #LKO-TL-4402 verified",
                avatarColorHex = 0xFF1E3A8A,
                specialization = "Chikan Stitching & Premium Suit Alteration"
            ),
            ArtisanEntity(
                id = 3,
                name = "Mohan Lal Cobbler",
                craftTitle = "Handcrafted Leather Artisan & Shoe Master",
                category = "Cobblers",
                location = "Sadar Bazaar, Kanpur",
                distanceKm = 0.8,
                bio = "Specialist in handmade pure leather Punjabi and Kolhapuri juttis, resole restoration of dress shoes, bag zipper replacement, and bespoke leather belt crafting.",
                phone = "+91 94520 89765",
                experienceYears = 31,
                rating = 4.95,
                reviewsCount = 205,
                startingPrice = 150.0,
                isVerified = true,
                isAvailable = true,
                verificationNotes = "Kanpur Leather Guild Badge #KNP-LG-091",
                avatarColorHex = 0xFF78350F,
                specialization = "Handmade Juttis & Goodyear Welt Shoe Resole"
            ),
            ArtisanEntity(
                id = 4,
                name = "Shanti Devi Weavers",
                craftTitle = "Handloom Silk & Banarasi Artisan",
                category = "Artisans",
                location = "Varanasi Ghats, Uttar Pradesh",
                distanceKm = 3.5,
                bio = "Direct weaver family cooperative creating pure handspun silk dupattas, handloom cotton stoles, and hand-embroidered wall tapestries with traditional zari motifs.",
                phone = "+91 97931 22448",
                experienceYears = 19,
                rating = 4.85,
                reviewsCount = 89,
                startingPrice = 450.0,
                isVerified = true,
                isAvailable = true,
                verificationNotes = "Handloom Mark Registration #HL-VNS-712",
                avatarColorHex = 0xFF831843,
                specialization = "Pure Zari Weaving & Handspun Dupattas"
            ),
            ArtisanEntity(
                id = 5,
                name = "Lakshmi Bai Organics",
                craftTitle = "Homegrown Spices & Traditional Pickle Vendor",
                category = "Vendors",
                location = "Kolhapur Market, Maharashtra",
                distanceKm = 1.9,
                bio = "Family-run small batch producer of stone-ground Kolhapuri masala, sun-dried mango and green chili pickles in cold-pressed mustard oil, and crisp handmade papads.",
                phone = "+91 93240 55123",
                experienceYears = 14,
                rating = 4.9,
                reviewsCount = 167,
                startingPrice = 90.0,
                isVerified = true,
                isAvailable = true,
                verificationNotes = "FSSAI Registration #21523098765432 verified",
                avatarColorHex = 0xFFD97706,
                specialization = "Stone-Ground Spice Blends & Aged Pickles"
            ),
            ArtisanEntity(
                id = 6,
                name = "Devraj Sharma (Kumhar)",
                craftTitle = "Ceramic Studio & Terracotta Garden Planter Maker",
                category = "Potters",
                location = "Sector 14, Jaipur",
                distanceKm = 4.2,
                bio = "Crafting breathable terracotta planters, bonsai pots, and hand-etched garden fountains that keep plant roots aerated and cool during harsh summer months.",
                phone = "+91 96541 22890",
                experienceYears = 16,
                rating = 4.7,
                reviewsCount = 64,
                startingPrice = 180.0,
                isVerified = true,
                isAvailable = false, // Busy / Offline toggle demonstration
                verificationNotes = "Rajasthan Crafts Council #RJ-CC-1120",
                avatarColorHex = 0xFF9A3412,
                specialization = "Terracotta Planters & Decorative Clay Urns"
            ),
            // Unverified / Pending Artisan for Admin Verification Queue
            ArtisanEntity(
                id = 7,
                name = "Gopal Das Bamboo Works",
                craftTitle = "Eco-Friendly Bamboo Artisan & Basket Weaver",
                category = "Artisans",
                location = "Majuli / Guwahati Artisan Cluster",
                distanceKm = 5.0,
                bio = "Newly registered master craftsman seeking marketplace verification. Crafts lightweight bamboo lampshades, fruit baskets, and durable folding stools.",
                phone = "+91 91234 56780",
                experienceYears = 11,
                rating = 5.0,
                reviewsCount = 4,
                startingPrice = 200.0,
                isVerified = false, // Pending verification queue!
                isAvailable = true,
                verificationNotes = "Application submitted: Aadhaar card and sample workshop photos uploaded",
                avatarColorHex = 0xFF166534,
                specialization = "Bamboo Home Decor & Handwoven Baskets"
            ),
            ArtisanEntity(
                id = 8,
                name = "Kavita Saree Restoration",
                craftTitle = "Heritage Fabric Restorer & Darning Specialist",
                category = "Tailors",
                location = "Malleshwaram, Bengaluru",
                distanceKm = 2.8,
                bio = "Specializing in invisible darning (Rafoo), antique zari polishing, silk fall & pico finishing, and blouse alteration for heirloom sarees.",
                phone = "+91 98860 11992",
                experienceYears = 15,
                rating = 4.75,
                reviewsCount = 42,
                startingPrice = 100.0,
                isVerified = false, // Pending verification queue!
                isAvailable = true,
                verificationNotes = "Application pending: Artisan certification from Craft Revival Trust attached",
                avatarColorHex = 0xFF4C1D95,
                specialization = "Invisible Darning & Heritage Zari Polishing"
            )
        )
        artisanDao.insertArtisans(initialArtisans)

        // 3. Prepopulate Services
        val initialServices = listOf(
            // Ramu Prajapati Services
            ServiceEntity(
                id = 1,
                artisanId = 1,
                title = "Custom Clay Cookware Seasoning & Pre-Treatment",
                description = "Traditional rice starch and mustard oil seasoning process to make clay biryani handis 100% flame safe and crack resistant.",
                price = 150.0,
                estimatedTime = "1 day"
            ),
            ServiceEntity(
                id = 2,
                artisanId = 1,
                title = "Handmade Pottery Wheel Workshop (Doorstep/Event)",
                description = "Interactive 2-hour clay shaping session for kids and families with wet clay & mini potter wheel.",
                price = 850.0,
                estimatedTime = "2 hours"
            ),
            ServiceEntity(
                id = 3,
                artisanId = 1,
                title = "Festive Diya Bulk Hand-Painting & Packaging",
                description = "Custom painting and decorative golden border edging for 50+ festival terracotta lamps.",
                price = 300.0,
                estimatedTime = "2-3 days"
            ),

            // Master Aslam Khan Services
            ServiceEntity(
                id = 4,
                artisanId = 2,
                title = "Doorstep Tailoring Measurement & Blouse Stitching",
                description = "Master measurement taking at home with designer blouse cut, princess seam padding, and piping.",
                price = 450.0,
                estimatedTime = "2 days"
            ),
            ServiceEntity(
                id = 5,
                artisanId = 2,
                title = "Suit & Trouser Precision Alteration",
                description = "Waist resizing, length tapering, shoulder slim fit adjustment, and cuff finishing.",
                price = 200.0,
                estimatedTime = "Same day"
            ),
            ServiceEntity(
                id = 6,
                artisanId = 2,
                title = "Bespoke Kurta-Pyjama Tailoring (Traditional Cut)",
                description = "Comfort-fit handmade pure linen or cotton kurta with neat French seams and mother-of-pearl buttons.",
                price = 550.0,
                estimatedTime = "3 days"
            ),

            // Mohan Lal Cobbler Services
            ServiceEntity(
                id = 7,
                artisanId = 3,
                title = "Premium Leather Shoe Sole Replacement (Goodyear Stitch)",
                description = "Complete replacement of worn-out leather or rubber soles with hand-stitched reinforcement.",
                price = 350.0,
                estimatedTime = "1-2 days"
            ),
            ServiceEntity(
                id = 8,
                artisanId = 3,
                title = "Handcrafted Shoe Deep Conditioning & Wax Polish",
                description = "Natural beeswax restoration, scuff removal, edge inking, and water-repellent sealing.",
                price = 180.0,
                estimatedTime = "1 hour"
            ),
            ServiceEntity(
                id = 9,
                artisanId = 3,
                title = "Leather Bag Zipper & Strap Restoration",
                description = "Heavy-duty brass zipper replacement and tensile leather handle reinforcement.",
                price = 220.0,
                estimatedTime = "1 day"
            ),

            // Shanti Devi Services
            ServiceEntity(
                id = 10,
                artisanId = 4,
                title = "Custom Zari Border Weaving on Client Saree",
                description = "Hand-attachment of pure silver-coated metallic thread border to vintage fabrics.",
                price = 700.0,
                estimatedTime = "4 days"
            ),

            // Lakshmi Bai Services
            ServiceEntity(
                id = 11,
                artisanId = 5,
                title = "Custom Spice Grinding & Ratio Formulation",
                description = "Tailor-made roast and grind of authentic regional garam masala based on dietary preferences.",
                price = 160.0,
                estimatedTime = "Same day"
            )
        )
        serviceDao.insertServices(initialServices)

        // 4. Prepopulate Products
        val initialProducts = listOf(
            // Ramu Prajapati Products
            ProductEntity(
                id = 1,
                artisanId = 1,
                title = "Traditional Earthen Slow-Cook Dum Handi (2.5L)",
                description = "Pure untreated red clay pot with snug lid. Retains moisture and infuses food with authentic earthy aroma.",
                price = 320.0,
                stock = 18,
                tag = "Bestseller",
                material = "100% Pure Clay"
            ),
            ProductEntity(
                id = 2,
                artisanId = 1,
                title = "Natural Water-Cooling Clay Surahi with Tap (5L)",
                description = "Naturally cools drinking water by evaporative cooling without electricity. Chemical and lead-free.",
                price = 480.0,
                stock = 12,
                tag = "Eco-Friendly",
                material = "Natural Porous Terracotta"
            ),
            ProductEntity(
                id = 3,
                artisanId = 1,
                title = "Festive Hand-Engraved Terracotta Diyas (Set of 12)",
                description = "Intricately carved traditional oil lamps for Diwali, Pooja, and home entryway decor.",
                price = 140.0,
                stock = 45,
                tag = "Handcrafted",
                material = "Kiln-Fired Clay"
            ),

            // Master Aslam Khan Products
            ProductEntity(
                id = 4,
                artisanId = 2,
                title = "Handmade Pure Lucknowi Chikankari Cotton Kurta",
                description = "Breathable pure muslin cotton kurta with hand-embroidered shadow work along neckline and sleeves.",
                price = 890.0,
                stock = 8,
                tag = "Artisan Made",
                material = "100% Mulmul Cotton"
            ),

            // Mohan Lal Products
            ProductEntity(
                id = 5,
                artisanId = 3,
                title = "Handstitched Genuine Leather Kolhapuri Juttis",
                description = "Custom cushioned insole, durable vegetable-tanned buffalo leather with hand-braided strap.",
                price = 750.0,
                stock = 15,
                tag = "Pure Leather",
                material = "Full-Grain Leather"
            ),
            ProductEntity(
                id = 6,
                artisanId = 3,
                title = "Handmade Solid Brass Buckle Leather Belt",
                description = "Full 3.5mm thick single-piece leather strap with antiqued solid brass buckle built to last decades.",
                price = 450.0,
                stock = 20,
                tag = "Durable",
                material = "Buffalo Hide & Solid Brass"
            ),

            // Shanti Devi Products
            ProductEntity(
                id = 7,
                artisanId = 4,
                title = "Handwoven Banarasi Katan Silk Dupatta",
                description = "Rich jewel-toned handloom silk dupatta adorned with delicate golden zari floral meenakari motifs.",
                price = 1250.0,
                stock = 6,
                tag = "Heritage",
                material = "Pure Banarasi Silk"
            ),

            // Lakshmi Bai Products
            ProductEntity(
                id = 8,
                artisanId = 5,
                title = "Aged Hand-Pounded Mango Pickle (500g Jar)",
                description = "Sun-matured raw Rajapuri mango chunks steeped in cold-pressed mustard oil and fenugreek spices.",
                price = 190.0,
                stock = 30,
                tag = "No Preservatives",
                material = "Raw Mango & Cold-Pressed Oil"
            ),
            ProductEntity(
                id = 9,
                artisanId = 5,
                title = "Stone-Ground Kolhapuri Special Masala (250g)",
                description = "Authentic 32-ingredient slow-roasted spice blend with Byadgi & Lavangi chilies for rich color and depth.",
                price = 160.0,
                stock = 40,
                tag = "Stone-Ground",
                material = "Whole Roasted Spices"
            )
        )
        productDao.insertProducts(initialProducts)

        // 5. Prepopulate Reviews
        val initialReviews = listOf(
            ReviewEntity(
                id = 1,
                artisanId = 1,
                customerName = "Pooja Verma",
                rating = 5,
                comment = "The clay dum handi makes the best chicken biryani and daal! Ramu ji also gave tips on how to season it with rice water.",
                dateText = "3 days ago"
            ),
            ReviewEntity(
                id = 2,
                artisanId = 1,
                customerName = "Arun Mathur",
                rating = 5,
                comment = "Natural cooling clay surahi was delivered in flawless condition. The water tastes sweet and naturally chilled.",
                dateText = "1 week ago"
            ),
            ReviewEntity(
                id = 3,
                artisanId = 2,
                customerName = "Farhan Siddiqui",
                rating = 5,
                comment = "Master Aslam fitted my wedding sherwani perfectly. Prompt delivery and supreme needlework quality.",
                dateText = "4 days ago"
            ),
            ReviewEntity(
                id = 4,
                artisanId = 3,
                customerName = "Col. Rajesh Rao",
                rating = 5,
                comment = "Resoled my 8-year-old riding boots with heavy Goodyear stitching. Mohan Lal is a true craftsman!",
                dateText = "Yesterday"
            ),
            ReviewEntity(
                id = 5,
                artisanId = 5,
                customerName = "Sunita Deshmukh",
                rating = 5,
                comment = "Reminds me of my grandmother's authentic village pickle. No vinegar, pure cold-pressed mustard oil aroma.",
                dateText = "5 days ago"
            )
        )
        reviewDao.insertReviews(initialReviews)

        // 6. Prepopulate Sample Orders & Requests
        val initialOrders = listOf(
            OrderRequestEntity(
                id = 1,
                orderType = "SERVICE",
                customerName = "Ananya Sharma",
                customerPhone = "+91 98765 11223",
                customerAddress = "Flat 402, Royal Palms, Green Park",
                artisanId = 1,
                artisanName = "Ramu Prajapati (Kumhar)",
                itemTitle = "Custom Clay Cookware Seasoning & Pre-Treatment",
                quantity = 1,
                price = 150.0,
                status = "PENDING",
                scheduledDate = "Tomorrow, 11:00 AM",
                customerNotes = "Please season two handis I bought last week.",
                timestamp = System.currentTimeMillis() - 3600000 * 3
            ),
            OrderRequestEntity(
                id = 2,
                orderType = "PRODUCT",
                customerName = "Vikram Malhotra",
                customerPhone = "+91 98765 99887",
                customerAddress = "B-12, Sector 15, Near City Center",
                artisanId = 1,
                artisanName = "Ramu Prajapati (Kumhar)",
                itemTitle = "Traditional Earthen Slow-Cook Dum Handi (2.5L)",
                quantity = 2,
                price = 640.0,
                status = "ACCEPTED",
                scheduledDate = "Standard 2-Day Delivery",
                customerNotes = "Kindly pack with extra straw padding.",
                timestamp = System.currentTimeMillis() - 3600000 * 20
            ),
            OrderRequestEntity(
                id = 3,
                orderType = "SERVICE",
                customerName = "Rohit Sen",
                customerPhone = "+91 99887 77665",
                customerAddress = "House 89, Civil Lines",
                artisanId = 3,
                artisanName = "Mohan Lal Cobbler",
                itemTitle = "Premium Leather Shoe Sole Replacement (Goodyear Stitch)",
                quantity = 1,
                price = 350.0,
                status = "IN_PROGRESS",
                scheduledDate = "Today, 5:00 PM",
                customerNotes = "Need vibram-style heavy tread rubber base.",
                timestamp = System.currentTimeMillis() - 3600000 * 48
            ),
            OrderRequestEntity(
                id = 4,
                orderType = "PRODUCT",
                customerName = "Meera Nair",
                customerPhone = "+91 91234 44321",
                customerAddress = "104, Heritage Enclave",
                artisanId = 5,
                artisanName = "Lakshmi Bai Organics",
                itemTitle = "Stone-Ground Kolhapuri Special Masala (250g)",
                quantity = 2,
                price = 320.0,
                status = "COMPLETED",
                scheduledDate = "Delivered",
                customerNotes = "Mild spice variant preferred.",
                timestamp = System.currentTimeMillis() - 3600000 * 72
            )
        )
        orderDao.insertOrders(initialOrders)
    }
}
