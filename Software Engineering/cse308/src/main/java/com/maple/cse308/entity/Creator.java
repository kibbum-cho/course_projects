package com.maple.cse308.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Creator")
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "creatorId")
    private Integer creatorId;

    @Column(name = "creator", unique = true, length = 32)
    private String creator;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tvCreator", joinColumns = @JoinColumn(name = "creatorId"), inverseJoinColumns = @JoinColumn(name = "tvId"))
    private Set<TvShow> creators;


    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Set<TvShow> getCreators() {
        return creators;
    }

    public void setCreators(Set<TvShow> creators) {
        this.creators = creators;
    }
}
