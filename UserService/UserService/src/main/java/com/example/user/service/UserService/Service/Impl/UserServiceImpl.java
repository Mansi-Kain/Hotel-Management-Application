package com.example.user.service.UserService.Service.Impl;

import com.example.user.service.UserService.Entities.Hotel;
import com.example.user.service.UserService.Entities.Rating;
import com.example.user.service.UserService.Entities.User;
import com.example.user.service.UserService.Exceptions.ResourceNotFoundException;
import com.example.user.service.UserService.External.services.HotelService;
import com.example.user.service.UserService.Repository.UserRepository;
import com.example.user.service.UserService.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUserId= UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    //get single user
    @Override
    public User getUser(String userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given id not found"));
        //fetching rating of the above user from RATING-SERVICE
        //http://localhost:8083/ratings/users/ff9dc1b6-37a6-49b9-8140-87bd7a1466bb

        Rating[] ratingByUser=restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+ user.getUserId(), Rating[].class);
        logger.info("{}",ratingByUser);
       List<Rating> ratings = Arrays.stream(ratingByUser).toList();
       List<Rating> ratingList = ratings.stream().map(rating->{
//            //api call to hotel service to get the hotel
//            //http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
//            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }
}
