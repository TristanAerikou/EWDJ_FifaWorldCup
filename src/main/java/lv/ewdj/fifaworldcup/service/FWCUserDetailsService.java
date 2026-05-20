package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.model.Role;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FWCUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: %s".formatted(username)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                convertAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> convertAuthorities(Role role) {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_%s".formatted(role.name()))
        );
    }
}
