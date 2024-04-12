package com.heatherpiper.controller;

import com.heatherpiper.service.SquiggleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

public class SseTestController {

    private final SquiggleService squiggleService;

    public SseTestController(SquiggleService squiggleService) {
        this.squiggleService = squiggleService;
    }

    @GetMapping(path = "/sse-test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void sseTest() {
        squiggleService.subscribeToTestStream();
    }

}
