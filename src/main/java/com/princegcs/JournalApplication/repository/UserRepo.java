package com.princegcs.JournalApplication.repository;

import com.princegcs.JournalApplication.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepo extends MongoRepository<User, ObjectId>, UserRepoCustom {

    Optional<User> findByUserName(String username);

}
