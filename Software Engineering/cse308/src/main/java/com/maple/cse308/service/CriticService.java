package com.maple.cse308.service;

import com.maple.cse308.entity.Critic;

import java.util.List;

public interface CriticService {

    Critic getCriticById(int criticId);

    List<Critic> criticSearch(String search);
}
