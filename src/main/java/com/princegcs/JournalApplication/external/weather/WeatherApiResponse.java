 package com.princegcs.JournalApplication.external.weather;


import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

 @Data
 public class WeatherApiResponse {

     private Current current;

     @Data
     public static class Current {

         @JsonProperty("temp_c")
         private Double tempC;

         private Condition condition;
     }

     @Data
     public static class Condition {

         private String text;
     }
 }