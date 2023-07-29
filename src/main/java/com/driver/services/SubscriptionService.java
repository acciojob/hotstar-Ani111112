package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
            subscription.setStartSubscriptionDate(new Date());
            subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
            if (subscriptionEntryDto.getSubscriptionType().toString().equals("BASIC")) {
                int cost = 500 + 200 * subscriptionEntryDto.getNoOfScreensRequired();
                subscription.setTotalAmountPaid(cost);
            } else if (subscriptionEntryDto.getSubscriptionType().toString().equals("PRO")) {
                int cost = 800 + 250 * subscriptionEntryDto.getNoOfScreensRequired();
                subscription.setTotalAmountPaid(cost);
            }else {
                int cost = 1000 + 350 * subscriptionEntryDto.getNoOfScreensRequired();
                subscription.setTotalAmountPaid(cost);
            }
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            user.setSubscription(subscription);
            User savedUser = userRepository.save(user);
            return savedSubscription.getTotalAmountPaid();
        }

        return 0;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        Subscription subscription = user.getSubscription();
        if (subscription.getSubscriptionType().toString().equals("ELITE"))
            throw new Exception("Already the best Subscription");

        int oldPrice = subscription.getTotalAmountPaid();

        if (subscription.getSubscriptionType().toString().equals("BASIC")) {
            subscription.setSubscriptionType(SubscriptionType.PRO);
            int cost = 800 + 250 * subscription.getNoOfScreensSubscribed();
            subscription.setTotalAmountPaid(cost);
            user.setSubscription(subscription);
            userRepository.save(user);
            return cost - oldPrice;
        }else if(subscription.getSubscriptionType().toString().equals("PRO")) {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            int cost = 1000 + 350 * subscription.getNoOfScreensSubscribed();
            subscription.setTotalAmountPaid(cost);
            user.setSubscription(subscription);
            userRepository.save(user);
            return cost - oldPrice;
        }
        return 0;
    }

    public Integer calculateTotalRevenueOfHotstar(){
        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> allSubscription = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for (Subscription subscription : allSubscription) {
            totalRevenue += subscription.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}
