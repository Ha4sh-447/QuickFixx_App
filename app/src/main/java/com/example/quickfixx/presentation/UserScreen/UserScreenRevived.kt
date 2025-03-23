package com.example.quickfixx.presentation.UserScreen

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.GTranslate
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person4
import androidx.compose.material.icons.rounded.PersonPin
import androidx.compose.material.icons.rounded.Reviews
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.quickfixx.R
import com.example.quickfixx.ViewModels.ElectricianViewModel
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.HomePage.BottomNavigationItem
import com.example.quickfixx.presentation.sign_in.SignInState
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import com.example.quickfixx.presentation.sign_in.UserData
import com.example.quickfixx.screens.auth.Electrician.ElectricianScreenState
import com.example.quickfixx.ui.theme.DeepBlue
import com.example.quickfixx.ui.theme.Silver
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
    navController: NavController,
    userData : UserData?,
    onSignOut: () -> Unit,
    user : User?,
    userViewModel: UserViewModel,
    viewModel: SignInViewModel
){
//    val userState by viewModel.state.collectAsState()
    val user = viewModel.state.value.user
    user?.image?.let { Log.d("USERIMAGE", it) }
//    val user = userData?.let { viewModel.getUser(it.email) }
    val username = remember {
        mutableStateOf(user?.name)
    }
    val userEmail = remember {
        mutableStateOf(user?.email)
    }
    val userContact = remember{
        mutableStateOf(user?.contact)
    }
    var editProfileScreen by remember { mutableStateOf(false) }
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
        mutableStateOf(2)
    }
    Scaffold(
        modifier = Modifier
            .background(Color.White),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepBlue)
                    .padding(0.dp),
                title = {
                    Text(
                        text = "Tutor",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                actions = {
                    if (userData != null) {
                        IconButton(
                            onClick = { onSignOut() },
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sign out",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                )
        },

        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.title)
                        },

                        icon = {
                            BadgedBox(
                                badge = {

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

    ) {padding ->

        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .background(Silver)
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .size(140.dp)
            ) {
                Column (
                    modifier = Modifier
                        .padding(10.dp)
                ){
                Row(
                    modifier = Modifier
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
//                    if (userData != null) {
//                        Image(
//                            painter =rememberAsyncImagePainter(userData.profilePictureUrl),
//                            contentDescription = "Profile picture",
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                        )
//                    }
//                    if (userData != null) {
                        // Use File URI scheme for local files
                    val imageUri = if (user?.image?.startsWith("/") == true) {
                        "file://${user.image}"
                    } else {
                        user?.image
                    }
                    Log.d("INFO", "UPDATED URL")
                    Log.d("IMAGE", imageUri.toString())

                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
//                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                    ) {
                        if (user != null) {
                            user.name?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }

                        if (user != null) {
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.labelMedium,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .padding(bottom = 15.dp, top = 10.dp),
                    color = Color.Black
                )
                Row {
                    Text(
                        text = "Edit  ",
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(top = 5.dp)
                            .clickable {
                                editProfileScreen = !editProfileScreen
//                                ProfileScreen()
                            }
                    )
                    Spacer(
                        modifier = Modifier
                            .width(250.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowForwardIos,
                        contentDescription = "Goto Edit Profile",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(top = 5.dp)

                    )
                }
            }
            }
            if(editProfileScreen){
                ModalBottomSheet(
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(400.dp) // Adjusted for better spacing
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Edit Profile",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 10.dp),
                                    style = MaterialTheme.typography.headlineLarge
                                )

                                // Profile Image Picker with local storage functionality
                                val imageUri = rememberSaveable { mutableStateOf(user?.image ?: "") }
                                val context = LocalContext.current
                                val contentResolver = context.contentResolver

                                val painter = rememberAsyncImagePainter(
                                    if (imageUri.value.isEmpty()) R.drawable.ic_search else imageUri.value
                                )

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.GetContent()
                                ) { uri: Uri? ->
                                    uri?.let {
                                        // Save image locally and get the local path
                                        val localPath = saveImageLocally(contentResolver, uri, context)
                                        imageUri.value = localPath
                                    }
                                }

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray) // Fallback background color
                                        .clickable { launcher.launch("image/*") }
                                ) {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Profile Image",
                                        modifier = Modifier
                                            .fillMaxSize() // Ensures the image takes full space
                                            .clip(CircleShape), // Makes sure it remains circular
                                        contentScale = ContentScale.Crop // Ensures full coverage of the circle
                                    )
                                }

                                Text(
                                    text = "Change Profile Picture",
                                    modifier = Modifier.clickable { launcher.launch("image/*") },
                                    color = Color.Blue
                                )

                                Spacer(modifier = Modifier.height(17.dp))

                                // User input fields
                                Column {
                                    username.value?.let {
                                        TextField(
                                            value = it,
                                            modifier = Modifier.fillMaxWidth(),
                                            onValueChange = { newValue -> username.value = newValue },
                                            label = { Text("Name") }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(17.dp))
                                    userEmail.value?.let {
                                        TextField(
                                            value = it,
                                            modifier = Modifier.fillMaxWidth(),
                                            onValueChange = { newValue -> userEmail.value = newValue },
                                            label = { Text("Email") }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(17.dp))
                                    userContact.value?.let {
                                        TextField(
                                            value = it,
                                            modifier = Modifier.fillMaxWidth(),
                                            onValueChange = { newValue -> userContact.value = newValue },
                                            label = { Text("Contact") }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(17.dp))

                                // Save Button
                                Button(
                                    onClick = {
                                        if (user != null) {
                                            user.name = username.value.toString()
                                            user.email = userEmail.value.toString()
                                            user.contact = userContact.value.toString()
                                            user.image = imageUri.value // Update profile image with local path
                                        }
                                        Log.d("User-Updated", user.toString())
                                        if (user != null) {
                                            userViewModel.updateUser(user.id, user)
                                        }
                                        editProfileScreen = !editProfileScreen
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(Color.Black)
                                ) {
                                    Text(text = "Save", color = Color.White)
                                }
                            }
                        }
                    },
                    onDismissRequest = {
                        editProfileScreen = !editProfileScreen
                    }
                )
            }

            TwoCardsBelowUserCard()
            Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .clickable {
                            if (user?.role == "tutor") {
                                // Navigate to update profile screen if user is already a tutor
                                navController.navigate("update_tutor_profile")
                            } else {
                                // Navigate to sign up as tutor screen if user is not a tutor
                                navController.navigate("edit_profile")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person4,
                            contentDescription = "Icon",
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = if (user?.role != "tutor") "Sign up as Tutor" else "Update your profile"
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 65.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
                Divider()
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Reviews,
                            contentDescription = "Icon",
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "Your Reviews"
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 75.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                        )
                    }

            }
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "More",
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.GTranslate,
                            contentDescription = "Icon",
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                        Text(
                            text = "Choose Language"
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 50.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
                Divider()
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Icon",
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "About"
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 105.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,

                        )
                    }
                }
                Divider()
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonPin,
                            contentDescription = "Icon",
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "Contact Info"
                        )
                        Spacer(
                            modifier = Modifier
//                                .padding(vertical = 10.dp)
                                .padding(horizontal = 77.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                             modifier = Modifier
                                 .size(20.dp)
                        )
                    }
//                }
                }
                Divider()
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Logout,
                            contentDescription = "Icon",
                            modifier = Modifier
                                .clickable {
                                    onSignOut()
                                }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(
                            text = "Log out"
                        )
                        Spacer(
                            modifier = Modifier
//                                .padding(vertical = 10.dp)
                                .padding(horizontal = 100.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                             modifier = Modifier
                                 .size(20.dp)
                                 .clickable {
                                     onSignOut()
                                 }
                        )
                    }
//                }
                }
            }
        }

    }
}

// Helper function to save image locally
private fun saveImageLocally(contentResolver: ContentResolver, sourceUri: Uri, context: Context): String {
    try {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "profile_$timestamp.jpg"

        // Create file in app-specific directory
        val imagesDir = File(context.filesDir, "profile_images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }

        val destinationFile = File(imagesDir, filename)

        // Copy the image data
        contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }

        Log.d("ImageSave", "Image saved at: ${destinationFile.absolutePath}")
        return destinationFile.absolutePath
    } catch (e: Exception) {
        Log.e("ImageSave", "Error saving image locally", e)
        return sourceUri.toString() // Fallback to the original URI if saving fails
    }
}

@Composable
fun TwoCardsBelowUserCard() {
    Box(

        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
//            .background(Color.Yellow)
//            .size(290.dp)
//            .height(290.dp)
    ) {
    Row(
//        modifier = Modifier
//            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(15.dp)
                .padding(vertical = 20.dp)
//                .align()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favourites",

                    )
                Text(
                    text = " Favourites",
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(
            modifier = Modifier.padding(end = 27.dp)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(15.dp)
                .padding(vertical = 20.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonPin,
                    contentDescription = "ServiceProvider",
                    )
                Text(
                    text = "Tutor",
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, state: SignInState, electricianViewModel: ElectricianViewModel) {
    val context = LocalContext.current
    val notification = rememberSaveable { mutableStateOf("") }

    if (notification.value.isNotEmpty()) {
        Toast.makeText(context, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    val user = state.user
    var name by rememberSaveable { mutableStateOf(user?.name ?: "") }
    var username by rememberSaveable { mutableStateOf(user?.name ?: "") }
    var bio by rememberSaveable { mutableStateOf("") }
    var availability by rememberSaveable { mutableStateOf("") }
    var fees by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf("") }

    val imageUri = rememberSaveable { mutableStateOf(user?.image ?: "") }

    // Subject dropdown setup
    val subjects = mapOf(
        "Microprocessor" to "microprocessors",
        "Data Structures" to "DS",
        "Mobile Computing" to "MC",
        "Engineering Maths" to "EM"
    )

    var selectedSubject by rememberSaveable { mutableStateOf(subjects.entries.find { it.value == electricianViewModel.title.value }?.key ?: "Select Subject") }
    var expanded by remember { mutableStateOf(false) }

    // Permission launcher for storage access
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
//        val canReadStorage = permissions[Manifest.permission.READ_MEDIA] ?: false
//        val canWriteStorage = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
//
//        if (canReadStorage && canWriteStorage) {
//            // Permission granted, now launch image picker
////            launcher.launch("image/*")
//        } else {
//            // Show error to user
//            notification.value = "Storage permission required to select image"
//        }
    }

    // Image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedImagePath = saveImageToLocalStorage(context, uri)
            if (savedImagePath.isNotEmpty()) {
                imageUri.value = savedImagePath
                Log.d("ProfileImage", "Path saved to imageUri: $savedImagePath")
            } else {
                notification.value = "Failed to save image"
            }
        }
    }

    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_search
        else if (imageUri.value.startsWith("/"))
            File(imageUri.value).toUri()
        else
            imageUri.value
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // For Android 10+ we don't need permission for app-specific storage
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(arrayOf(
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ))
                    }
                }
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "Change Profile Picture",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        launcher.launch("image/*")
                    } else {
//                        permissionLauncher.launch(arrayOf(
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ))
                    }
                },
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Editable Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown menu for subjects
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                label = { Text("Subject") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                subjects.keys.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject) },
                        onClick = {
                            selectedSubject = subject
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = availability,
            onValueChange = { availability = it },
            label = { Text("Availability") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = fees,
            onValueChange = { fees = it },
            label = { Text("Fees") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Save button
        Button(
            onClick = {
                val finalImageUri = if (imageUri.value.startsWith("content://")) {
                    val savedPath = saveImageToLocalStorage(context, Uri.parse(imageUri.value))
                    savedPath
                } else {
                    imageUri.value
                }

                notification.value = "Profile updated"
                if (user != null) {
                    electricianViewModel.saveTutor(
                        name,
                        user.id.toIntOrNull() ?: 0,
                        user.contact,
                        user.email,
                        subject = subjects[selectedSubject] ?: "",
                        fees.toIntOrNull() ?: 0,
                        bio,
                        experience.toIntOrNull() ?: 0,
                        availability,
                        image = finalImageUri,
                    )
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

// Updated image saving function
fun saveImageToLocalStorage(context: Context, uri: Uri): String {
    try {
        // Add debugging log
        Log.d("ProfileImage", "Saving image from URI: $uri")

        // Use app-specific storage instead of external storage
        val profileDirectory = File(context.getExternalFilesDir(null), "QuickFixx_App/profile")
        if (!profileDirectory.exists()) {
            val dirCreated = profileDirectory.mkdirs()
            Log.d("ProfileImage", "Directory created: $dirCreated at ${profileDirectory.absolutePath}")
        }

        // Create file with a unique name
        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(profileDirectory, fileName)
        Log.d("ProfileImage", "Saving to file: ${file.absolutePath}")

        // Copy data from selected image URI to the new file
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val bytesCopied = inputStream.copyTo(outputStream)
                Log.d("ProfileImage", "Bytes copied: $bytesCopied")
            }
        } ?: run {
            Log.e("ProfileImage", "Failed to open input stream from $uri")
            return ""
        }

        Log.d("ProfileImage", "Successfully saved image to: ${file.absolutePath}")
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("ProfileImage", "Error saving image", e)
        return ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTutorProfileScreen(
    navController: NavController,
    state: SignInState,
    electricianViewModel: ElectricianViewModel,
    tutorState: ElectricianScreenState // Assuming you have a TutorData class to hold tutor information
) {
    val context = LocalContext.current
    val notification = rememberSaveable { mutableStateOf("") }

    val tutorData = tutorState.tutor
    if (notification.value.isNotEmpty()) {
        Toast.makeText(context, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    val user = state.user

    // Pre-populate fields with existing tutor data
    var name by rememberSaveable { mutableStateOf(user?.name ?: "") }
    var bio by rememberSaveable { mutableStateOf(tutorData?.bio ?: "") }
    var availability by rememberSaveable { mutableStateOf(tutorData?.availability ?: "") }
    var fees by rememberSaveable { mutableStateOf(tutorData?.fees?.toString() ?: "") }
    var experience by rememberSaveable { mutableStateOf(tutorData?.experience?.toString() ?: "") }

    val imageUri = rememberSaveable { mutableStateOf(tutorData?.image ?: user?.image ?: "") }

    // Subject dropdown setup
    val subjects = mapOf(
        "Microprocessor" to "microprocessors",
        "Data Structures" to "DS",
        "Mobile Computing" to "MC",
        "Engineering Maths" to "EM"
    )

    var selectedSubject by rememberSaveable {
        mutableStateOf(
            subjects.entries.find { it.value == tutorData?.subject }?.key
                ?: subjects.entries.find { it.value == electricianViewModel.title.value }?.key
                ?: "Select Subject"
        )
    }
    var expanded by remember { mutableStateOf(false) }

    // Image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedImagePath = saveImageToLocalStorage(context, uri)
            imageUri.value = savedImagePath
        }
    }

    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty()) R.drawable.ic_search else imageUri.value
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header text
        Text(
            text = "Update Tutor Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile Image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { launcher.launch("image/*") }
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "Change Profile Picture",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { launcher.launch("image/*") },
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Editable Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown menu for subjects
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                label = { Text("Subject") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                subjects.keys.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject) },
                        onClick = {
                            selectedSubject = subject
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = availability,
            onValueChange = { availability = it },
            label = { Text("Availability") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = fees,
            onValueChange = { fees = it },
            label = { Text("Fees") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Update button
        Button(
            onClick = {
                notification.value = "Profile updated successfully"
                navController.popBackStack()
                if (user != null) {
                    electricianViewModel.updateTutor(
                        name = name,
                        userId = user.id.toIntOrNull() ?: 0,
                        contact = user.contact,
                        email = user.email,
                        subject = subjects[selectedSubject] ?: tutorData?.subject ?: "",
                        fees = fees.toIntOrNull() ?: tutorData?.fees ?: 0,
                        bio = bio,
                        experience = experience.toIntOrNull() ?: tutorData?.experience ?: 0,
                        availability = availability,
                        image = imageUri.value,
                        tutorId = tutorData?.id ?: 0  // This is the ID of the tutor record to update
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Profile", color = Color.White)
        }
    }
}

//fun saveImageToLocalStorage(context: Context, uri: Uri): String {
//    try {
//        // Define the directory path
//        val profileDirectory = File(Environment.getExternalStorageDirectory(), "QuickFixx_App/profile")
//        if (!profileDirectory.exists()) profileDirectory.mkdirs()
//
//        // Create file with a unique name
//        val file = File(profileDirectory, "profile_${System.currentTimeMillis()}.jpg")
//
//        // Copy data from selected image URI to the new file
//        context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            FileOutputStream(file).use { outputStream ->
//                inputStream.copyTo(outputStream)
//            }
//        }
//
//        return file.absolutePath
//    } catch (e: IOException) {
//        e.printStackTrace()
//        return ""
//    }
//}

//fun saveImageToLocalStorage(context: Context, uri: Uri): String {
//    try {
//        // Add debugging log
//        Log.d("ProfileImage", "Saving image from URI: $uri")
//
//        // Use app-specific storage instead of external storage
//        val profileDirectory = File(context.getExternalFilesDir(null), "QuickFixx_App/profile")
//        if (!profileDirectory.exists()) {
//            val dirCreated = profileDirectory.mkdirs()
//            Log.d("ProfileImage", "Directory created: $dirCreated at ${profileDirectory.absolutePath}")
//        }
//
//        // Create file with a unique name
//        val fileName = "profile_${System.currentTimeMillis()}.jpg"
//        val file = File(profileDirectory, fileName)
//        Log.d("ProfileImage", "Saving to file: ${file.absolutePath}")
//
//        // Copy data from selected image URI to the new file
//        context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            FileOutputStream(file).use { outputStream ->
//                val bytesCopied = inputStream.copyTo(outputStream)
//                Log.d("ProfileImage", "Bytes copied: $bytesCopied")
//            }
//        } ?: run {
//            Log.e("ProfileImage", "Failed to open input stream from $uri")
//            return ""
//        }
//
//        Log.d("ProfileImage", "Successfully saved image to: ${file.absolutePath}")
//        return file.absolutePath
//    } catch (e: Exception) {
//        Log.e("ProfileImage", "Error saving image", e)
//        return ""
//    }
//}