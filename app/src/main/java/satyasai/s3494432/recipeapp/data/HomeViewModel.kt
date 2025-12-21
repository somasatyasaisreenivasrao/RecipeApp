package satyasai.s3494432.recipeapp.data

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import satyasai.s3494432.recipeapp.AppDatabase
import satyasai.s3494432.recipeapp.FavoriteMealEntity

class HomeViewModel (application: Application) : AndroidViewModel(application) {
    private val api: MealApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApi::class.java)
    }

    var meals by mutableStateOf<List<Meal>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    var selectedMeal by mutableStateOf<Meal?>(null)
        private set

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.favoriteMealDao()

    private val _favorites = MutableStateFlow<List<FavoriteMealEntity>>(emptyList())
    val favorites = _favorites.asStateFlow()

    fun loadDefaultMeals() {
        searchMeals("chicken")
    }

    fun searchMeals(query: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = api.searchMeals(query)
                meals = response.meals ?: emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCategory(category: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = api.filterByCategory(category)
                meals = response.meals ?: emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMealTime(mealTime: String) {
        viewModelScope.launch {
            isLoading = true
            meals = when (mealTime) {
                "Breakfast" -> api.filterByCategory("Breakfast").meals
                "Lunch" -> api.filterByCategory("Chicken").meals
                "Dinner" -> api.filterByCategory("Seafood").meals
                else -> api.searchMeals("chicken").meals
            } ?: emptyList()
            isLoading = false
        }
    }


    fun estimateTime(category: String?): String {
        return when (category) {
            "Breakfast" -> "15–20 min"
            "Dessert" -> "30–40 min"
            "Seafood" -> "35–45 min"
            "Chicken", "Beef" -> "40–60 min"
            else -> "25–35 min"
        }
    }

    fun getRating(mealId: String): Float {
        // UI-only rating (stable per meal)
        return (3.8f + (mealId.hashCode() % 12) / 10f).coerceIn(3.8f, 4.9f)
    }


    fun getMealDetails(mealId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val response = api.getMealById(mealId)
            selectedMeal = response.meals?.firstOrNull()
            isLoading = false
            onDone()
        }
    }

    init {
        viewModelScope.launch {
            dao.getAllFavorites().collect { _favorites.value = it }
        }
    }

    fun addToFavorites(meal: Meal) {
        viewModelScope.launch {
            val favorite = FavoriteMealEntity(
                idMeal = meal.idMeal,
                strMeal = meal.strMeal,
                strMealThumb = meal.strMealThumb,
                strCategory = meal.strCategory,
                strArea = meal.strArea,
                strInstructions = meal.strInstructions,
                strYoutube = meal.strYoutube
            )
            dao.insertMeal(favorite)
        }
    }

    fun applyFilters(
        query: String,
        category: String?,
        vegOnly: Boolean
    ) {
        viewModelScope.launch {
            isLoading = true

            val result = when {
                category != null -> getMealsByCategorySmart(category)
                query.isNotBlank() -> api.searchMeals(query).meals
                else -> api.searchMeals("chicken").meals
            } ?: emptyList()

            meals = if (vegOnly) {
                result.filter { isVegMeal(it) }
            } else {
                result
            }

            isLoading = false
        }
    }

    private fun isVegMeal(meal: Meal): Boolean {
        val nonVegKeywords = listOf(
            "chicken", "beef", "pork", "fish", "lamb",
            "mutton", "seafood", "shrimp", "egg"
        )

        val ingredients = listOfNotNull(
            meal.strIngredient1,
            meal.strIngredient2,
            meal.strIngredient3,
            meal.strIngredient4,
            meal.strIngredient5
        ).joinToString(" ").lowercase()

        return nonVegKeywords.none { ingredients.contains(it) }
    }

    private suspend fun getMealsByCategorySmart(category: String): List<Meal> {
        return when (category) {
            "Breakfast" -> {
                api.filterByCategory("Breakfast").meals ?: emptyList()
            }

            "Lunch" -> {
                api.filterByCategory("Chicken").meals ?: emptyList()
            }

            "Dinner" -> {
                api.filterByCategory("Seafood").meals ?: emptyList()
            }

            else -> emptyList()
        }
    }


    fun removeFromFavorites(mealId: String) {
        viewModelScope.launch {
            dao.deleteMeal(mealId)
        }
    }

    suspend fun isFavorite(mealId: String): Boolean = dao.isFavorite(mealId)


}
