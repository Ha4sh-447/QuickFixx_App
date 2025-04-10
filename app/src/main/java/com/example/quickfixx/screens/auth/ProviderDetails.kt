@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.quickfixx.screens.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.quickfixx.R
import com.example.quickfixx.R.drawable.baseline_star_outline_24
import com.example.quickfixx.ViewModels.ElectricianViewModel
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.navigation.Screens
import com.example.quickfixx.screens.auth.Electrician.ElectricianScreenState
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import com.example.quickfixx.presentation.HomePage.BottomNavigationItem
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.heightIn
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetails(navController: NavController,
                    tutor: ElectricianScreenState,
                    evm: ElectricianViewModel,
                    onBook : ()->Unit) {

    val selectedTutor = evm.state.collectAsStateWithLifecycle().value.tutor
    if (selectedTutor != null) {
        Log.d("INFO", "PROVIDER DETAILS"+selectedTutor.name)
    }
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
//            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = "home"
        ),
        BottomNavigationItem(
            title = "Messages",
            selectedIcon = Icons.Filled.Notifications,
//            unselectedIcon = Icons.Outlined.Notifications,
            hasNews = false,
            route = "messages"
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
            hasNews = true,
            route = "user_profile"
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }   
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = "Tutor",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screens.CustomerSupport.route)
                        },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
//                        modifier = Modifier
////                            .height(20.dp)
//                            .padding(3.dp),
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route)
                            },
                            label = {
                                Text(text = item.title)
                            },
//                        alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
//                                        Badge()
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(30.dp),
                                        imageVector = item.selectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = it.calculateTopPadding())
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Profile",
                    Modifier
                        .padding(top = 5.dp, bottom = 9.dp, start = 9.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentHeight(),
                    fontSize = 24.sp,  // Set the desired font size here
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Left,
                    textDecoration = TextDecoration.Underline
                )
                if (selectedTutor != null) {
                    CardElevation1(selectedTutor.name, selectedTutor.rating, selectedTutor, navController, electricianViewModel = evm, onBook)
                }
                if (selectedTutor != null) {
                    Summary(selectedTutor.name, selectedTutor.rating, selectedTutor.contact, selectedTutor.fees, selectedTutor.experience, true, navController)
                }
                ReviewsSection(
                    reviews = listOf(
                        Review(username = "Sarang", rating = 5, comment = "Great service!"),
                        Review(username = "Akshat", rating = 4, comment = "Good experience"),
                        
                    )
                )
            }

        }
    }
}

@Composable
fun Summary(
    name: String,
    rating: Float,
    contact: String,
    fees: Int,
    yearsOfExperience: Int,
    verified: Boolean,
    navController: NavController
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFDAE1E7),
        modifier = Modifier
            .height(310.dp)
            .padding(10.dp)
            .padding(start = 3.dp)
            .fillMaxWidth(),
        shadowElevation = 10.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .size(width = 500.dp, height = 280.dp)
                            .padding(start = 16.dp),
                        color = Color(0xFFD1D5E1)
                    ) {
                        Column(
                            Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = name,
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                AssistChip(
                                    onClick = { Log.d("Assist chip", "hello world") },
                                    label = { Text("Verified") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Localized description",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    }
                                )

                                AssistChip(
                                    onClick = { Log.d("Assist chip", "hello world") },
                                    label = { Text("Great Value") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Localized description",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Column(
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                SummaryItem(Icons.Default.ThumbUp,"Fees: $fees")
                                SummaryItem(Icons.Default.RateReview,"Rating: $rating")
                                SummaryItem(Icons.Default.Timer,"Experience: $yearsOfExperience years")
                                SummaryItem(Icons.Default.ThumbUp,"Verification: ${if (verified) "Verified" else "Not Verified"}")
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                        }
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

@Composable
fun SummaryItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun CardElevation1(name: String, rating: Float, tutor: Tutor, navController: NavController, electricianViewModel: ElectricianViewModel, onBook: () -> Unit) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFDAE1E7),
        modifier = Modifier
            .height(310.dp)
            .padding(10.dp)
            .padding(start = 3.dp)
            .fillMaxWidth(),
        shadowElevation = 10.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp)
                ) {
                    val imageUrl = tutor.image ?: "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?q=80&w=1965&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.size(128.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .size(width = 500.dp, height = 280.dp)
                            .padding(start = 16.dp),
                        color = Color(0xFFD1D5E1)
                    ) {
                        Column(
                            Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = name,
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 1.dp)
                            ) {
//                                AssistChip(
//                                    onClick = { Log.d("Assist chip", "hello world") },
//                                    label = { Text("Verified") },
//                                    leadingIcon = {
//                                        Icon(
//                                            Icons.Filled.Check,
//                                            contentDescription = "Localized description",
//                                            Modifier.size(AssistChipDefaults.IconSize)
//                                        )
//                                    }
//                                )

//                                AssistChip(
//                                    onClick = { Log.d("Assist chip", "hello world") },
//                                    label = { Text("Great Value") },
//                                    leadingIcon = {
//                                        Icon(
//                                            Icons.Filled.Check,
//                                            contentDescription = "Localized description",
//                                            Modifier.size(AssistChipDefaults.IconSize)
//                                        )
//                                    },
//                                    modifier = Modifier.padding(start = 8.dp)
//                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = buildAnnotatedString {

                                        if (rating >= 4) {
                                            withStyle(style = SpanStyle(color = Color.Black)) {
                                                append("Excellent: $rating")
                                            }
                                        } else if (rating > 3) {
                                            withStyle(style = SpanStyle(color = Color.Black)) {
                                                append("Good: $rating")
                                            }
                                        } else {
                                            withStyle(style = SpanStyle(color = Color.Black)) {
                                                append("Average: $rating")
                                            }
                                        }
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                repeat(5) { index ->
                                    val starColor =
                                        if (index < rating) Color.Yellow else Color.Gray

                                    Icon(
                                        painter = painterResource(id = baseline_star_outline_24),
                                        tint = starColor,
                                        contentDescription = null
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

//                            // Add description text
//                            Text(
//                                text = "This is a description of the service provider...",
//                                fontSize = 12.sp,
//                                color = Color.Gray,
//                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
//                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Book button with QR code functionality
                                BookButtonWithQR(navController, tutor=tutor, electricianViewModel = electricianViewModel, onBook = onBook)

                                // Call button
                                OutlinedButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color.Blue),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.Blue,
                                        containerColor = Color.White,
                                    ),
                                    onClick = {
                                        val phoneNumber = "7045432201"
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Text(
                                        text = "Call",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ReviewCard(username: String, rating: Int, comment: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "User: $username",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rating: $rating",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = Color(0xFFF6B266),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Reviews",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (reviews.isNotEmpty()) {
            reviews.forEach { review ->
                ReviewCard(username = review.username, rating = review.rating, comment = review.comment)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(
                text = "No reviews yet.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

data class Review(val username: String, val rating: Int, val comment: String)

@Composable
fun BookButtonWithQR(
    navController: NavController,
    onBook: () -> Unit,
    electricianViewModel: ElectricianViewModel,
    tutor: Tutor  // Only keep tutor parameter to access all details
) {
    var showQRCode by remember { mutableStateOf(false) }
    var locationText by remember { mutableStateOf("Fetching location...") }
    val context = LocalContext.current

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Handle permission denied
            locationText = "Location permission denied"
        }
    }

    // Generate a unique booking reference
    val bookingReference = remember {
        // Simple implementation - in production use UUID or another robust method
        "BK-" + System.currentTimeMillis().toString().takeLast(8)
    }

    // Get current datetime formatted nicely
    val currentDateTime = remember {
        val formatter = SimpleDateFormat("MMMM d, yyyy - h:mm a", Locale.getDefault())
        formatter.format(Date())
    }

    // Create location client outside of LaunchedEffect
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Request permission and get location
    LaunchedEffect(Unit) {
        val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = ContextCompat.checkSelfPermission(context, locationPermission) ==
                PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            // Request permission
            locationPermissionLauncher.launch(locationPermission)
        } else {
            // We have permission, get location
            getLocation(context, fusedLocationClient) { location ->
                locationText = location
            }
        }
    }

    val subjectStr = when {
        tutor.subject == null || tutor.subject.isEmpty() -> "General Tutoring"
        else -> {
            tutor.subject
        }
    }

    tutor.earnings += tutor.fees

    // Compile all booking information
    val bookingInfo = """
        TUTOR BOOKING CONFIRMATION
        -------------------------
        Tutor: ${tutor.name}
        Subject: $subjectStr
        Date & Time: $currentDateTime
        Fees: ${tutor.fees}
        Location: $locationText
        -------------------------
        Booking Ref: $bookingReference
        Contact: ${tutor.contact ?: "N/A"}
        Student: ${context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("user_name", "Student") ?: "Student"}
    """.trimIndent()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White
            ),
            onClick = {
                // Check if we have location permission now before showing QR
                val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
                val hasPermission = ContextCompat.checkSelfPermission(context, locationPermission) ==
                        PackageManager.PERMISSION_GRANTED
//                Enter function to update tutor here
                tutor.earnings += tutor.fees
                electricianViewModel.updateTutor(
                    name = tutor.name,
//                    userId = user.id.toIntOrNull() ?: 0,
                    userId = tutor.uid,
                    contact = tutor.contact,
                    email = tutor.email,
                    subject =  tutor.subject,
                    fees = tutor.fees,
                    bio = tutor.bio,
                    experience = tutor.experience,
                    availability = tutor.availability,
                    image = tutor.image,
                    rating = tutor.rating,
                    earnings = tutor.earnings,
                    tutorId = tutor.id  // This is the ID of the tutor record to update
                )
                Log.d("INFO", "UPDATED TUTOR INFO")

                if (hasPermission && locationText == "Fetching location...") {
                    // Try to get location one more time before showing QR
                    getLocation(context, fusedLocationClient) { location ->
                        locationText = location
                        onBook()
//                        electricianViewModel.updateTutor(tutor)
                        showQRCode = true
                    }
                } else {
                    onBook()
//                    electricianViewModel.updateSelectedTutor(tutor)
                    showQRCode = true
                }
            }
        ) {
            Text(
                text = "Book",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    if (showQRCode) {
        QRCodeDialog(
            qrContent = bookingInfo,
            bookingReference = bookingReference,
            tutorName = tutor.name,
            subject = subjectStr,  // FIXED: Now using a guaranteed String type
            dateTime = currentDateTime,
            location = locationText,
            onDismiss = {
                showQRCode = false
                navController.navigate("profile")
            },
            onShare = { bitmap ->
                shareQRCode(context, bitmap, "Booking Confirmation: $bookingReference")
            }
        )
    }
}

// Helper function to get location
private fun getLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (String) -> Unit
) {
    try {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1  // Just get one update
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)  // Stop updates after getting location

                val location = result.lastLocation
                if (location != null) {
                    // Use Geocoder to get address from coordinates
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Use the new API for Android 13+
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                                if (addresses.isNotEmpty()) {
                                    val address = addresses[0]
                                    onLocationResult(address.getAddressLine(0) ?:
                                    "${location.latitude}, ${location.longitude}")
                                } else {
                                    onLocationResult("${location.latitude}, ${location.longitude}")
                                }
                            }
                        } else {
                            // Use the older API for Android 12 and below
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val address = addresses[0]
                                onLocationResult(address.getAddressLine(0) ?:
                                "${location.latitude}, ${location.longitude}")
                            } else {
                                onLocationResult("${location.latitude}, ${location.longitude}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Location", "Geocoder error: ${e.message}")
                        onLocationResult("${location.latitude}, ${location.longitude}")
                    }
                } else {
                    onLocationResult("Location unavailable")
                }
            }
        }

        // Request location updates
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        // Also try lastLocation as a fallback
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Use Geocoder to get address from coordinates
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Already handled in callback, this is just a fallback
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0]
                            onLocationResult(address.getAddressLine(0) ?:
                            "${location.latitude}, ${location.longitude}")
                        } else {
                            // This won't override the callback if it works
                        }
                    }
                } catch (e: Exception) {
                    // Ignore errors in fallback
                }
            }
        }
    } catch (e: SecurityException) {
        Log.e("Location", "Security exception: ${e.message}")
        onLocationResult("Location access denied")
    } catch (e: Exception) {
        Log.e("Location", "Error: ${e.message}")
        onLocationResult("Error accessing location")
    }
}
@Composable
fun QRCodeDialog(
    qrContent: String,
    bookingReference: String,
    tutorName: String,
    subject: String,
    dateTime: String,
    location: String,
    onDismiss: () -> Unit,
    onShare: (Bitmap) -> Unit
) {
    val qrBitmap = remember(qrContent) {
        generateQRCode(content = qrContent, size = 800)
    }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Booking Confirmation",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Booking details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Booking Reference: $bookingReference",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow(icon = Icons.Default.Person, label = "Tutor", value = tutorName)
                    DetailRow(icon = Icons.Default.Book, label = "Subject", value = subject)
                    DetailRow(icon = Icons.Default.DateRange, label = "Date & Time", value = dateTime)
                    DetailRow(icon = Icons.Default.LocationOn, label = "Location", value = location)
                }

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                qrBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(240.dp)
                            .background(Color.White)
                            .padding(8.dp)
                    )

                    Text(
                        text = "Show this QR code to your tutor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Close")
                    }

                    Button(
                        onClick = { qrBitmap?.let { onShare(it) } },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                modifier = Modifier.size(18.dp)
                            )
                            Text("Share")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


fun generateQRCode(content: String, size: Int): Bitmap? {
    try {
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.MARGIN] = 1 // Make quiet zone smaller

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) BLACK else WHITE)
            }
        }

        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

private fun shareQRCode(context: android.content.Context, bitmap: Bitmap, title: String) {
    try {
        // Save bitmap to cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "booking_qr.png")

        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
        }

        // Get URI via FileProvider
        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        // Create share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "Here's my tutor booking confirmation.")
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Booking Confirmation"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error sharing QR code: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
