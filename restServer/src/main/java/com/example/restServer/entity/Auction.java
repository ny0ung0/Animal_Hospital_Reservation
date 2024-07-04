package com.example.restServer.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Member hospital;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    private String itemName;
    private String itemDescription;
    private Double startingBid;
    private Double currentBid;

    @ManyToOne
    @JoinColumn(name = "current_bidder_id")
    private Member currentBidder;

    private Double upperLimitPrice;
    private Date expiryDate;
    private Date startDate;
    private Date endDate;
    private Boolean isCompleted;

    // Getters and Setters
}
