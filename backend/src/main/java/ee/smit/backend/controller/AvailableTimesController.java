package ee.smit.backend.controller;

import ee.smit.backend.dto.AvailableTime;
import ee.smit.backend.dto.BookingRequest;
import ee.smit.backend.service.AvailableTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class AvailableTimesController {

    @Autowired
    private AvailableTimesService availableTimesService;

    @GetMapping("/available-times")
    public List<AvailableTime> getAvailableTimes() {
        return availableTimesService.getAvailableTimes();
    }

    @PostMapping("/book-time")
    public Mono<String> bookTime(@RequestBody BookingRequest bookingRequest) {
        String id = bookingRequest.getId();
        String contactInformation = bookingRequest.getContactInformation();
        String name = bookingRequest.getName();
        return availableTimesService.bookTime(id, contactInformation, name);
    }
}
