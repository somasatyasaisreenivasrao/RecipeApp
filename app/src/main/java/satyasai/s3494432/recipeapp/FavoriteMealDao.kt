package satyasai.s3494432.recipeapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: FavoriteMealEntity)

    @Query("DELETE FROM favorite_meals WHERE idMeal = :mealId")
    suspend fun deleteMeal(mealId: String)

    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): Flow<List<FavoriteMealEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE idMeal = :mealId)")
    suspend fun isFavorite(mealId: String): Boolean
}