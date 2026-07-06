package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.enums.Sentiment;
import com.princegcs.JournalApplication.model.WeeklySentimentEvent;
import com.princegcs.JournalApplication.service.kafka.WeeklySentimentProducer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@EmbeddedKafka(
//        partitions = 1,
//        topics = {"weekly-sentiment-events"}
//)
@SpringBootTest
class WeeklySentimentProducerTests {

    @Autowired
    private WeeklySentimentProducer producer;

    @Disabled
    @Test
    void shouldPublishWeeklySentimentEvent() {

        WeeklySentimentEvent event = WeeklySentimentEvent.builder()
                .email("princegcsn@gmail.com")
                .sentiment(Sentiment.VERY_POSITIVE)
                .build();

        producer.publish(event);
    }
}


