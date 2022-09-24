package com.example.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MessageController {

    @GetMapping
    public ResponseEntity<Message> respondMessage() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Message("OK"));
    }
}
