package com.stitch.user.service.impl;

import com.stitch.commons.exception.StitchException;
import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.Customer;
import com.stitch.user.repository.BodyMeasurementRepository;
import com.stitch.user.repository.CustomerRepository;
import com.stitch.user.service.BodyMeasurementService;
import com.stitch.user.util.DtoMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BodyMeasurementServiceImpl implements BodyMeasurementService {


    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final CustomerRepository customerRepository;

    public BodyMeasurementServiceImpl(BodyMeasurementRepository bodyMeasurementRepository, CustomerRepository customerRepository) {
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress){
         Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(customerEmailAddress);
         if(existingCustomer.isEmpty()){
             throw new StitchException("customer does not exist :" + customerEmailAddress);
         }
         BodyMeasurement bodyMeasurement = DtoMapper.bodyMeasurementRequestToEntity(bodyMeasurementRequest);
         bodyMeasurement.setCustomer(existingCustomer.get());
         BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

         return DtoMapper.bodyMeasurementEntityToDto(savedBodyMeasurement);
    }


    @Override
    public BodyMeasurementDto upDateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress){
        Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(customerEmailAddress);
        if(existingCustomer.isEmpty()){
            throw new StitchException("customer does not exist :" + customerEmailAddress);
        }
        Optional<BodyMeasurement> existingBodyMeasurement = bodyMeasurementRepository.findByCustomer(existingCustomer.get());
        if(existingBodyMeasurement.isEmpty()){
            throw new StitchException("Body measurement has not been created :" + customerEmailAddress);
        }
        BodyMeasurement bodyMeasurement = existingBodyMeasurement.get();
//                DtoMapper.bodyMeasurementRequestToEntity(bodyMeasurementRequest);
        bodyMeasurement.setKnee(bodyMeasurementRequest.getKnee());
        bodyMeasurement.setAnkle(bodyMeasurementRequest.getAnkle());
        bodyMeasurement.setNeck(bodyMeasurementRequest.getNeck());
        bodyMeasurement.setChest(bodyMeasurementRequest.getChest());
        bodyMeasurement.setHipWidth(bodyMeasurementRequest.getHipWidth());
        bodyMeasurement.setThigh(bodyMeasurementRequest.getThigh());
        bodyMeasurement.setLongSleeveAtWrist(bodyMeasurementRequest.getLongSleeveAtWrist());
        bodyMeasurement.setNeckToHipLength(bodyMeasurementRequest.getNeckToHipLength());
        bodyMeasurement.setMidSleeveAtElbow(bodyMeasurementRequest.getMidSleeveAtElbow());
        bodyMeasurement.setShortSleeveAtBiceps(bodyMeasurementRequest.getShortSleeveAtBiceps());
        bodyMeasurement.setTrouserLength(bodyMeasurementRequest.getTrouserLength());

        bodyMeasurement.setCustomer(existingCustomer.get());
        BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

        return DtoMapper.bodyMeasurementEntityToDto(savedBodyMeasurement);
    }

}
