package io.intelligenta.communityportal.web;


import io.intelligenta.communityportal.models.BaseEntity;
import io.intelligenta.communityportal.service.BaseEntityCrudService;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public abstract class CrudResource<T extends BaseEntity, S extends BaseEntityCrudService<T>>  {

    public abstract S getService();


    public void beforeSave(T entity) {

    }

    public abstract T beforeUpdate(T oldEntity, T newEntity);


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<T> getAll(HttpServletRequest request) {
        Sort sort = RequestProcessor.sorting(request);
        Specification specification = RequestProcessor.getSpecifications(request);
        if (specification == null) {
            return getService().findAll(sort);
        }
        return getService().findAll(specification,sort);
    }

    @RequestMapping(value = "/paged", method = RequestMethod.GET, produces = "application/json")
    public Page<T> getAll(@RequestParam int page, @RequestParam int pageSize,
                          HttpServletRequest request) throws JSONException {
        return getPaged(page,pageSize,null,null,request);
    }

    protected Page<T> getPaged(int page, int pageSize, Specification<T> baseSpec,Specification<T> secondSpec, HttpServletRequest request) {
        Sort sort = RequestProcessor.sorting(request);
        Pageable pageable = PageRequest.of (page , pageSize, sort);
        Specification specification = RequestProcessor.getSpecifications(request);
        if (baseSpec!=null) {
            specification = Specification.where(specification).and(baseSpec).and(secondSpec);
        }
        if (specification != null) {
            return getService().findAll(specification, pageable);
        }

        return getService().findAll(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public T get(@PathVariable Long id, HttpServletResponse response) {
        Optional<T> entity = getService().findOne(id);
        if (!entity.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return entity.get();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void delete(@PathVariable Long id) {
        getService().delete(id);
    }


}
