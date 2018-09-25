package com.maple.cse308.entity;


import com.maple.cse308.enums.Visibility;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", nullable = false)
    private int userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "resetToken")
    private String resetToken;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "bio")
    private String bio;

    @Column(name = "hometown")
    private String hometown;

    @Column(name = "country")
    private String country;

    @Column(name = "suspendDate")
    @Temporal(TemporalType.DATE)
    private Calendar suspendDate;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles;

    /*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "UserUser", joinColumns = @JoinColumn(name = "userId1"), inverseJoinColumns = @JoinColumn(name = "userId2"))
    private Set<User> blocklist;*/

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "WatchList", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "movieId"))
    private Set<Movie> watchList;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "DontWatchList", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "movieId"))
    private Set<Movie> dontWatchList;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "WatchListTV", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "tvId"))
    private Set<TvShow> watchListTV;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "DontWatchListTV", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "tvId"))
    private Set<TvShow> dontWatchListTV;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Calendar getSuspendDate() {
        return suspendDate;
    }

    public void setSuspendDate(Calendar suspendDate) {
        this.suspendDate = suspendDate;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

  /*  public Set<User> getBlocklist() {
        return blocklist;
    }

    public void setBlocklist(Set<User> blocklist) {
        this.blocklist = blocklist;
    }*/

    public Set<Movie> getWatchList() {
        return watchList;
    }

    public void setWatchList(Set<Movie> watchList) {
        this.watchList = watchList;
    }

    public Set<Movie> getDontWatchList() {
        return dontWatchList;
    }

    public void setDontWatchList(Set<Movie> dontWatchList) {
        this.dontWatchList = dontWatchList;
    }

    public Set<TvShow> getWatchListTV() {
        return watchListTV;
    }

    public void setWatchListTV(Set<TvShow> watchListTV) {
        this.watchListTV = watchListTV;
    }

    public Set<TvShow> getDontWatchListTV() {
        return dontWatchListTV;
    }

    public void setDontWatchListTV(Set<TvShow> dontWatchListTV) {
        this.dontWatchListTV = dontWatchListTV;
    }
}

