package com.example.U5W3D2.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.U5W3D2.exceptions.BadRequestException;
import com.example.U5W3D2.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class UsersService {
    @Autowired
    UsersDAO usersDAO;

    @Autowired
    Cloudinary cloudinary;

    public Page<User> getUsers(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orderBy));
        return usersDAO.findAll(pageable);
    }

    /*public User save(NewUserDTO user) {
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
        newUser.setAvatarUrl("https://ui-avatars.com/api/?name=" + user.name() + "+" + user.surname());
        newUser.setPassword(user.password());
        return usersDAO.save(newUser);
    }*/

    public void save(User user) {
        // metodo per salvare direttamente gli utenti creati con Java Faker nel database
        usersDAO.save(user);
        System.out.println("Nuovo utente salvato nel database");
    }

    public User findById(UUID id) {
        return usersDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(UUID uuid, User body) {
        User found = this.findById(uuid);
        found.setName(body.getName());
        found.setSurname(body.getSurname());
        found.setUsername(body.getUsername());
        found.setAvatarUrl(body.getAvatarUrl());
        return usersDAO.save(found);
    }

    public void deleteById(UUID uuid) {
        User found = this.findById(uuid);
        usersDAO.delete(found);
    }

    public User uploadAvatar(MultipartFile file, UUID id) throws IOException {

        String url = (String) cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("url");
        User user = this.findById(id);

        user.setAvatarUrl(url);
        return usersDAO.save(user);
    }

    public Page<User> getUsers(String name, String surname, int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orderBy));
        if(name == null && surname == null) return usersDAO.findAll(pageable);
        if(name == null) return usersDAO.findBySurname(surname,pageable);
        if(surname == null) return usersDAO.findByName(name,pageable);
        return usersDAO.findByNameAndSurname(name,surname,pageable);

    }

    public User findByEmail(String email) throws NotFoundException {
        System.out.println(usersDAO.findByEmail(email));
        return usersDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovata!"));
    }

}
