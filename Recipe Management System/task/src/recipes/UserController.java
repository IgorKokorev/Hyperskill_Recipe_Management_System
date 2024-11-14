package recipes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/register")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (user.getEmail() == null ||
            user.getEmail().isBlank() ||
            !user.getEmail().contains("@") ||
            !user.getEmail().contains(".")) {
            return ResponseEntity.badRequest().build();
        }
        
        if (user.getPassword() == null ||
            user.getPassword().isBlank() ||
            user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().build();
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok().build();
    }
}
