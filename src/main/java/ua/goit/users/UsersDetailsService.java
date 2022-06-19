package ua.goit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UsersDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDto user = userService.loadUserByUserName(name);
        return new UserPrincipal(user);
    }
}
