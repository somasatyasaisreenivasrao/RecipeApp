package satyasai.s3494432.recipeapp

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val emailKey = UserPrefs.getEmail(context).replace(".", ",")

    val dbRef = FirebaseDatabase.getInstance()
        .getReference("ChefAccounts")
        .child(emailKey)

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // Profile states
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var favoriteCuisine by remember { mutableStateOf("") }

    // Load profile data
    LaunchedEffect(Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            snapshot.getValue(ChefData::class.java)?.let {
                name = it.name
                age = it.age
                email = it.email
                phone = it.phone
                location = it.location
                bio = it.bio
                favoriteCuisine = it.favoriteCuisine
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(70.dp))
            }

            Spacer(Modifier.height(20.dp))

            ProfileTextField("Full Name", name) { name = it }
            ProfileTextField("Age", age) { age = it }
            ProfileTextField("Email", email, enabled = false) {}
            ProfileTextField("Phone", phone) { phone = it }
            ProfileTextField("Location", location) { location = it }

            ProfileTextField(
                label = "Bio",
                value = bio,
                maxLines = 3
            ) { bio = it }

            ProfileTextField("Experience Level", experience) { experience = it }
            ProfileTextField("Favorite Cuisine", favoriteCuisine) { favoriteCuisine = it }

            Spacer(Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    isSaving = true
                    val updatedChef = ChefData(
                        name, age, email, UserPrefs.getPassword(context),
                        phone, location, bio, experience, favoriteCuisine
                    )

                    dbRef.setValue(updatedChef).addOnCompleteListener {
                        isSaving = false
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving)
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                else
                    Text("Save Changes")
            }

            Spacer(Modifier.height(20.dp))

            // Logout
            OutlinedButton(
                onClick = {
                    UserPrefs.markLoginStatus(context,false)

                    val intent = Intent(context, RecipeLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}


@Composable
fun ProfileTextField(
    label: String,
    value: String,
    enabled: Boolean = true,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

