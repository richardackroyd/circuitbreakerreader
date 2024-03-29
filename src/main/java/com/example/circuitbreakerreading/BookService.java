package com.example.circuitbreakerreading;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.net.URI;

@Service
public class BookService {

  private final RestTemplate restTemplate;
  @Value("${bookstore.server}")
  String bookstoreServer;

  public BookService(RestTemplate rest) {
    this.restTemplate = rest;
  }

  public String readingListNoHystrix() {
    URI uri = URI.create(bookstoreServer + "/recommended");

    return this.restTemplate.getForObject(uri, String.class);
  }

  public String readingListDelayedNoHystrix() {
    URI uri = URI.create(bookstoreServer + "/recommended-delayed");

    return this.restTemplate.getForObject(uri, String.class);
  }

  @HystrixCommand(fallbackMethod = "reliable")
  public String readingList() {
    URI uri = URI.create(bookstoreServer + "/recommended");

    return this.restTemplate.getForObject(uri, String.class);
  }

  @HystrixCommand(fallbackMethod = "reliable")
  public String readingListDelayed() {
    URI uri = URI.create(bookstoreServer + "/recommended-delayed");

    return this.restTemplate.getForObject(uri, String.class);
  }

  public String reliable() {
    return "Cloud Native Java (O'Reilly) - returned as a fallbackMethod via Hystrix because the Bookstore service is not stable";
  }

}
