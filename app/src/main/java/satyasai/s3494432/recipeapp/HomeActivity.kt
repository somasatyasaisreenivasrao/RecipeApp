package satyasai.s3494432.recipeapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import satyasai.s3494432.recipeapp.data.HomeViewModel
import java.util.Locale

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                RecipeApp()
            }
        }
    }
}

// --------------------------- Navigation Setup ---------------------------
@Composable
fun RecipeApp(viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("details") { RecipeDetailsScreen(navController, viewModel) }
        composable("favorites") { FavoritesScreen(navController, viewModel) }
        composable("profile") { ProfileScreen(navController) }
    }
}

// --------------------------- Home Screen ---------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Voice search launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrEmpty()) {
                query = spokenText
                viewModel.searchMeals(spokenText)
            } else {
                Toast.makeText(context, "No voice input detected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a recipe name...")
        }
        try {
            voiceLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Voice search not supported", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadDefaultMeals()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF8F9FA), Color(0xFFE3F2FD))))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row()
        {
            Text(
            text = "🍳 Cook Book",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF212121),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.favorite),
                contentDescription = "Favorites",
                modifier = Modifier
                    .size(38.dp)
                    .clickable {
                        navController.navigate("favorites")                    }
                    .padding(end = 4.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(38.dp)
                    .clickable {
                        navController.navigate("profile")// ✅ navigate to Favorites screen
                    }
                    .padding(end = 4.dp)
            )
        }
//

        // Search bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search recipes...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),

            trailingIcon = {
                Row {
                    IconButton(onClick = { viewModel.searchMeals(query) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { startVoiceSearch() }) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Search")
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Categories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val categories = listOf("Breakfast", "Lunch", "Dinner")
            categories.forEach { category ->
                CategoryChip(
                    text = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        if(selectedCategory=="Lunch"){
                            viewModel.loadCategory("Chicken")
                        }
                        else if(selectedCategory=="Dinner")
                        {
                            viewModel.loadCategory("Seafood")
                        }
                        else
                        viewModel.loadCategory(category)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Crossfade(targetState = viewModel.isLoading) { loading ->
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1E88E5))
                }
            } else {
                if (viewModel.meals.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No recipes found 😔", color = Color.Gray)
                    }
                } else {
                    LazyColumn {
                        items(viewModel.meals) { meal ->
                            ImageCard(
                                imageUrl = meal.strMealThumb,
                                title = meal.strMeal,
                                badgeText = meal.idMeal,
                                onClick = {
                                    viewModel.getMealDetails(meal.idMeal) {
                                        navController.navigate("details")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                brush = if (isSelected)
                    Brush.horizontalGradient(listOf(Color(0xFF42A5F5), Color(0xFF1976D2)))
                else
                    Brush.linearGradient(listOf(Color(0xFFE3F2FD), Color(0xFFE3F2FD)))
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF1976D2),
            fontWeight = FontWeight.SemiBold
        )
    }
}

// --------------------------- Recipe Card ---------------------------
@Composable
fun ImageCard(
    imageUrl: String,
    title: String,
    badgeText: String,
    modifier: Modifier = Modifier,
    cardHeight: Dp = 240.dp,
    cornerRadius: Dp = 18.dp,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    RecipeApp()
}

