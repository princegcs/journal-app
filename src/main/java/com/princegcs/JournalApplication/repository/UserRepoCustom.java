package com.princegcs.JournalApplication.repository;

import com.princegcs.JournalApplication.entity.User;

import java.util.List;

public interface UserRepoCustom {

    List<User> getUsersForSentimentAnalysis();
}
