package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        int count = 0;
        for (WebSeries webSeries : webSeriesList) {
            if (user.getSubscription().getSubscriptionType().toString().equals("ELITE")) {
                if (user.getAge() >= webSeries.getAgeLimit())count++;
            }else if (user.getSubscription().getSubscriptionType().toString().equals("PRO")) {
                if (webSeries.getSubscriptionType().toString().equals("ELITE") ||
                        user.getAge() < webSeries.getAgeLimit())continue;

                count++;
            }else {
                if (webSeries.getSubscriptionType().toString().equals("ELITE") ||
                        webSeries.getSubscriptionType().toString().equals("PRO") ||
                        user.getAge() < webSeries.getAgeLimit())continue;
                count++;
            }
        }
        return count;
    }


}
