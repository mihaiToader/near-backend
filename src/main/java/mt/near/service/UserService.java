package mt.near.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mt.near.domain.UserEntity;
import mt.near.repository.UserRepository;
import mt.near.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityConstants securityConstants;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final BCryptPasswordEncoder bCryptPasswordEncoder,
                       final SecurityConstants securityConstants) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityConstants = securityConstants;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Wrong username or password!"));
    }

    public UserEntity saveUser(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(user.getPassword());
        return userRepository.save(user);
    }

    public String getJWTToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + securityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, securityConstants.SECRET.getBytes())
                .compact();
    }

    public boolean delete(String username){
        return userRepository.deleteUser(username) > 0;
    }

    public List<UserEntity> findUsersByNameContaining(final String name, final Integer limit) {
        return userRepository.findAllByNameContaining(name, new PageRequest(0, limit));
    }

    public List<UserEntity> findUsersByEmailContaining(final String email, final Integer limit) {
        return userRepository.findAllByEmailContaining(email, new PageRequest(0, limit));
    }
}
