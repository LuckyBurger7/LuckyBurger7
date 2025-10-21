package org.example.luckyburger.domain.event.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.event.dto.response.EventResponse;
import org.example.luckyburger.domain.event.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @GetMapping("/v1/events/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable Long eventId) {
        return ApiResponse.success(eventService.getEventResponse(eventId));
    }

    @GetMapping("/v1/events/all")
    public ResponseEntity<ApiPageResponse<EventResponse>> getAllEvent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ApiPageResponse.success(eventService.getAllEventResponse(pageable));
    }

    @GetMapping("/v1/events")
    public ResponseEntity<ApiPageResponse<EventResponse>> getAllEventByNotDeleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ApiPageResponse.success(eventService.getAllEventByNotDeletedResponse(pageable));
    }
}
