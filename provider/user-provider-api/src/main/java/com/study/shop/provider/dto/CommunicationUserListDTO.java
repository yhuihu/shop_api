package com.study.shop.provider.dto;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author Tiger
 * @date 2020-03-23
 * @see com.study.shop.provider.dto
 **/
@Data
public class CommunicationUserListDTO {
    private String userId;
    private String nickName;
    private String icon;
    private Date createTime;
    private Integer isRead;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunicationUserListDTO)) {
            return false;
        }
        CommunicationUserListDTO that = (CommunicationUserListDTO) o;
        return userId.equals(that.userId) &&
                nickName.equals(that.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, nickName);
    }
}
