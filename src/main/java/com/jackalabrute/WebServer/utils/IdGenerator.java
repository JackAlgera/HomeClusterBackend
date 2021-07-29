package com.jackalabrute.WebServer.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGenerator {

    public UUID getRandomId() {
        return UUID.randomUUID();
    }

}