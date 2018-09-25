package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="follow")
public class Follow {

    @EmbeddedId
    private FollowIdentity followIdentity;

    public FollowIdentity getFollowIdentity() {
        return followIdentity;
    }

    public void setFollowIdentity(FollowIdentity followIdentity) {
        this.followIdentity = followIdentity;
    }
}
