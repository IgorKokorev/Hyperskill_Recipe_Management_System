package recipes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Slf4j
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;
    
    @GetMapping
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") int id, @AuthenticationPrincipal User user) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe != null) {
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ResponseEntity<RecipeId> postRecipe(@RequestBody Recipe recipe, @AuthenticationPrincipal User user) {
        if (!checkRecipe(recipe)) return ResponseEntity.badRequest().body(null);
        recipe.setDate(LocalDateTime.now());
        recipe.setUserId(user.getId());
        recipe = recipeRepository.save(recipe);
        
        return ResponseEntity.ok(new RecipeId(recipe.getId()));
    }
    
    private boolean checkRecipe(Recipe recipe) {
        if (recipe == null) return false;
        if (recipe.getName() == null || recipe.getName().isBlank()) return false;
        if (recipe.getCategory() == null || recipe.getCategory().isBlank())
            return false;
        if (recipe.getDescription() == null || recipe.getDescription().isBlank())
            return false;
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty())
            return false;
        if (recipe.getDirections() == null || recipe.getDirections().isEmpty())
            return false;
        return true;
    }
    
    @DeleteMapping
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Recipe> deleteRecipe(
        @PathVariable("id") int id,
        @AuthenticationPrincipal User user) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe != null) {
            if (!recipe.getUserId().equals(user.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            recipeRepository.delete(recipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> updateRecipe(
        @RequestBody Recipe recipe,
        @PathVariable("id") int id,
        @AuthenticationPrincipal User user) {
        if (!checkRecipe(recipe)) return ResponseEntity.badRequest().body(null);
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Recipe savedRecipe = recipeOptional.get();
        if (!savedRecipe.getUserId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipe.setDate(LocalDateTime.now());
        recipe.setId(id);
        recipe.setUserId(user.getId());
        recipeRepository.save(recipe);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    public ResponseEntity<List<Recipe>> searchRecipe(
        @RequestParam(name = "category", required = false) String category,
        @RequestParam(name = "name", required = false) String name
    ) {
        if (category == null && name == null) return ResponseEntity.badRequest().body(null);
        if (category != null && name != null) return ResponseEntity.badRequest().body(null);
        if (category == null && name.isBlank()) return ResponseEntity.badRequest().body(null);
        if (name == null && category.isBlank()) return ResponseEntity.badRequest().body(null);
        
        
        List<Recipe> recipes = category == null ?
            recipeRepository.findByNameContainsIgnoreCaseOrderByDateDesc(name) :
            recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        if (recipes == null) recipes = emptyList();
        
        return ResponseEntity.ok(recipes);
    }
}
