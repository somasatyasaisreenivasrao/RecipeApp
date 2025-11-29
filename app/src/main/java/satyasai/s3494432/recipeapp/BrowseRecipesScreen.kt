package satyasai.s3494432.recipeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.*
import coil.compose.AsyncImage


@Composable
fun BrowseRecipesScreen()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(color = colorResource(id = R.color.Violet))
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Recipe App",
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 8.dp)
            )


            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cook Book",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Recipe App",
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 8.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "Recipe App",
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}


@Preview(showBackground = true,  heightDp = 1200)
@Composable
fun BrowseRecipesScreenPreview() {
    RecipeDetailScreen()
}

@Composable
fun RecipeDetailScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // --- Top Image Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1603133872878-684f208fb84e",
                contentDescription = "Spaghetti Oglio",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Back & Favorite Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                IconButton(onClick = { /*TODO: Back*/ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /*TODO: Favorite*/ }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            }
        }

        // --- Recipe Info Section ---
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Spaghetti Oglio",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
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
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "Spaghetti aglio e olio is a simple yet delicious Italian pasta dish. It’s made with spaghetti noodles, garlic, olive oil, and chili flakes.",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Ingredients Section ---
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val ingredients = listOf(
                Triple("Spaghetti", "225g", painterResource(id = R.drawable.ic_spaghetti)),//ic_spaghetti
                Triple("Olive Oil", "60g", painterResource(id = R.drawable.ic_olive)),//ic_olive_oil
                Triple("Red Pepper Flakes", "1/2 tsp", painterResource(id = R.drawable.ic_pepper)),//ic_pepper
                Triple("Parsley", "10g", painterResource(id = R.drawable.ic_parsley)),//ic_parsley
                Triple("Onion", "4 cloves", painterResource(id = R.drawable.ic_onion))//ic_onion
            )

            ingredients.forEach { (name, qty, icon) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = icon,
                            contentDescription = name,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = name, fontSize = 15.sp)
                    }
                    Text(text = qty, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Directions Section ---
            Text(
                text = "Directions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "1. Fill a large pot with water, bring it to a rolling boil over high heat.\n" +
                        "2. Add spaghetti and cook according to the package instructions.\n" +
                        "3. Heat olive oil, add garlic and red pepper flakes.\n" +
                        "4. Toss cooked pasta and garnish with parsley.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Watch Video Button ---
            Button(
                onClick = { /*TODO: Watch video*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(text = "▶ Watch Videos", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
