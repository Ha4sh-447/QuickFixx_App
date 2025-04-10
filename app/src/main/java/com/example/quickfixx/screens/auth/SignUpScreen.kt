package com.example.quickfixx.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.quickfixx.domain.model.User
import com.example.quickfixx.presentation.sign_in.GoogleAuthUiClient
import com.example.quickfixx.presentation.sign_in.SignInState
import com.example.quickfixx.presentation.sign_in.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    state: SignInState,
    navController: NavController,
    viewModel: SignInViewModel,
    onSignInClick: () -> Unit,
    googleAuthClient: GoogleAuthUiClient
) {
    var isLoginScreen by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoginScreen) {
            LoginScreen(
                state = state,
                navController = navController,
                viewModel = viewModel,
                onSignInClick = onSignInClick,
                googleAuthClient = googleAuthClient,
//                onSwitchToSignUp = { isLoginScreen = false }
            )
        } else {
            SignUpScreen(
                state = state,
                navController = navController,
                viewModel = viewModel,
                onSignInClick = onSignInClick,
                googleAuthClient = googleAuthClient,
//                onSwitchToLogin = { isLoginScreen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: SignInState,
    navController: NavController,
    viewModel: SignInViewModel,
    onSignInClick: () -> Unit,
    googleAuthClient: GoogleAuthUiClient,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = googleAuthClient.getAut()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // Observe changes in state to handle navigation or error messages
    LaunchedEffect(state.user) {
        state.user?.let {
            // Successfully retrieved user, navigate to home
            navController.navigate("home") {
                popUpTo(0)
            }
        }
    }

    // Handle sign-in errors
    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun validateLoginFields(): Boolean {
        emailError = if (email.isEmpty()) "Email is required" else ""
        passwordError = if (password.isEmpty()) "Password is required" else ""
        return emailError.isEmpty() && passwordError.isEmpty()
    }

    fun loginUser(email: String, password: String) {
        if (!validateLoginFields()) return

        coroutineScope.launch {
            try {
                // First, try to retrieve user by email
                val user = viewModel.getUserByEmail(email)

                if (user != null) {
                    // If user exists, try Firebase authentication
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Authentication successful, navigation will be handled by LaunchedEffect
                                viewModel.getUser(email)
                            } else {
                                // Firebase authentication failed
                                Toast.makeText(
                                    context,
                                    "Login failed: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    // User not found in the database
                    Toast.makeText(
                        context,
                        "User not found",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                // Handle any unexpected errors
                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIos,
                                contentDescription = "back icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    enabled = true,
                    placeholder = { Text(text = "Enter your email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.MailOutline,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.padding(3.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                if (emailError.isNotEmpty()) {
                    Text(
                        text = emailError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Password",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    enabled = true,
                    placeholder = { Text(text = "Enter your password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.padding(3.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (passwordError.isNotEmpty()) {
                    Text(
                        text = passwordError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { loginUser(email, password) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    Text(
                        text = "Login",
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "OR",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )

                Button(
                    onClick = { onSignInClick() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    Text(
                        text = "Login with Google",
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("signup") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//private lateinit var binding:ActivitySignUpBinding
fun SignUpScreen(
    state: SignInState,
    navController: NavController,
    viewModel: SignInViewModel,
    onSignInClick: ()->Unit,
    googleAuthClient: GoogleAuthUiClient,
) {
    val auth = googleAuthClient.getAut()
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    val newUser: User
    var Name = remember {
        mutableStateOf("")
    }

    val Contact = remember {
        mutableStateOf("")
    }

    var gmail = remember {
        mutableStateOf("")
    }

    var password = remember {
        mutableStateOf("")
    }

    var nameError by remember { mutableStateOf("") }
    var contactError by remember { mutableStateOf("") }
    var gmailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    fun signUpAndSignIn(email: String, password: String, user: User){
        state.user=user
        Log.d("STATE USER", user.name)
        Log.d("USER START 0","---------------------------")
//        viewModel.getUser(email)
        viewModel.saveUser(name=user.name, email=user.email, contact = user.contact, password = user.password, role="user", image = "")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Account created successfully, now sign in
                Log.d("USER START 1","---------------------------")
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("USER START 2","---------------------------")
                        // Sign-in success, navigate to home screen
//                        googleAuthClient.getSignedInUser()?.profilePictureUrl?.let { viewModel.saveUser(name=user.name, email=user.email, contact = user.contact, password = user.password, role="user", image = it) }
                        googleAuthClient.getSignedInUser()?.username?.let {
                            Log.d("SIGNED UP USER",
                                it
                            )
                        }
                        navController.navigate("home") {
                            popUpTo(0)
                        }
                    } else {
                        // Handle sign-in failure
                        Log.d("SIGN IN TASK", task.exception.toString())
                        Toast.makeText(
                            context,
                            "Sign-in failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                // Handle account creation failure
                Log.d("SIGN UP TASK", task.exception.toString())
                Toast.makeText(
                    context,
                    "Sign-up failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
//        viewModel.getUser(email)
    }

    fun signInWithSameCredentials(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success, navigate to home screen
                navController.navigate("home") {
                    popUpTo(0)
                }
            } else {
                // Handle sign-in failure
                Log.d("SIGN IN TASK", task.exception.toString())
                Toast.makeText(
                    context,
                    "Sign-in failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun validateFields():Boolean{
        val isValid = false
        if(Name.value.equals("")){
            nameError = "Name is required"
        }
        if(Contact.value.equals("")){
            contactError = "Contact is required"
        }
        if(gmail.value.equals("")){
            gmailError = "Email is required"
        }
        if(password.value.equals("")){
            passwordError = "Password is required"
        }

        return !isValid
    }


    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Sign Up",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIos,
                                contentDescription = "back icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Full Name",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    OutlinedTextField(
                        value = Name.value,
                        onValueChange = { Name.value = it },
                        enabled = true,
                        placeholder = { Text(text = "Full Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.padding(3.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Contact Number",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    OutlinedTextField(
                        value = Contact.value,
                        onValueChange = { Contact.value = it },
                        enabled = true,
                        placeholder = { Text(text = "Contact number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.padding(3.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Gmail",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    OutlinedTextField(
                        value = gmail.value,
                        onValueChange = { gmail.value = it },
                        enabled = true,
                        placeholder = { Text(text = "Gmail Id") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.MailOutline,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.padding(3.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Password",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        enabled = true,
                        placeholder = { Text(text = "Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.padding(3.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    // CREATE ACCOUNT BUTTON
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val user: User = User(
                                name = Name.value,
                                email = gmail.value,
                                password = password.value,
                                contact = Contact.value,
                                role = "user",
                                image = "",
                                id = ""
                            )
                            Log.d("SAVE USER", Name.value)
                            signUpAndSignIn(gmail.value, password.value, user)
                            // navController.navigate("home")
                        },
                        enabled = validateFields(),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = ButtonDefaults.ContentPadding,
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "                   OR",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )

                    Button(
                        onClick = {
                            onSignInClick()
                            // navController.navigate("home")
                        },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = ButtonDefaults.ContentPadding,
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text(
                            text = "Login with Google",
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}