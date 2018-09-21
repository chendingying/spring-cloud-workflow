package com.spring.cloud.form.inherit;

import com.spring.cloud.form.domain.RunByteArray;

/**
 * Created by CDZ on 2018/9/17.
 */
public class InheritRunByteArray extends RunByteArray {
    private String editorJson;

    public String getEditorJson() {
        return editorJson;
    }

    public void setEditorJson(String editorJson) {
        this.editorJson = editorJson;
    }
}
