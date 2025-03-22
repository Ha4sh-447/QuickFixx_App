package com.example.quickfixx

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickfixx.ViewModels.ElectricianViewModel
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.HomePage.HomeVM
import com.example.quickfixx.presentation.Messages
import com.example.quickfixx.presentation.UserScreen.ProfileScreen
import com.example.quickfixx.presentation.UserScreen.UpdateTutorProfileScreen
import com.example.quickfixx.presentation.UserScreen.UserCard
import com.example.quickfixx.presentation.UserScreen.UserViewModel
import com.example.quickfixx.presentation.sign_in.GoogleAuthUiClient
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import com.example.quickfixx.screens.auth.Electrician.ElectricianData
import com.example.quickfixx.screens.auth.ProviderDetails
import com.example.quickfixx.screens.auth.SignUpScreen
import com.example.quickfixx.screens.auth.WelcomePageScreen
import com.example.quickfixx.ui.theme.QuickFixxTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        Log.d("FIREBASE MESSAGING", "STARTED")
        // Get the device token
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM Token", token!!)
                    // Send this token to your server
                } else {
                    Log.e("FCM Token", "Error getting token", task.exception)
                }
            }
        requestNotificationPermission()

        setContent {
            QuickFixxTheme {

                val viewModel = hiltViewModel<SignInViewModel>()
                val homeVM = hiltViewModel<HomeVM>()
                val eVM = hiltViewModel<ElectricianViewModel>()
                var user: User? = null

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(key1 = Unit) {
                        val authUser = googleAuthUiClient.getSignedInUser()

                        if (authUser != null) {
                            Log.d("STEP", authUser.email)

                            // Fetch user from database (with retry)
                            var user = viewModel.getUserByEmail(authUser.email)

                            if (user == null) {
                                // Instead of navigating immediately, wait & retry
                                delay(1000) // Wait for the user to be added to DB
                                user = viewModel.getUserByEmail(authUser.email)
                            }

                            if (user == null) {
                                // If user is still not found, go to sign up
                                navController.navigate("sign_up")
                                Toast.makeText(applicationContext, "Sign in to continue", Toast.LENGTH_LONG).show()
                            } else {
                                // If user exists, go to home
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        } else {
                            navController.navigate("sign_up") {
                                popUpTo(0)
                            }
                        }
                    }

                    NavHost(navController = navController, startDestination = "welcome") {
                        composable("welcome"){
                            WelcomePageScreen(navController = navController)
                        }

                        composable("edit_profile"){
//                            val electricianViewModel: ElectricianViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            ProfileScreen(navController=navController, state = state, electricianViewModel = eVM)
                        }
                        composable("update_tutor_profile") {
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val tutorData by eVM.state.collectAsStateWithLifecycle()
                            UpdateTutorProfileScreen(
                                navController = navController,
                                state = state,
                                electricianViewModel = eVM,
                                tutorState = tutorData
                            )
                        }

                        composable("electricians/{tabIndex}") { backStackEntry ->
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val tabIndex = arguments.getString("tabIndex")?.toIntOrNull() ?: 0
                            val homeVM: HomeVM = hiltViewModel()
                            val title by homeVM.title.collectAsStateWithLifecycle()
                            ElectricianData(navController = navController, viewModel = eVM, homeVM = homeVM, tabIndex = tabIndex, title=title)
                        }
                        composable("user_profile"){
//                            ProfileScreen(onGoBack = { })
                            val userViewModel: UserViewModel = hiltViewModel()
                            UserCard(navController = navController,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("sign_up"){
                                            popUpTo(0)
                                        }

                                    }
                                },
                                user,
                                userViewModel,
                                viewModel
                                )
//                            UserDetails(navController = navController)
                        }

                        composable("messages"){
                            Messages(navController = navController)
                        }

                        composable("profile") {
                            val homeVM: HomeVM = hiltViewModel()
                            val title by homeVM.title.collectAsStateWithLifecycle()

//                            val EviewModel: ElectricianViewModel = hiltViewModel()
                            val tutor by eVM.state.collectAsStateWithLifecycle()

                            ProviderDetails(
                                navController = navController,
                                tutor = tutor,
                                evm = eVM,
                                onBook = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Booking in process",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }

                        composable("sign_up"){
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch,
                                                state
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    val signedInUser = googleAuthUiClient.getSignedInUser()

                                    if (signedInUser != null) {
                                        launch {
                                            var user = viewModel.getUserByEmail(signedInUser.email)

                                            if (user == null) {
                                                val newUser = User(
                                                    id = "", // Auto-generated ID
                                                    name = signedInUser.username ?: "Unknown",
                                                    email = signedInUser.email,
                                                    contact = "",
                                                    image = signedInUser.profilePictureUrl ?: "",
                                                    role = "User",
                                                    password = "123456",
                                                )

                                                viewModel.saveUser(
                                                    name = newUser.name,
                                                    email = newUser.email,
                                                    contact = newUser.contact,
                                                    image = newUser.image,
                                                    role = newUser.role,
                                                    password = newUser.password
                                                )

                                                delay(500) // Ensure database has time to process the new user

                                                user = viewModel.getUserByEmail(signedInUser.email) // Retry fetching
                                            }

                                            if (user != null) {
                                                state.user = user
                                                Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_LONG).show()

                                                navController.navigate("home") {
                                                    popUpTo(0)
                                                }

//                                                viewModel.resetState()
                                            }
                                        }
                                    }
                                }
                            }

                            SignUpScreen(
                                state,
                                navController = navController,
                                viewModel,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
//                                        googleAuthUiClient.getSignedInUser()
//                                            ?.let { it1 -> viewModel.getUser(it1.email) }
                                    }

                                },
                                googleAuthClient = googleAuthUiClient
                            )
                        }

                        composable("home"){
//                            val homeVM = hiltViewModel<HomeVM>()
                            val homeState by homeVM.homeState.collectAsStateWithLifecycle()
                            val title by homeVM.title.collectAsStateWithLifecycle()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            com.example.quickfixx.presentation.HomePage.HomePage(
                                navController = navController,
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                HViewModel = viewModel,
                                homeVM = homeVM,
                                onTitleChange = {newTitle -> homeVM.currTitle(newTitle)},
                                title = title,
                                homeState = homeState,
                            ) {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed out",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("sign_up") {
                                        popUpTo(0)
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
    }
    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}