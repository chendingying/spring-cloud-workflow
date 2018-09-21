package com.spring.cloud.form.resource.table;

import com.spring.cloud.form.domain.ByteArray;
import com.spring.cloud.form.repository.ByteArrayRepository;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.form.constant.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cdy
 * @create 2018/9/12
 */
@RestController
public class FormByteArrayResource extends BaseResource {
    @Autowired
    ByteArrayRepository byteArrayRepository;

    private ByteArray getFormByteArrayFromRequest(Integer id) {
        ByteArray byteArray = byteArrayRepository.findOne(id);
        if (byteArray == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        String a = new String(byteArray.getContentByte());
        System.out.println(a);
//        byteArray.setContent(new String(byteArray.getContentByte()));
        return byteArray;
    }

    @GetMapping(value = "/form-byteArray/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ByteArray getFormDefinition(@PathVariable Integer id) {
        return getFormByteArrayFromRequest(id);
    }


}
