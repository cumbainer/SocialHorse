package ua.socialnetwork.security;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.Friend;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.entity.UserImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private User user;

    @Override
    //This method represents what users are allowed to do(authorities)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return roleList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return user.getFirstName() + ' ' + user.getLastName();
    }

    public int getAge() {
        return user.getAge();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public List<UserImage> getImages() {
        return user.getImages();
    }

    public int getImage() {
        return user.getImages().get(0).getId();
    }

    public int getImageForFeed() {
        List<UserImage> out = user.getImages();

        if (out.size() == 1 && out.get(0).getName().equals("userImage")) {
            return out.get(0).getId();
        }
        if (out.size() == 2 && out.get(1).getName().equals("backgroundImage")) {
            return out.get(0).getId();
        }
        if (out.size() == 2 && out.get(1).getName().equals("userImage")) {
            return out.get(1).getId();
        }
        if (out.size() == 2) {
            return out.get(0).getId();
        }
        if (out.size() >= 3) {
            return out.get(user.getImages().size() - 1).getId();
        }
        return -1;

    }



//    public Set<Friend> getReceivedRequests() {
//        return user.getReceivedRequests();
//    }

//    public Set<Friend> getSentRequest() {
//        return user.getSentRequest();
//    }

}
