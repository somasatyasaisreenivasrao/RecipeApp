package satyasai.s3494432.recipeapp

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import satyasai.s3494432.recipeapp.data.HomeViewModel
import satyasai.s3494432.recipeapp.data.Meal


@Composable
fun RecipeDetailsScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val meal = viewModel.selectedMeal
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    val favoriteTint by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else Color.White,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(meal?.idMeal) {
        meal?.let {
            isFavorite = viewModel.isFavorite(it.idMeal)
        }
    }

    if (meal == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick =
                            {
                            if (isFavorite) {
                            viewModel.removeFromFavorites(meal.idMeal)
                            isFavorite = false
                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addToFavorites(meal)
                            isFavorite = true
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                        }},
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorite",
                            tint = favoriteTint
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = "Time",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = " 20 mins  ", fontSize = 14.sp)

                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = " 4.9  ", fontSize = 14.sp)

                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Easy",
                        tint = Color(0xFF8BC34A),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = " Easy", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = meal.strInstructions ?: "No description available.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val ingredients = extractIngredients(meal)
                ingredients.forEach { (ingredient, measure) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = ingredient,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = ingredient, fontSize = 15.sp)
                        }
                        Text(text = measure, fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Directions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = meal.strInstructions ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        meal.strYoutube?.let { url ->
                            openUrl(context, url)
                        } ?: Toast.makeText(context, "No video found", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "▶ Watch Video", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

fun extractIngredients(meal: Meal): List<Pair<String, String>> {
    val list = mutableListOf<Pair<String, String>>()

    val ingredients = listOf(
        meal.strIngredient1, meal.strIngredient2, meal.strIngredient3, meal.strIngredient4,
        meal.strIngredient5, meal.strIngredient6, meal.strIngredient7, meal.strIngredient8,
        meal.strIngredient9, meal.strIngredient10, meal.strIngredient11, meal.strIngredient12,
        meal.strIngredient13, meal.strIngredient14, meal.strIngredient15, meal.strIngredient16,
        meal.strIngredient17, meal.strIngredient18, meal.strIngredient19, meal.strIngredient20
    )

    val measures = listOf(
        meal.strMeasure1, meal.strMeasure2, meal.strMeasure3, meal.strMeasure4,
        meal.strMeasure5, meal.strMeasure6, meal.strMeasure7, meal.strMeasure8,
        meal.strMeasure9, meal.strMeasure10, meal.strMeasure11, meal.strMeasure12,
        meal.strMeasure13, meal.strMeasure14, meal.strMeasure15, meal.strMeasure16,
        meal.strMeasure17, meal.strMeasure18, meal.strMeasure19, meal.strMeasure20
    )

    for (i in ingredients.indices) {
        val ingredient = ingredients[i]
        val measure = measures[i]
        if (!ingredient.isNullOrBlank() && ingredient != "null") {
            list.add(ingredient.trim() to (measure?.trim() ?: ""))
        }
    }

    return list
}


fun openUrl(context: android.content.Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
    }
}