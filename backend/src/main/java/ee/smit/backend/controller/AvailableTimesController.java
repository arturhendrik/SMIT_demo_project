package ee.smit.backend.controller;

import ee.smit.backend.dto.AvailableTime;
import ee.smit.backend.service.AvailableTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AvailableTimesController {

    @Autowired
    private AvailableTimesService availableTimesService;

    @GetMapping("/available-times")
    public List<AvailableTime> getAvailableTimes() {
        return availableTimesService.getAvailableTimes();
    }
}
