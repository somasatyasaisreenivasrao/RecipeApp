package satyasai.s3494432.recipeapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_meals")
data class FavoriteMealEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strYoutube: String?
)
