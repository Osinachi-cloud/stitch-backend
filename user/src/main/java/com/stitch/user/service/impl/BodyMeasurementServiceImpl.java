package com.stitch.user.service.impl;

import com.stitch.user.exception.UserException;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.stitch.commons.util.SharedUtils.getLoggedInUser;
import static com.stitch.commons.util.SharedUtils.validateField;
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
    public BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest){
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new UserException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new UserException("User with username : " + username + " does not exist", 404));

            if(bodyMeasurementRepository.findBodyMeasurementByUserEntity(customer).size() >= 10){
                throw new UserException("You can not have more than 10 measurement", 417);
            }
            validateField(bodyMeasurementRequest.getTag(), "Measurement Tag");
            if(bodyMeasurementRepository.findBodyMeasurementByTag(bodyMeasurementRequest.getTag()).isPresent()){
                throw new UserException("Body measurement with tag :" + bodyMeasurementRequest.getTag() + "already exists", 404);
            }
            BodyMeasurement bodyMeasurement = DtoMapper.bodyMeasurementRequestToEntity(bodyMeasurementRequest);
            bodyMeasurement.setUserEntity(customer);
            BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

            return bodyMeasurementEntityToDto(savedBodyMeasurement);
        }catch (UserException e){
            log.error("Custom error occurred creating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(),e.getCode());
        }catch (Exception e){
            log.error("An error occurred while creating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(),400);
        }

    }


    @Override
    public BodyMeasurementDto updateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest){

        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new UserException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new UserException("User with username : " + username + " does not exist", 404));
            BodyMeasurement bodyMeasurement = bodyMeasurementRepository.findByUserEntity(customer)
                    .orElseThrow(() -> new UserException("Body measurement has not been created :" + username, 404));

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
            bodyMeasurement.setUserEntity(customer);
            BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

            return bodyMeasurementEntityToDto(savedBodyMeasurement);
        }catch (UserException e){
            log.error("Custom error updating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(),e.getCode());
        }catch (Exception e){
            log.error("An error occurred while updating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(),400);
        }

    }

    @Override
    public List<BodyMeasurementDto> getBodyMeasurementByUser(){
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new UserException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new UserException("User with username : " + username + " does not exist", 404));
            List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findBodyMeasurementByUserEntity(customer);
            log.info("bodyMeasurementList :{}", bodyMeasurementList);
            return bodyMeasurementList.stream().map(DtoMapper::bodyMeasurementEntityToDto).collect(Collectors.toList());

        }catch (UserException e){
            log.error("Custom error getting user measurement by type : {}", e.getMessage());
            throw new UserException(e.getMessage(),e.getCode());
        }catch (Exception e){
            log.error("An error occurred getting user measurement by type : {}", e.getMessage());
            throw new UserException(e.getMessage(),400);
        }
    }

//    @PostConstruct
//    public void getThis(){
//        List<BodyMeasurement> list =  bodyMeasurementRepository.findAll();
//        log.info("list : >>>>>>>>> : {}", list);
//    }

}
