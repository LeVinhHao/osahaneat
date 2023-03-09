package com.cybersoft.osahaneat.service;

import com.cybersoft.osahaneat.repository.UserRepository;
import com.cybersoft.osahaneat.service.imp.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceImp {
    @Autowired
    UserRepository userRepository;
    @Override
    public boolean login(String username, String password) {
        return userRepository.findByEmailAndPassword(username,password).size()>0;
    }
}
