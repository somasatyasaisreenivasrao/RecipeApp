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

    fun removeFromFavorites(mealId: String) {
        viewModelScope.launch {
            dao.deleteMeal(mealId)
        }
    }

    suspend fun isFavorite(mealId: String): Boolean = dao.isFavorite(mealId)


}
