package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Status;

import java.util.List;


public interface StatusService {

    Status createStatus (Status status);

    Status findStatusById (Long id);

    List<Status> findAllWithKeyword (String keyword);

    List<Status> findAll();

    Status updateStatus (Status status);

    void deleteStatusById(Long statusId);

}
