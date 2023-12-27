package com.stitch.notification.service.impl;


import com.stitch.commons.util.NumberUtils;
import com.stitch.notification.exception.NotificationException;
import com.stitch.notification.model.InAppNotification;
import com.stitch.notification.model.dto.InAppNotificationRequest;
import com.stitch.notification.model.dto.InAppNotificationResponse;
import com.stitch.notification.model.dto.InAppNotificationStatsResponse;
import com.stitch.notification.repository.InAppNotificationRepository;
import com.stitch.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InAppNotificationServiceImpl implements InAppNotificationService {

    private final InAppNotificationRepository inAppNotificationRepository;



    public void saveInAppNotification(InAppNotificationRequest request){
        InAppNotification appNotification = mapToEntity(request);
        inAppNotificationRepository.saveAndFlush(appNotification);
    }

    private InAppNotification mapToEntity(InAppNotificationRequest request) {
        InAppNotification appNotification = new InAppNotification();
        appNotification.setCustomerId(request.getCustomerId());
        appNotification.setNotificationId(NumberUtils.generate(20));
        appNotification.setSubject(request.getSubject());
        appNotification.setContent(request.getContent());
        appNotification.setSeverity(request.getSeverity());
        return appNotification;
    }

    public void updateReadInAppNotification(String notificationId, String customerId){
        InAppNotification appNotification = getByCustomerAndNotificationId(notificationId, customerId);
        appNotification.setRead(true);
        inAppNotificationRepository.saveAndFlush(appNotification);
    }

    @Transactional
    public void deleteInAppNotification(String notificationId, String customerId){
        InAppNotification appNotification = getByCustomerAndNotificationId(notificationId, customerId);
        inAppNotificationRepository.deleteByNotificationId(notificationId);
    }


    private InAppNotification getByCustomerAndNotificationId(String notificationId, String customerId) {
        return inAppNotificationRepository.findByCustomerIdAndNotificationId(customerId, notificationId)
            .orElseThrow(() -> new NotificationException("notification not found"));
    }

    public List<InAppNotificationResponse> fetchAllCustomerNotification(String customerId, int page, int size){
        return inAppNotificationRepository.findAllByCustomerId(customerId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))).stream()
            .map(this::mapToDto).collect(Collectors.toList());
    }

    private InAppNotificationResponse mapToDto(InAppNotification appNotification) {
        InAppNotificationResponse response = new InAppNotificationResponse();
        response.setCustomerId(appNotification.getCustomerId());
        response.setNotificationId(appNotification.getNotificationId());
        response.setSubject(appNotification.getSubject());
        response.setContent(appNotification.getContent());
        response.setRead(appNotification.isRead());
        response.setDateTime(appNotification.getDateCreated().toString());
        response.setSeverity(appNotification.getSeverity());
        return response;
    }

    public void saveBulkInAppNotification(List<InAppNotificationRequest> request){
        List<InAppNotification> appNotification = request.stream()
            .map(this::mapToEntity).collect(Collectors.toList());
        inAppNotificationRepository.saveAll(appNotification);
    }

    @Override
    public InAppNotificationStatsResponse customerInAppNotificationStats(String customerId){

        long read = inAppNotificationRepository.countAllByCustomerIdAndRead(customerId, true);

        long total = inAppNotificationRepository.countAllByCustomerId(customerId);

        return InAppNotificationStatsResponse.builder()
            .total(total)
            .unread(total - read)
            .read(read)
            .build();
    }
}
