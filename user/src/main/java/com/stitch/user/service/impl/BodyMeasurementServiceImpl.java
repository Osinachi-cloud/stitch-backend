package com.stitch.user.service.impl;

import com.stitch.commons.exception.StitchException;
import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.BodyMeasurementRepository;
import com.stitch.user.repository.UserRepository;
import com.stitch.user.service.BodyMeasurementService;
import com.stitch.user.util.DtoMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stitch.user.util.DtoMapper.bodyMeasurementEntityToDto;


@Service
@Slf4j
public class BodyMeasurementServiceImpl implements BodyMeasurementService {


    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final UserRepository customerRepository;

    public BodyMeasurementServiceImpl(BodyMeasurementRepository bodyMeasurementRepository, UserRepository customerRepository) {
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress){
         Optional<UserEntity> existingCustomer = customerRepository.findByEmailAddress(customerEmailAddress);
         if(existingCustomer.isEmpty()){
             throw new StitchException("customer does not exist :" + customerEmailAddress);
         }
         if(bodyMeasurementRepository.findBodyMeasurementByUserEntity(existingCustomer.get()).size() >= 10){
             throw new StitchException("You can not have more than 10 measurement");
         }
         if(bodyMeasurementRepository.findBodyMeasurementByTag(bodyMeasurementRequest.getTag()).isPresent()){
             throw new StitchException("Body measurement with tag :" + bodyMeasurementRequest.getTag() + "already exists");
         }
         BodyMeasurement bodyMeasurement = DtoMapper.bodyMeasurementRequestToEntity(bodyMeasurementRequest);
         bodyMeasurement.setUserEntity(existingCustomer.get());
         BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

         return bodyMeasurementEntityToDto(savedBodyMeasurement);
    }


    @Override
    public BodyMeasurementDto upDateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress){
        Optional<UserEntity> existingCustomer = customerRepository.findByEmailAddress(customerEmailAddress);
        if(existingCustomer.isEmpty()){
            throw new StitchException("customer does not exist :" + customerEmailAddress);
        }
        Optional<BodyMeasurement> existingBodyMeasurement = bodyMeasurementRepository.findByUserEntity(existingCustomer.get());
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

        bodyMeasurement.setUserEntity(existingCustomer.get());
        BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

        return bodyMeasurementEntityToDto(savedBodyMeasurement);
    }

    @Override
    public List<BodyMeasurementDto> getBodyMeasurementByUser(){
        System.out.println("hello here");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String customerEmailAddress = authentication.getName();
        log.info("customerEmailAddress :{}", customerEmailAddress);

        Optional<UserEntity> existingCustomer = customerRepository.findByEmailAddress(customerEmailAddress);
        if(existingCustomer.isEmpty()){
            throw new StitchException("customer does not exist :" + customerEmailAddress);
        }
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findBodyMeasurementByUserEntity(existingCustomer.get());
        log.info("bodyMeasurementList :{}", bodyMeasurementList);
        return bodyMeasurementList.stream().map(DtoMapper::bodyMeasurementEntityToDto).collect(Collectors.toList());
    }

    @PostConstruct
    public void getThis(){
        List<BodyMeasurement> list =  bodyMeasurementRepository.findAll();
        log.info("list : >>>>>>>>> : {}", list);
    }

}
