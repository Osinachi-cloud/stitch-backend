package com.stitch.service.serviceImpl;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.exception.ProductException;
import com.stitch.model.entity.VendorLike;
import com.stitch.repository.VendorLikeRepository;
import com.stitch.service.VendorLikeService;
import com.stitch.user.model.dto.UserDto;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stitch.commons.util.SharedUtils.getLoggedInUser;

@Service
@Slf4j
public class VendorLikeServiceImpl implements VendorLikeService {

    private final VendorLikeRepository vendorLikeRepository;
    private final UserRepository userRepository;

    public VendorLikeServiceImpl(VendorLikeRepository vendorLikeRepository, UserRepository userRepository) {
        this.vendorLikeRepository = vendorLikeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response addToLikes(String vendorEmailAddress) {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = userRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));

            Optional<VendorLike> existing = vendorLikeRepository.findByVendorEmailAddressAndUserEntity(vendorEmailAddress, customer);
            if (existing.isPresent()) {
                vendorLikeRepository.delete(existing.get());
                return ResponseUtils.createResponse(204, "Tailor has been removed from like list");
            }

            userRepository.findByEmailAddress(vendorEmailAddress)
                    .orElseThrow(() -> new ProductException("Tailor with email : " + vendorEmailAddress + " does not exist", 404));

            VendorLike vendorLike = new VendorLike();
            vendorLike.setVendorEmailAddress(vendorEmailAddress);
            vendorLike.setUserEntity(customer);
            vendorLikeRepository.save(vendorLike);
            return ResponseUtils.createResponse(204, "Successfully added tailor to Likes");

        } catch (ProductException e) {
            log.error("Custom error adding vendor likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Exception occurred adding vendor likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }
    }

    @Override
    public Response removeFromLikes(String vendorEmailAddress) {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = userRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));
            VendorLike vendorLike = vendorLikeRepository.findByVendorEmailAddressAndUserEntity(vendorEmailAddress, customer)
                    .orElseThrow(() -> new ProductException("Vendor Like with email : " + vendorEmailAddress + " does not exist", 404));
            vendorLikeRepository.delete(vendorLike);
            return ResponseUtils.createDefaultSuccessResponse();
        } catch (Exception e) {
            log.error("Exception occurred removing vendor likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }
    }

    @Override
    public PaginatedResponse<List<UserDto>> getAllLikes(int page, int size) {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = userRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));

            Pageable pageRequest = PageRequest.of(page, size);
            Page<VendorLike> vendorLikes = vendorLikeRepository.findVendorLikesByUserEntity(customer, pageRequest);

            PaginatedResponse<List<UserDto>> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setPage(vendorLikes.getNumber());
            paginatedResponse.setSize(vendorLikes.getSize());
            paginatedResponse.setTotal(vendorLikeRepository.getLikeCount(username));
            paginatedResponse.setData(convertVendorLikeListToDto(vendorLikes.getContent()));
            return paginatedResponse;
        } catch (Exception e) {
            log.error("Exception occurred getting vendor likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }
    }

    private List<UserDto> convertVendorLikeListToDto(List<VendorLike> vendorLikeList) {
        return vendorLikeList.stream()
                .map(vl -> {
                    UserEntity vendor = userRepository.findByEmailAddress(vl.getVendorEmailAddress()).orElse(null);
                    if (vendor == null) return null;
                    UserDto dto = new UserDto();
                    dto.setFirstName(vendor.getFirstName());
                    dto.setLastName(vendor.getLastName());
                    dto.setEmailAddress(vendor.getEmailAddress());
                    dto.setPhoneNumber(vendor.getPhoneNumber());
                    dto.setProfileImage(vendor.getProfileImage());
                    dto.setShortBio(vendor.getShortBio());
                    return dto;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}
