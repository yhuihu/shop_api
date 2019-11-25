package com.study.shop.business.dto.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.dto.params
 **/
@Data
public class PasswordParam implements Serializable {

    private static final long serialVersionUID = -1474727095840529210L;
    private String username;
    private String oldPassword;
    private String newPassword;

}
