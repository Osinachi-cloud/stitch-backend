package com.stitch.gateway.controller.vendorLike;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.model.dto.PageRequest;
import com.stitch.service.VendorLikeService;
import com.stitch.user.model.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stitch.gateway.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class VendorLikeController {
    private final VendorLikeService vendorLikeService;

    public VendorLikeController(VendorLikeService vendorLikeService) {
        this.vendorLikeService = vendorLikeService;
    }

    @PostMapping("/add-vendor-likes")
    public ResponseEntity<Response> addVendorLikes(@RequestBody Map<String, String> body) {
        String vendorEmail = body.get("vendorEmail");
        return ResponseEntity.ok(vendorLikeService.addToLikes(vendorEmail));
    }

    @PostMapping("/add-tailor-likes/{vendorEmail}")
    public ResponseEntity<Response> addTailorLikes(@PathVariable String vendorEmail) {
        return ResponseEntity.ok(vendorLikeService.addToLikes(vendorEmail));
    }

    @DeleteMapping("/delete-tailor-like/{vendorEmail}")
    public ResponseEntity<Response> deleteTailorLike(@PathVariable String vendorEmail) {
        return ResponseEntity.ok(vendorLikeService.removeFromLikes(vendorEmail));
    }

    @GetMapping("/get-all-vendor-likes")
    public ResponseEntity<PaginatedResponse<List<UserDto>>> getAllVendorLikes(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page.orElse(0));
        pageRequest.setSize(size.orElse(5));
        return ResponseEntity.ok(vendorLikeService.getAllLikes(pageRequest.getPage(), pageRequest.getSize()));
    }

    @GetMapping("/get-all-tailor-likes")
    public ResponseEntity<PaginatedResponse<List<UserDto>>> getAllTailorLikes(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page.orElse(0));
        pageRequest.setSize(size.orElse(5));
        return ResponseEntity.ok(vendorLikeService.getAllLikes(pageRequest.getPage(), pageRequest.getSize()));
    }
}
