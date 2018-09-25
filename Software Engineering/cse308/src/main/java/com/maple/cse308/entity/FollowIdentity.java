package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FollowIdentity implements Serializable {

    @Column(name="userId")
    private Integer userId;

    @Column(name="followingId")
    private Integer followingId;

    public FollowIdentity(){}

    public FollowIdentity(Integer userId, Integer followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFollowingId() {
        return followingId;
    }

    public void setFollowedId(Integer followingId) {
        this.followingId = followingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowIdentity that = (FollowIdentity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return followingId != null ? followingId.equals(that.followingId) : that.followingId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (followingId != null ? followingId.hashCode() : 0);
        return result;
    }
}
