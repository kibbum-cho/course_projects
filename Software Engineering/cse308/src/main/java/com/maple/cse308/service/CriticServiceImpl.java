package com.maple.cse308.service;

import com.maple.cse308.entity.Critic;
import com.maple.cse308.entity.User;
import com.maple.cse308.repository.CriticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CriticServiceImpl implements CriticService {

    @Autowired
    CriticRepository criticRepository;

    @Override
    public Critic getCriticById(int criticId) {
        return criticRepository.findByCriticId(criticId);
    }

    @Override
    public List<Critic> criticSearch(String search) {
        //String needs to be parsed, and removed for duplcates.
        String[] searchString;
        if (search.contains(" ")) {
            searchString = search.split(" ");
        } else {
            searchString = new String[1];
            searchString[0] = search;
        }
        String longest = searchString[0];
        for (String string : searchString) {
            if (string.length() > longest.length()) {
                longest = string;
            }
        }

        List<Critic> criticList = criticRepository.findAllByFirstNameOrMiddleNameOrLastNameIgnoreCase(longest, longest, longest);
        List<Critic> resultList = new LinkedList();
        resultList.addAll(criticList);
        for (String string : searchString) {
            string = string.toLowerCase();
            for (Critic critic : criticList) {
                String first = critic.getFirstName().toLowerCase();
                String middle = critic.getMiddleName().toLowerCase();
                String last = critic.getLastName().toLowerCase();
                if (!first.contains(string) && !middle.contains(string) && !last.contains(string)) {
                    resultList.remove(critic);
                }
            }
        }

        return resultList;
    }

    public Critic getCriticByUser(User user){
        return criticRepository.findByUser(user);
    }

}
