package bank.bankproject.service;

import bank.bankproject.exception.UserNotFoundException;
import bank.bankproject.exception.UsernameTakenException;
import bank.bankproject.repository.UserRepository;
import bank.bankproject.domain.AuthRequest;
import bank.bankproject.dto.user.UserDto;
import bank.bankproject.dto.user.UserView;
import bank.bankproject.data.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserView register(AuthRequest request){
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> { throw new UsernameTakenException(""); });

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User userToRegister = new User(request.getUsername(), "USER;", encodedPassword);
        User userRegistered = userRepository.save(userToRegister);

        return new UserView(userRegistered);
    }

    public List<UserView> getByUsername(String username){
        return userRepository
                .findByUsernameContains(username)
                .stream()
                .map(u -> new UserView(u))
                .collect(Collectors.toList());

    }

    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<UserDto> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userEntity -> new UserDto(
                        userEntity.getUsername(),
                        "")
                )
                .collect(Collectors.toList());
    }

    public User getCurrentUser(){
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

}
