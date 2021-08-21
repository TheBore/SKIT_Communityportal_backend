package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.exceptions.StatusCanNotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.StatusNotFoundException;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public StatusServiceImpl (StatusRepository statusRepository){
        this.statusRepository = statusRepository;
    }

    @Override
    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public Status findStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(StatusNotFoundException::new);
    }

    @Override
    public List<Status> findAllWithKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("") && !keyword.equals("null") && !keyword.equals("undefined")){
            return statusRepository.findAllByKeyword(keyword);
        }
        else{
            return statusRepository.findAll();
        }
    }

    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Status updateStatus(Status status) {
        Status updatedStatus = statusRepository.findById(status.getId()).orElseThrow(StatusNotFoundException::new);

        updatedStatus.setStatusMk(status.getStatusMk());
        updatedStatus.setStatusAl(status.getStatusAl());
        updatedStatus.setStatusEn(status.getStatusEn());
        updatedStatus.setStatusType(status.getStatusType());

        if(status.getStatusType() != null){
            updatedStatus.setStatusType(status.getStatusType());
        }
        else{
            updatedStatus.setStatusType(updatedStatus.getStatusType());
        }

        return statusRepository.save(updatedStatus);
    }

    @Override
    public void deleteStatusById(Long statusId) {
     Status status = statusRepository.findById(statusId).orElseThrow(StatusNotFoundException::new);
     if(status.getActivities().size() > 0 || status.getMeasures().size() > 0 || status.getNaps().size() > 0 || status.getIndicators().size() > 0){
         throw new StatusCanNotBeDeletedException();
     }
     else{
         statusRepository.delete(status);
     }

    }
}
