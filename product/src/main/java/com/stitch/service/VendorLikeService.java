package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.user.model.dto.UserDto;

import java.util.List;

public interface VendorLikeService {
    Response addToLikes(String vendorEmailAddress);
    Response removeFromLikes(String vendorEmailAddress);
    PaginatedResponse<List<UserDto>> getAllLikes(int page, int size);
}
