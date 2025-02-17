package com.example.quickfixx.presentation.UserScreen

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.quickfixx.R
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.HomePage.BottomNavigationItem
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import com.example.quickfixx.presentation.sign_in.UserData
import com.example.quickfixx.ui.theme.DeepBlue
import com.example.quickfixx.ui.theme.Silver

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
                    if (userData != null) {
                        Image(
                            painter =rememberAsyncImagePainter(userData.profilePictureUrl),
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }
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
                            .width(220.dp)
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
                                .height(300.dp) // Adjust height as needed
                                .padding(16.dp)
                        ) {
                            // Your input form UI here
                            Column {
                                Text(
                                    text = "Edit Profile",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 10.dp),
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                Box(
                                    modifier = Modifier
                                        .background(Silver)
                                ) {
                                    Column (
                                        modifier = Modifier
                                            .background(Silver)
                                    ){
                                    username.value?.let {
                                        TextField(
                                            value = it, // Replace with your state variable
                                            onValueChange = { newValue ->
                                                username.value = newValue
                                            }, // Update your state on change
                                            label = { Text("Name") }
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(17.dp)
                                    )
                                    userEmail.value?.let {
                                        TextField(
                                            value = it, // Replace with your state variable
                                            onValueChange = { newValue ->
                                                userEmail.value = newValue
                                            }, // Update your state on change
                                            label = { Text("Email") }
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(17.dp)
                                    )
                                    userContact.value?.let {
                                        TextField(
                                            value = it, // Replace with your state variable
                                            onValueChange = { newValue ->
                                                userContact.value = newValue
                                            }, // Update your state on change
                                            label = { Text("Contact") }
                                        )
                                    }
                                }
                            }
                                Spacer(
                                    modifier =Modifier
                                        .height(17.dp)
                                )
                                Button(
                                    onClick = {
                                        if (user != null) {
                                            user.name = username.value.toString()
                                            user.email = userEmail.value.toString()
                                            user.contact = userContact.value.toString()
                                        }
                                        Log.d("User-Updated", user.toString())
                                        if (user != null) {
                                            userViewModel.updateUser(user.id,user)
                                        }
                                        editProfileScreen=!editProfileScreen

                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally),
//                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                                    colors = ButtonDefaults.buttonColors(Color.Black)
                                ) {
                                    Text(
                                        text = "Save",
                                        color = Color.White
                                        )
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
                            .clickable {
                                navController.navigate("edit_profile")
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
                            text = "Sign up as Tutor"
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
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
                .padding(vertical = 10.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonPin,
                    contentDescription = "ServiceProvider",

                    )
                Text(
                    text = "Service Provider",
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
}

@Composable
fun ProfileScreen(navController: NavController) {

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var name by rememberSaveable { mutableStateOf("default name") }
    var username by rememberSaveable { mutableStateOf("default username") }
    var bio by rememberSaveable { mutableStateOf("default bio") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Cancel",
                modifier = Modifier.clickable { notification.value = "Cancelled" })
            Text(text = "Save",
                modifier = Modifier.clickable { notification.value = "Profile updated" })
        }

        ProfileImage()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
//                colors = TextField(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray,
                    focusedTextColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray,
                    focusedTextColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Bio", modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
            )
            TextField(
                value = bio,
                onValueChange = { bio = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray,
                    focusedTextColor = Color.Black
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }
    }
}

@Composable
fun ProfileImage() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_search
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Change profile picture")
    }
}