package com.miyu.module.pdm.controller.admin.dataobject.vo;

import java.io.Serializable;

public class WinDataObjectAttrs implements Serializable {

    private static final long serialVersionUID = 1L;

    private String attr_name;
    private String attr_alias;
    private String attr_type;
    private String attr_length;

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public String getAttr_alias() {
        return attr_alias;
    }

    public void setAttr_alias(String attr_alias) {
        this.attr_alias = attr_alias;
    }

    public String getAttr_type() {
        return attr_type;
    }

    public void setAttr_type(String attr_type) {
        this.attr_type = attr_type;
    }

    public String getAttr_length() {
        return attr_length;
    }

    public void setAttr_length(String attr_length) {
        this.attr_length = attr_length;
    }

}

