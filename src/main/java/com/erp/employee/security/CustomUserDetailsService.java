package com.erp.employee.security;

import com.erp.employee.models.Employee;
import com.erp.employee.repositories.IEmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IEmployeeRepository userRepo;

    @Transactional
    public UserDetails loadByUserId(UUID id) {
        Employee user = this.userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Employee user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found with email of " + email));
        return UserPrincipal.create(user);
    }
}