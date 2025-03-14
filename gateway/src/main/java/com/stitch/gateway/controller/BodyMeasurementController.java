package com.stitch.gateway.controller;


import com.stitch.commons.exception.StitchException;
import com.stitch.gateway.security.model.CustomUserDetails;
import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.service.BodyMeasurementService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BodyMeasurementController {

    private final BodyMeasurementService bodyMeasurementService;

    public BodyMeasurementController(BodyMeasurementService bodyMeasurementService) {
        this.bodyMeasurementService = bodyMeasurementService;
    }

    @MutationMapping(value = "createBodyMeasurement")
    public BodyMeasurementDto createBodyMeasurement(@Argument("bodyMeasurementRequest")BodyMeasurementRequest bodyMeasurementRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return bodyMeasurementService.createBodyMeasurement(bodyMeasurementRequest, customUserDetails.getUsername());
    }

    @MutationMapping(value = "updateBodyMeasurement")
    public BodyMeasurementDto updateBodyMeasurement(@Argument("bodyMeasurementRequest")BodyMeasurementRequest bodyMeasurementRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return bodyMeasurementService.upDateBodyMeasurement(bodyMeasurementRequest, customUserDetails.getUsername());
    }

    @QueryMapping(value = "getBodyMeasurementByUser")
    public List<BodyMeasurementDto> getBodyMeasurementByUser(){
        try {
            return bodyMeasurementService.getBodyMeasurementByUser();
        } catch (Exception e){
            throw new StitchException("Error Occurred: " + e.getMessage());
        }
    }

}
