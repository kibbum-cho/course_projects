package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "Critic")
public class Critic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "criticId")
    private Integer criticId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "website")
    private String website;

    @Column(name = "publication")
    private String publication;

    @Column(name = "critic_group")
    private String group;

    @Column(name = "photo")
    private String photo;

    @Column(name = "topCritic")
    private Boolean isTopCritic = false;

    public Integer getCriticId() {
        return criticId;
    }

    public void setCriticId(Integer criticId) {
        this.criticId = criticId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getIsTopCritic() {
        return this.isTopCritic;
    }

    public void setIsTopCritic(Boolean isTopCritic) {
        this.isTopCritic = isTopCritic;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getTopCritic() {
        return isTopCritic;
    }

    public void setTopCritic(Boolean topCritic) {
        isTopCritic = topCritic;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
