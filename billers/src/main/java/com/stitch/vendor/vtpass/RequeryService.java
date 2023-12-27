package com.stitch.vendor.vtpass;

import com.stitch.model.dtos.VTPassRequeryRequest;
import com.stitch.model.dtos.VTPassRequeryResponse;

public interface RequeryService {

    VTPassRequeryResponse query(VTPassRequeryRequest request);
}
