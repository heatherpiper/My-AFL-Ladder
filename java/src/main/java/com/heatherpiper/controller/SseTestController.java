package com.heatherpiper.controller;

import com.heatherpiper.service.SquiggleService;
import org.springframework.web.bind.annotation.GetMapping;

public class SseTestController {

    private final SquiggleService squiggleService;

    public SseTestController(SquiggleService squiggleService) {
        this.squiggleService = squiggleService;
    }

    @GetMapping("/sse-test")
    public void sseTest() {
        squiggleService.subscribeToTestStream();
    }

}
