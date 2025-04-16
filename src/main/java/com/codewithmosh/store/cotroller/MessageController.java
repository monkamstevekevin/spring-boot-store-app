package com.codewithmosh.store.cotroller;

import com.codewithmosh.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/greet")
    public Message sayHello() {
        return new Message("Hello, World!");
    }
}
