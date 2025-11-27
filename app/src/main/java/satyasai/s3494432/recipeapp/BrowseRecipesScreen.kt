package satyasai.s3494432.recipeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


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