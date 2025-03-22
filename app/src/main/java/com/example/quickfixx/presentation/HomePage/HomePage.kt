package com.example.quickfixx.presentation.HomePage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Bathroom
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Carpenter
import androidx.compose.material.icons.rounded.ElectricalServices
import androidx.compose.material.icons.rounded.House
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickfixx.R
import com.example.quickfixx.ui.theme.AquaBlue
import com.example.quickfixx.ui.theme.DeepBlue
import com.example.quickfixx.ui.theme.Silver
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.sign_in.SignInState
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import com.example.quickfixx.presentation.sign_in.UserData
import kotlinx.coroutines.delay

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
//    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val route: String
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    userData: UserData?,
    HViewModel: SignInViewModel,
    homeVM: HomeVM,
    onTitleChange:(String) ->Unit,
    state: SignInState,
    title: String,
    homeState: HomePageState,
//    user : User?,
    onSignOut: () -> Unit) {

    val user = state.user

    user?.let { Log.d("HOME PAGE USER", it.name) }

    if(userData!=null){
        userData.username?.let { Log.d("HOME PAGE USERDATA", it) }
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

    Scaffold(
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
                    // Show sign-out button only if user is logged in
                    if (user != null || userData != null) {
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
    ) {padding ->
    Box(
        modifier = Modifier
            .background(AquaBlue)
            .fillMaxSize()
    ) {

        Column {

            Spacer(modifier = Modifier.size(10.dp))
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(30.dp)
                    .height(100.dp)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
//                        .padding(horizontal = 9.dp)
                        .padding(top = 29.dp)
                        .align(Alignment.CenterStart)
                ) {

                    if (userData != null) {
                        Text(
                            text = "Hello " +userData.username ,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Text(
                        text = "Welcome to Tutor",
                        modifier = Modifier
                            .padding(top = 15.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
            ServicesSection(
                services = listOf(
                    Services(
                        title = "Microprocessors",
                        icon = Icons.Rounded.ElectricalServices,
                        description = "Electrician icon",
                        image = R.drawable.img,
                        index = 0,
                    ),
                    Services(
                        title = "Data Structures",
                        icon = Icons.Rounded.Carpenter,
                        description = "Carpenter icon",
                        image = R.drawable.dssa,
                        index = 1,
                    ),
                    Services(
                        title = "Mobile Computing",
                        icon = Icons.Rounded.Plumbing,
                        description = "Plumber icon",
                        image = R.drawable.mobile_computing,
                        index = 2,
                    ),
                    Services(
                        title = "Engineering Maths",
                        icon = Icons.Rounded.House,
                        description = "Housekeeping icon",
                        image = R.drawable.maths,
                        index = 3,
                    )
                ), navController, homeVM, onTitleChange
            )
            Spacer(modifier = Modifier
                .size(20.dp)
                .background(color = Color.Green)
            )
        }
    }
}
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ServicesSection(services: List<Services>, navController: NavController, homeVM: HomeVM, onTitleChange: (String) -> Unit,) {
    homeVM.homeState.value.service?.let { Log.d("INFO", it.title) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
            .background(Silver)
            .padding(top = 5.dp)
            .padding(bottom = 50.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(15.dp),
                fontWeight = FontWeight.ExtraBold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 50.dp),
                modifier = Modifier.height(560.dp),
                userScrollEnabled = true
            ) {
                items(services.size) {
                    ServiceItem(homeVM, service = services[it], navController = navController, onTitleChange = onTitleChange)
                }
            }

        }
//    }
    }
}

@Composable
fun ServiceItem(
    homeVM: HomeVM,
    onTitleChange: (String) -> Unit,
    service: Services,
    navController: NavController
){
    Box(
        modifier = Modifier
            .padding(7.5.dp)
            .clip(RoundedCornerShape(10.dp))
            .size(290.dp)
            .background(Color.White)
            .clickable {
                Log.d("INFO from service-item", service.title)
                homeVM.currService(service, service.title)
                onTitleChange(service.title)
                navController.navigate("electricians/0")
            }
    ){

        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.White)
        ){

            Image(
                painter = painterResource(id = service.image),
                contentDescription = service.description,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .align(Alignment.TopStart)
            )
            Text(
                text = service.title,
                style = MaterialTheme.typography.headlineLarge,
                lineHeight = 40.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(top = 10.dp)
                    .padding(start = 20.dp),
                color = Color.Black
            )
        }
    }
}