package bank.bankproject.dto.user;

import bank.bankproject.data.entities.User;
import lombok.Data;

@Data
public class UserView {
    private Long id;
    private String username;

    public UserView(User user){
        this.id = user.getUserId();
        this.username = user.getUsername();
    }
}
