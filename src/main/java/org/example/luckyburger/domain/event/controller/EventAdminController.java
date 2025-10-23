package org.example.luckyburger.domain.event.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.event.dto.request.EventCreateRequest;
import org.example.luckyburger.domain.event.dto.response.EventResponse;
import org.example.luckyburger.domain.event.service.EventAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class EventAdminController {

    private final EventAdminService eventAdminService;

    @PostMapping("/v1/admin/events")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody EventCreateRequest request
    ) {
        return ApiResponse.created(eventAdminService.createEventResponse(request));
    }

    @PutMapping("/v1/admin/events/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventCreateRequest request
    ) {
        return ApiResponse.success(eventAdminService.updateEventResponse(eventId, request));
    }

    @DeleteMapping("/v1/admin/events/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {
        eventAdminService.deleteEvent(eventId);
        return ApiResponse.noContent();
    }
}
