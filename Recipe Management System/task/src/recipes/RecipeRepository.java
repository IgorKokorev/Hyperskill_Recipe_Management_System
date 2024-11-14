package recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByNameContainsIgnoreCaseOrderByDateDesc(String name);
    
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}
