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
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.HomePage.HomeVM
import com.example.quickfixx.presentation.Messages
import com.example.quickfixx.presentation.UserScreen.UserCard
import com.example.quickfixx.presentation.UserScreen.UserViewModel
import com.example.quickfixx.presentation.sign_in.GoogleAuthUiClient
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import com.example.quickfixx.screens.auth.Electrician.ElectricianData
import com.example.quickfixx.screens.auth.ProviderDetails
import com.example.quickfixx.screens.auth.SignUpScreen
import com.example.quickfixx.screens.auth.WelcomePageScreen
import com.example.quickfixx.ui.theme.QuickFixxTheme
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
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
//            val generativeModel = GenerativeModel(
//
//                modelName = "gemini-pro-vision",
//
////                apiKey = BuildConfig.GOOGLE_API_KEY
//            )

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
                var user: User? = null

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    val navController = rememberNavController()
                    LaunchedEffect(key1 = Unit) {
                        val authUser = googleAuthUiClient.getSignedInUser()
                        authUser?.email?.let { Log.d("AUTH USER", it) }
                        if (authUser!=null){
                            Log.d("STEP", authUser.email)
                            user = viewModel.getUserByEmail(authUser.email)
                            Log.d("STEP", "---------------------")
                            if (user ==null){
                                navController.navigate("sign_up")
                                Toast.makeText(
                                    applicationContext,
                                    "Sign in to continue",
                                    Toast.LENGTH_LONG
                                ).show()
                            }else{
                                navController.navigate("home"){
                                    popUpTo(0)
                                }
                            }
                        }else{
                            navController.navigate("sign_up"){
                                popUpTo(0)
                            }
                        }
                    }
                    NavHost(navController = navController, startDestination = "welcome") {
                        composable("welcome"){
                            WelcomePageScreen(navController = navController)
                        }

//                       composable("electricians"){
//                           val EviewModel : ElectricianViewModel= hiltViewModel()
//                           ElectricianData(navController = navController, EviewModel)
//                       }
                        composable("electricians/{tabIndex}") { backStackEntry ->
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val tabIndex = arguments.getString("tabIndex")?.toIntOrNull() ?: 0
                            val EviewModel: ElectricianViewModel = hiltViewModel()
                            val homeVM: HomeVM = hiltViewModel()
                            val title by homeVM.title.collectAsStateWithLifecycle()
                            ElectricianData(navController = navController, viewModel = EviewModel, homeVM = homeVM, tabIndex = tabIndex, title=title)
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
                        composable("profile"){
                            ProviderDetails(navController = navController,
                                onBook = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Booking in process",
                                        Toast.LENGTH_LONG
                                    ).show()

                                })
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
                                if(state.isSignInSuccessful) {
                                    state.userData?.username?.let { it1 ->
                                        Log.d("SINGIN SUCC",
                                            it1
                                        )
                                    }
                                    val signedInUser = googleAuthUiClient.getSignedInUser()
//                                    Log.d("SIGNED IN USER", signedInUser.username)
                                    if (signedInUser != null) {
                                         user = viewModel.repo.getByEmail(signedInUser.email)
                                        viewModel.getUser(signedInUser.email)
                                        if (user != null) {
                                            Log.d("AFTER SIGNED IN USER", user!!.name)
                                            state.user = user // Update the user in the state
                                            Toast.makeText(
                                                applicationContext,
                                                "Sign in successful",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.navigate("home") {
                                                popUpTo(0)
                                            }
                                            viewModel.resetState()
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