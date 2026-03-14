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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    @Transactional
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
                throw new UserException("Body measurement with tag :" + bodyMeasurementRequest.getTag() + " already exists", 404);
            }

            BodyMeasurement bodyMeasurement = DtoMapper.bodyMeasurementRequestToEntity(bodyMeasurementRequest);
            bodyMeasurement.setUserEntity(customer);

            // Handle default measurement logic
            handleDefaultMeasurement(customer, bodyMeasurement, bodyMeasurementRequest.isDefault());

            BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

            return bodyMeasurementEntityToDto(savedBodyMeasurement);
        } catch (UserException e){
            log.error("Custom error occurred creating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(), e.getCode());
        } catch (Exception e){
            log.error("An error occurred while creating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(), 400);
        }
    }

    @Override
    @Transactional
    public BodyMeasurementDto updateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest){
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new UserException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new UserException("User with username : " + username + " does not exist", 404));

            BodyMeasurement bodyMeasurement = bodyMeasurementRepository.findBodyMeasurementByTagAndUserEntity(bodyMeasurementRequest.getTag(), customer)
                    .orElseThrow(() -> new UserException("Body measurement has not been created :" + username, 404));

            // Update all fields
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
//            bodyMeasurement.setDefault(bodyMeasurementRequest.isDefault());
            bodyMeasurement.setUserEntity(customer);

            // Handle default measurement logic
            handleDefaultMeasurement(customer, bodyMeasurement, bodyMeasurementRequest.isDefault());

            BodyMeasurement savedBodyMeasurement = bodyMeasurementRepository.save(bodyMeasurement);

            return bodyMeasurementEntityToDto(savedBodyMeasurement);
        } catch (UserException e){
            log.error("Custom error updating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(), e.getCode());
        } catch (Exception e){
            log.error("An error occurred while updating body measurement : {}", e.getMessage());
            throw new UserException(e.getMessage(), 400);
        }
    }

    /**
     * Handles the default measurement logic for both create and update operations
     * @param customer The user entity
     * @param bodyMeasurement The body measurement to be processed
     * @param requestedIsDefault The isDefault value from the request
     */

    private void handleDefaultMeasurement(UserEntity customer, BodyMeasurement bodyMeasurement, Boolean requestedIsDefault) {
        System.out.println("is default " + requestedIsDefault);

        if (Boolean.TRUE.equals(requestedIsDefault)) {
            System.out.println("got here 1");
            // Use email instead of ID
            bodyMeasurementRepository.updateAllUserMeasurementsToNonDefault(customer.getEmailAddress());
            bodyMeasurement.setDefault(true);

        } else if (Boolean.FALSE.equals(requestedIsDefault)) {
            System.out.println("got here 2");
            boolean isCurrentlyDefault = bodyMeasurement.isDefault();

            if (isCurrentlyDefault && bodyMeasurement.getId() != null) {
                long defaultCount = bodyMeasurementRepository.countByUserEntityAndIsDefaultTrue(customer);

                if (defaultCount <= 1) {
                    throw new UserException("Cannot unset the default measurement. Please set another measurement as default first.", 400);
                }
            }
            bodyMeasurement.setDefault(false);
        } else {
            if (bodyMeasurement.getId() == null) { // Create scenario
                boolean hasDefault = bodyMeasurementRepository.existsByUserEntityAndIsDefaultTrue(customer);
                bodyMeasurement.setDefault(!hasDefault);
            }
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
            return bodyMeasurementList.stream()
                    .map(DtoMapper::bodyMeasurementEntityToDto)
                    .sorted((a, b) -> {
                        if (a.isDefault() && !b.isDefault()) return -1;
                        if (!a.isDefault() && b.isDefault()) return 1;
                        return 0;
                    })
                    .collect(Collectors.toList());

        }catch (UserException e){
            log.error("Custom error getting user measurement by type : {}", e.getMessage());
            throw new UserException(e.getMessage(),e.getCode());
        }catch (Exception e){
            log.error("An error occurred getting user measurement by type : {}", e.getMessage());
            throw new UserException(e.getMessage(),400);
        }
    }

    @Override
    @Transactional  // Add this annotation to enable transaction for delete operation
    public void deleteBodyMeasurement(String tag, String email) {
        bodyMeasurementRepository.deleteBodyMeasurementByTagAndUserEmail(tag, email);
    }

    @Override
    public BodyMeasurementDto getBodyMeasurementByUserTag(String tag, String email) {
        BodyMeasurement bodyMeasurement = bodyMeasurementRepository.findBodyMeasurementByTagAndUserEntity_EmailAddress(tag,email).orElseThrow(()-> new UserException("No user and tag found"));
        return bodyMeasurementEntityToDto(bodyMeasurement);
    }

}
