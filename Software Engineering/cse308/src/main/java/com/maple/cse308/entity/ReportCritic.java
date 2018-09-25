package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="report_critic")
public class ReportCritic{

    @Id
    @Column(name="criticId")
    private Integer criticId;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "criticId", insertable = false, updatable = false)
    private Critic critic;

    public Critic getCritic() {
        return critic;
    }

    public void setCritic(Critic critic) {
        this.critic = critic;
    }

    public Integer getCriticId() {
        return criticId;
    }

    public void setCriticId(Integer criticId) {
        this.criticId = criticId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
