package com.eventually.backend.service;

import com.eventually.backend.model.HelloModel;

public class HelloService {
    public String getMessage() {
        return new HelloModel().getMessage();
    }
}