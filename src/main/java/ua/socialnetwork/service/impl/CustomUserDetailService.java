package ua.socialnetwork.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.security.SecurityUser;

@Slf4j
@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findUserByUsername(username);
        log.info("loadUserByUsername USERNAME= "+username);

        return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("user " +  "not found"));
    }
}
