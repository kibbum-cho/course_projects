package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "CriticGroup")
public class Groups {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "groupId")
    private Integer groupId;

    @Column(name = "groupName", length = 64, unique = true)
    private String groupName;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
