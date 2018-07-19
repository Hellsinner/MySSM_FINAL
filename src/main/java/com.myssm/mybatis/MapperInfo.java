package com.myssm.mybatis;

import java.util.List;

public class MapperInfo {

    private String type;

    private String nameSpace;

    private String id;

    private String ParamType;

    private String reurnType;

    private String sql;

    private List<String> paramMapping;

    public List<String> getParamMapping() {
        return paramMapping;
    }

    public void setParamMapping(List<String> paramMapping) {
        this.paramMapping = paramMapping;
    }

    public String getSql() {
        return sql;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamType() {
        return ParamType;
    }

    public void setParamType(String paramType) {
        ParamType = paramType;
    }

    public String getReurnType() {
        return reurnType;
    }

    public void setReurnType(String reurnType) {
        this.reurnType = reurnType;
    }
}
