package com.princegcs.JournalApplication.repository;

import com.princegcs.JournalApplication.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepoImpl implements UserRepoCustom{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<User> getUsersForSentimentAnalysis() {

        Query query = new Query();

        query.addCriteria(Criteria.where("email").ne(null));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        return mongoTemplate.find(query, User.class);
    }
}
