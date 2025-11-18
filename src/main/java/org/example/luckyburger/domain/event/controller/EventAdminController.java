package org.example.luckyburger.domain.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.event.dto.request.EventCreateRequest;
import org.example.luckyburger.domain.event.dto.response.EventResponse;
import org.example.luckyburger.domain.event.service.EventAdminService;
import org.example.luckyburger.domain.event.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class EventAdminController {

    private final EventAdminService eventAdminService;
    private final EventService eventService;

    @Operation(summary = "이벤트 추가")
    @PostMapping("/v1/admin/events")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody EventCreateRequest request
    ) {
        return ApiResponse.created(eventAdminService.createEventResponse(request));
    }

    @Operation(summary = "이벤트 수정")
    @PutMapping("/v1/admin/events/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventCreateRequest request
    ) {
        return ApiResponse.success(eventAdminService.updateEventResponse(eventId, request));
    }

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/v1/admin/events/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {
        eventAdminService.deleteEvent(eventId);
        return ApiResponse.noContent();
    }

    @Operation(summary = "이벤트 전체 조회")
    @GetMapping("/v1/admin/events/all")
    public ResponseEntity<ApiPageResponse<EventResponse>> getAllEvent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ApiPageResponse.success(eventService.getAllEventResponse(pageable));
    }
}
