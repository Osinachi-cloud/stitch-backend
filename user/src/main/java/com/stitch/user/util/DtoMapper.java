package com.stitch.user.util;

import com.stitch.user.model.dto.*;
import com.stitch.user.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.stitch.commons.util.Constants.getStr;

@Slf4j
public class DtoMapper {

    public static BodyMeasurement bodyMeasurementRequestToEntity(BodyMeasurementRequest bodyMeasurementRequest){
        BodyMeasurement bodyMeasurement = new BodyMeasurement();
        BeanUtils.copyProperties(bodyMeasurementRequest, bodyMeasurement);
        return bodyMeasurement;
    }

    public static BodyMeasurementDto bodyMeasurementEntityToDto(BodyMeasurement bodyMeasurement){
        BodyMeasurementDto bodyMeasurementDto = new BodyMeasurementDto();
        BeanUtils.copyProperties(bodyMeasurement, bodyMeasurementDto);
        return bodyMeasurementDto;
    }

    public static RoleDto mapRoleToDto(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        log.info("role permission: {}", role.getPermissions());

        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setDescription(role.getDescription());
        roleDto.setDateCreated(role.getDateCreated().toString());
        roleDto.setLastUpdated(role.getLastUpdated().toString());
        roleDto.setPermissionNames(mapToCollectionNamesToString(role.getPermissions()));
        roleDto.setPermissionsDto(mapToCollectionOfPermissionDto(role.getPermissions()));
        return roleDto;
    }

    public static List<String> mapToCollectionNamesToString(Collection<Permission> permissionCollection) {
        List<String> permissionNames = new ArrayList<>();
        for (Permission permission : permissionCollection) {
            if (permission != null) {
                String permissionName = permission.getName();
                permissionNames.add(permissionName);
            }
        }
        return permissionNames;
    }

    public static Collection<PermissionDto> mapToCollectionOfPermissionDto(Collection<Permission> permissionCollection) {
        Collection<PermissionDto> permissionDtos = new ArrayList<>();
        for (Permission permission : permissionCollection) {
            if (permission != null) {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setName(permission.getName());
                permissionDto.setId(permission.getId());
                permissionDto.setDescription(permission.getDescription());
                permissionDto.setCategory(permission.getCategory());
                permissionDtos.add(permissionDto);
            }
        }
        return permissionDtos;
    }

    public static RoleDto mapRoleToDtoLoginResponse(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setDescription(role.getDescription());
//        roleDto.setPermissionsDto(mapToCollectionOfPermissionDto(role.getPermissions()));
        System.out.println("=========== permissions");
        System.out.println(role.getPermissions().size());

        roleDto.setPermissionNames(mapToCollectionOfPermissionDtoLoginResponse(role.getPermissions()));
        return roleDto;
    }

    public static List<String> mapToCollectionOfPermissionDtoLoginResponse(Collection<Permission> permissionCollection) {
        List<String> stringList = new ArrayList<>();

        if (Objects.nonNull(permissionCollection) && !permissionCollection.isEmpty()) {
            for (Permission permission : permissionCollection) {
                log.info("permission_names : {}", permission.getName());
                if (!getStr(permission.getName()).isEmpty()) {
                    stringList.add(permission.getName());
                }
            }
        }


        System.out.println("================= string list");
        System.out.println(stringList);
        return stringList;
    }

    public static List<UserDto> convertUserListToDto(List<UserEntity> userEntityList) {

        return userEntityList.stream().map(userEntity -> {
            UserDto userDto = new UserDto();
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setEmailAddress(userEntity.getEmailAddress());
            userDto.setPhoneNumber(userEntity.getPhoneNumber());
            userDto.setRole(userEntity.getRole().getName());
            userDto.setProfileImage(userEntity.getProfileImage());
            userDto.setShortBio(userEntity.getShortBio());
           return  userDto;
        }).toList();

    }

    public static AddressDto mapAddressToDto(Address address){
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setFullAddress(address.getFullAddress());
        addressDto.setState(address.getState());
        addressDto.setStreet(address.getStreet());
        addressDto.setApartmentNumber(address.getApartmentNumber());
        addressDto.setHouseNumber(address.getHouseNumber());
        addressDto.setPostalCode(address.getPostalCode());
        addressDto.setId(address.getId());
        return addressDto;
    }

    public static Address mapAddressDtoToAddress(AddressDto addressDto){
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setFullAddress(addressDto.getFullAddress());
        address.setState(addressDto.getState());
        address.setStreet(addressDto.getStreet());
        address.setApartmentNumber(addressDto.getApartmentNumber());
        address.setHouseNumber(addressDto.getHouseNumber());
        address.setPostalCode(addressDto.getPostalCode());
        return address;
    }
}
