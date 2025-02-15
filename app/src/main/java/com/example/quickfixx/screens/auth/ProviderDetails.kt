@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.quickfixx.screens.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import android.net.Uri

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickfixx.R
import com.example.quickfixx.R.drawable.baseline_star_outline_24
import com.example.quickfixx.navigation.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetails(navController: NavController,
                    onBook : ()->Unit) {

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
                CardElevation1("Mukesh", 4, navController, onBook)
                Summary("Mukesh", 4, 4000, 300, 2, true, navController)
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
    rating: Int,
    likes: Int,
    reviews: Int,
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
                                SummaryItem(Icons.Default.ThumbUp,"Likes: $likes")
                                SummaryItem(Icons.Default.RateReview,"Reviews: $reviews")
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
fun CardElevation1(name: String, rating: Int, navController: NavController, onBook: () -> Unit) {
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
                    Image(
                        painter = painterResource(id = R.drawable.qf_app_logo),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
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

                            // Add description text
                            Text(
                                text = "This is a description of the service provider...",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                            )

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
                                        onBook()
                                        navController.navigate("profile")
                                    }
                                ) {
                                    Text(
                                        text = "Book",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

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
                                    onClick = { /*TODO*/ }
                                ) {
                                    Text(
                                        text = "Call",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
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
                                            onClick = { onBook() }
                                        ) {
                                            Text(
                                                text = "Book",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }

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

