package com.stitch.gateway.controller.user;


import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.service.BodyMeasurementService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stitch.gateway.util.Constants.BASE_URL;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(BASE_URL)
public class BodyMeasurementController {

    private final BodyMeasurementService bodyMeasurementService;

    public BodyMeasurementController(BodyMeasurementService bodyMeasurementService) {
        this.bodyMeasurementService = bodyMeasurementService;
    }

    @PostMapping("/create-body-measurement")
    public ResponseEntity<BodyMeasurementDto> createBodyMeasurement(@RequestBody BodyMeasurementRequest bodyMeasurementRequest) {
        return new ResponseEntity<>(bodyMeasurementService.createBodyMeasurement(bodyMeasurementRequest), CREATED);
    }

    @PutMapping("/update-body-measurement")
    public ResponseEntity<BodyMeasurementDto> updateBodyMeasurement(@RequestBody BodyMeasurementRequest bodyMeasurementRequest) {
        return ResponseEntity.ok(bodyMeasurementService.updateBodyMeasurement(bodyMeasurementRequest));
    }

    @GetMapping("/get-body-measurement-by-user")
    public ResponseEntity<List<BodyMeasurementDto>> getBodyMeasurementByUser() {
        return ResponseEntity.ok(bodyMeasurementService.getBodyMeasurementByUser());

    }

}
