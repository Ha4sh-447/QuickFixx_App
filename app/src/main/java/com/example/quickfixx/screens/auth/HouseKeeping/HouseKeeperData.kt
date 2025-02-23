//@file:Suppress("PreviewAnnotationInFunctionWithParameters")
//package com.example.quickfixx.screens.auth.HouseKeeping
//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CalendarMonth
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material3.AssistChip
//import androidx.compose.material3.AssistChipDefaults
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.example.quickfixx.R.drawable.baseline_star_outline_24
//import com.example.quickfixx.ViewModels.ElectricianViewModel
//import com.example.quickfixx.navigation.Screens
//
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HouseKeepingData(navController: NavController, viewModel: ElectricianViewModel) {
//
//    val electricianList = viewModel.state.value.data
//
//    val coroutineScope = rememberCoroutineScope()
//
////    Log.d("Electrician", electricianList.toString())
//
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(title = {
//                    Text(text = "QuickFixx",
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            navController.navigate(Screens.CustomerSupport.route)
//                        },
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Menu,
//                                contentDescription = "Menu icon",
//                                tint = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                    }
//                )
//            }
//        ) {
//
//
////                Text(text = "Experts", TextAlign= TextAlign.Start, modifier = Modifier.padding(16.dp))
//
//            LazyColumn(
//                modifier = Modifier
//                    .padding(vertical = it.calculateTopPadding())
//                    .fillMaxSize()
////                .verticalScroll(rememberScrollState())
//            ) {
//                item{
//                    Text(text = "Experts",
//                        Modifier
//                            .padding(top = 5.dp, bottom = 9.dp, start = 9.dp)
//                            .fillMaxWidth()
//                            .background(MaterialTheme.colorScheme.background)
//                            .wrapContentHeight(),
//                        fontSize = 24.sp,  // Set the desired font size here
//                        fontWeight = FontWeight.W900,
//                        textAlign = TextAlign.Left,
//                        textDecoration = TextDecoration.Underline
//                    )
//                }
//                electricianList?.let{ electricians ->
//                    items(items = electricians){
//                        Log.d("Electrician-name", it.name)
//                        Log.d("Electrician-rating", it.rating.toString())
//                        ElecCard(name = it.name, rating = it.rating, navController = navController)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun BookButton(navController: NavController) {
//    Button(
//
//        onClick = {
//            navController.navigate(Screens.ElectricianData.route)
//        },
//        shape = RoundedCornerShape(10.dp),
//        contentPadding = ButtonDefaults.ContentPadding,
//        modifier = Modifier
//            .width(200.dp)
//    ) {
//        Icon(
//            imageVector = Icons.Default.CalendarMonth,
//            contentDescription = null
//        )
//        Text(
//            text = "Book Service",
//            letterSpacing = 1.sp
//
//        )
//    }
//}
//@Composable
//fun CallButton(navController: NavController) {
//    Button(
//        onClick = {
//            navController.navigate(Screens.ElectricianData.route)
//        },
//        shape = RoundedCornerShape(10.dp),
//        contentPadding = ButtonDefaults.ContentPadding,
//        modifier = Modifier
//            .width(170.dp)
//    ) {
//        Icon(
//            imageVector = Icons.Default.Phone,
//            contentDescription = null
//        )
//        Text(
//            text = "Call",
//            letterSpacing = 1.sp
//
//        )
//    }
//}
//
//@Composable
//fun ElecCard(name: String, rating: Float, navController: NavController) {
//    Surface(
//        shape = RoundedCornerShape(16.dp),
//        color = Color(0xFFDAE1E7),
//        modifier = Modifier
//            .height(310.dp)
//            .padding(10.dp)
//            .padding(start = 3.dp)
//            .fillMaxWidth(),
//        shadowElevation = 10.dp
//    ) {
//        Column(
//            verticalArrangement = Arrangement.Center
//        ) {
//
//
//            Row(
//                modifier = Modifier.padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Surface(
//                    shape = RoundedCornerShape(16.dp),
//                    modifier = Modifier
//                        .size(width = 100.dp, height = 140.dp)
//
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?q=80&w=1965&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
//                        contentDescription = null,
//                        modifier = Modifier.size(128.dp)
//                    )
//                }
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .weight(2f),
////                verticalArrangement = Arrangement.Center
//                ) {
//                    Surface(
//                        shape = RoundedCornerShape(24.dp),
//                        modifier = Modifier
//                            .padding(2.dp)
//                            .fillMaxWidth()
//                            .size(width = 500.dp,height =280.dp )
//                            .padding(start = 16.dp),
//                        color = Color(0xFFD1D5E1)
//                    ) {
//                        Column(
////                            Modifier.padding(horizontal = 4.dp)
//                        ){
//                            Text(
//                                text = name,
//                                fontSize = 24.sp,
//                                style = MaterialTheme.typography.titleLarge,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(10.dp)
//                            )
//
//                            Spacer(modifier = Modifier.height(2.dp))
//
//
//                            AssistChip(
//                                modifier = Modifier.padding(start = 16.dp),
//                                onClick = { Log.d("Assist chip", "hello world") },
//                                label = { Text("Verified") },
//                                leadingIcon = {
//                                    Icon(
//                                        Icons.Filled.Check,
//                                        contentDescription = "Localized description",
//                                        Modifier.size(AssistChipDefaults.IconSize)
//                                    )
//                                }
//                            )
//
//
//                            Spacer(modifier = Modifier.height(2.dp))
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Text(
//                                    text = buildAnnotatedString {
//
//                                        if (rating >= 4) {
//                                            withStyle(style = SpanStyle(color = Color.Black)) {
//                                                append("Excellent: $rating")
//                                            }
//                                        } else if (rating > 3) {
//                                            withStyle(style = SpanStyle(color = Color.Black)) {
//                                                append("Good: $rating")
//                                            }
//                                        } else {
//                                            withStyle(style = SpanStyle(color = Color.Black)) {
//                                                append("Average: $rating")
//                                            }
//                                        }
//                                    },
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.SemiBold,
//                                    style = MaterialTheme.typography.titleLarge
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                repeat(5) { index ->
//                                    val starColor =
//                                        if (index < rating) Color(0xFFF6B266) else Color.Gray
//
//                                    Icon(
//                                        painter = painterResource(id = baseline_star_outline_24),
//                                        tint = starColor,
//                                        contentDescription = null
//                                    )
//                                }
//                            }
//
//                            Spacer(modifier = Modifier.height(4.dp))
//
//                            OutlinedButton(
//                                modifier = Modifier.padding(start = 56.dp),
//                                shape = RoundedCornerShape(8.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    contentColor = Color.Black,
//                                    containerColor = Color.White
//                                ),
//                                onClick = {
//                                    navController.navigate("profile")
//                                }
//                            ) {
//                                Text(
//                                    text = "View Profile",
//                                    fontSize = 11.sp,
//                                    fontWeight = FontWeight.SemiBold,
//                                    style = MaterialTheme.typography.titleLarge
//                                )
//                            }
//
//                            Spacer(modifier = Modifier.width(20.dp))
//                            OutlinedButton(
//                                modifier = Modifier.padding(start = 66.dp),
//                                shape = RoundedCornerShape(8.dp),
//                                border = BorderStroke(1.dp, Color.Blue),
//                                colors = ButtonDefaults.buttonColors(
//                                    contentColor = Color.Blue,
//                                    containerColor = Color.White,
//                                ),
//                                onClick = { /*TODO*/ }
//                            ) {
//                                Text(
//                                    text = "Contact",
//                                    fontSize = 11.sp,
//                                    fontWeight = FontWeight.SemiBold,
//                                    style = MaterialTheme.typography.titleLarge
//                                )
//                            }
//                        }
//                    }
//
//                }
//                Spacer(modifier = Modifier.width(20.dp))
//            }
//
//        }
//    }}
//
