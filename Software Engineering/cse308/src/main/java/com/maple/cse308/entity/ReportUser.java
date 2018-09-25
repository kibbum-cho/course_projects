package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="report_user")
public class ReportUser {

    @Id
    @Column(name="userId")
    private Integer userId;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
