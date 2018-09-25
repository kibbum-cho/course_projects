package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "Publication")
public class Publications {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "publicationId")
    private Integer publicationId;

    @Column(name = "criticId")
    private Integer criticId;

    @Column(name = "publicationName")
    private String publication;

    public Integer getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Integer publicationId) {
        this.publicationId = publicationId;
    }

    public Integer getCriticId() {
        return criticId;
    }

    public void setCriticId(Integer criticId) {
        this.criticId = criticId;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

}
