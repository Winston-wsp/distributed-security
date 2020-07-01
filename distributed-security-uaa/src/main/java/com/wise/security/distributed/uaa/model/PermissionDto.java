package com.wise.security.distributed.uaa.model;

import lombok.Data;

/**
 * @author: Winston
 * @createTime: 2020/6/27
 */
@Data
public class PermissionDto {

    private String id;
    private String code;
    private String description;
    private String url;
}
