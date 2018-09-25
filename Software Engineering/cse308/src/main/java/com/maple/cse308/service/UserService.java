package com.maple.cse308.service;

import com.maple.cse308.entity.Movie;
import com.maple.cse308.entity.TvShow;
import com.maple.cse308.entity.User;
import com.maple.cse308.enums.Visibility;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    String changePhoto(String photo);

    String changeCountry(String country) throws Exception;

    void changeEmail(String newEmail, String myPassword) throws Exception;

    String changePassword(String oldpass, String newpass) throws Exception;

    String changeHomeTown(String homeTown) throws Exception;

    String changeBio(String bio) throws Exception;

    String changeVisibility(Visibility visibility);

    void deleteUser();

    void deleteUser(String username);

    User findByUsername(String username);

    User getCurrentUser();

    //confirm current authorization level of user
    boolean confirmCurrentRole(String role);

    void updateUser();

    void registerUser(User user) throws Exception;

    void criticApplication(String website, String groups, String publications);

    void addEmployee(User user) throws Exception;

    void removeEmployee(User user) throws Exception;

    void banUser(User user) throws Exception;

    void suspendUser(User user, Calendar calendar) throws Exception;

  /*  void addToBlocklist(String username) throws Exception;

    void removeFromBlocklist(String username) throws Exception;*/

    Set<Movie> getWantToSeeList();

    Set<TvShow> getWantToSeeListTv();

    void addToWantToSeeList(Movie movie) throws Exception;

    void addToWantToSeeListTv(TvShow tv) throws Exception;

    void removeFromWantToSeeList(Movie movie) throws Exception;

    void removeFromWantToSeeListTv(TvShow tv) throws Exception;

    Set<Movie> getDontWantToSeeList();

    Set<TvShow> getDontWantToSeeListTv();

    void addToDontWantToSeeList(Movie movie) throws Exception;

    void addToDontWantToSeeListTv(TvShow tv) throws Exception;

    void removeFromDontWantToSeeList(Movie movie) throws Exception;

    void removeFromDontWantToSeeListTv(TvShow tv) throws Exception;

    void addMovie(Movie movie) throws Exception;

    void editMovie(Movie movie) throws Exception;

    void addTvShow(TvShow tvShow) throws Exception;

    void editTvShow(TvShow tvShow) throws Exception;

    void resetPasswordToken(String email, HttpServletRequest request) throws Exception;

    void resetPassword(String token, String newPass) throws Exception;

    void addFollow(Integer userId);

    void removeFollow(Integer userId);

    List<User> getProfileFollowing();
    List<User> getProfileFollowers();
    List<User> getUserFollowing(Integer userId);
    List<User> getUserFollowers(Integer userId);
}
