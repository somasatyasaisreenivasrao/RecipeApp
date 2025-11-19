package satyasai.s3494432.recipeapp

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Composable
fun Home()
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
        ImageCard("https://www.indianhealthyrecipes.com/wp-content/uploads/2022/03/chicken-65-swasthi.jpg","Chicken65","121")
        ImageCard("https://pickyeaterblog.com/wp-content/uploads/2021/05/indian-vegetables-Final-2.jpg","Veg Curry","11")
    }

}


@Composable
fun ImageCard(
    imageUrl: String,
    title: String,
    badgeText: String,
    modifier: Modifier = Modifier,
    cardHeight: Dp = 500.dp,
    cornerRadius: Dp = 12.dp
) {
    Card(
        modifier = modifier
            .height(cardHeight)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image (uses Coil)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.chicken_65),
                error = painterResource(id = R.drawable.chicken_65),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()

            )

            // Top-right badge (star + text)
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x99000000))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "rating",
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 13.sp
                )
            }

            // Bottom title with blurred translucent background
            val overlayHeight = 56.dp
            val overlayModifier = Modifier
                .fillMaxWidth()
                .height(overlayHeight)
                .align(Alignment.BottomStart)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Use RenderEffect blur (Android 12+)
                val blurPx = with(LocalDensity.current) { 12.dp.toPx() }
                overlayModifier
                    .graphicsLayer {
                        // apply a blur to the layer behind the overlay to get a frosted effect
                        renderEffect = RenderEffect
                            .createBlurEffect(blurPx, blurPx, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
            }

            // Place a semi-transparent gradient over the bottom so text is readable (works on all API levels)
            Box(
                modifier = overlayModifier
                    .drawBehind { /* keep space for gradient */ }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x66000000), Color(0x00000000)),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 28.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Home()
}