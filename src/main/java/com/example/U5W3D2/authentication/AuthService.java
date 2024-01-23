package com.example.U5W3D2.authentication;

import com.example.U5W3D2.exceptions.BadRequestException;
import com.example.U5W3D2.exceptions.UnauthorizedException;
import com.example.U5W3D2.security.JWTTools;
import com.example.U5W3D2.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bCrypt;



    public String authenticateUser(UserLoginDTO body) {
        User user = usersService.findByEmail(body.email());
        System.out.println(body.email() + " email del body");
        System.out.println(user.getEmail() + " email dell'user trovato con usersService");
        System.out.println(body.password() + " password del body");
        System.out.println(user.getPassword() + " password dell'user trovato con userService");
        if(bCrypt.matches(body.password(),user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Errore nelle credenziali");
        }
    }

    public User save(NewUserDTO user) {
        usersDAO.findByEmail(user.email()).ifPresent(user1 -> {
            throw new BadRequestException("Email " + user.email() + " già in uso");
        });
        usersDAO.findByUsername(user.username()).ifPresent(user1 -> {
            throw new BadRequestException("L'username " + user.username() + " già in uso");
        });

        User newUser = new User();
        newUser.setUsername(user.username());
        newUser.setSurname(user.surname());
        newUser.setName(user.name());
        newUser.setEmail(user.email());
        newUser.setRole(UserRole.USER);
        newUser.setAvatarUrl("https://ui-avatars.com/api/?name=" + user.name() + "+" + user.surname());
        newUser.setPassword(bCrypt.encode(user.password()));
        return usersDAO.save(newUser);
    }


}
