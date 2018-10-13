package com.spring.cloud.form.resource;


import com.spring.cloud.form.domain.ByteArray;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.form.repository.ByteArrayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CDZ on 2018/9/13.
 */
@RestController
public class FormEditorResource extends BaseResource {

    @Autowired
    ByteArrayRepository byteArrayRepository;

    @RequestMapping(value = "form-editor/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ByteArray editor(@PathVariable Integer id){
       return byteArrayRepository.findOne(id);
    }
}
