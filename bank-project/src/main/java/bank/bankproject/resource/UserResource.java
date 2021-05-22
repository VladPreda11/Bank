package bank.bankproject.resource;

import bank.bankproject.service.TransactionService;
import bank.bankproject.service.UserService;
import bank.bankproject.dto.user.UserView;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserResource {
    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping
    public List<UserView> getCurrentUserOrUserByUsername(@PathParam("username") Optional<String> username) {
        if(username.isPresent()){
            return userService.getByUsername(username.get());
        }

        return Arrays.asList(new UserView(userService.getCurrentUser()));
    }

    @GetMapping("/{id}")
    public UserView getById(@PathVariable long id) {
        return new UserView(userService.getById(id));
    }

}
