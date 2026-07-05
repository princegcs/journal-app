package com.princegcs.JournalApplication.repository;
import com.princegcs.JournalApplication.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

public interface JournalEntryRepo extends  MongoRepository<JournalEntry, ObjectId>{

}
