package com.security.config;

import com.security.model.Customer;
import com.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomeUserDetailsService implements UserDetailsService {

    private  CustomerRepository customerRepository;
    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      Customer customerDetail = customerRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("UserName not found"));
      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customerDetail.getRole()));
        return new User(customerDetail.getEmail(),customerDetail.getPwd(),authorities);
    }
}
