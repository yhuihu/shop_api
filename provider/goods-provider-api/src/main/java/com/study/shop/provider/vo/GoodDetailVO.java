package com.study.shop.provider.vo;

import com.study.shop.provider.domain.TbItem;
import lombok.Data;

/**
 * @author Tiger
 * @date 2019-12-25
 * @see com.study.shop.provider.vo
 **/
@Data
public class GoodDetailVO extends TbItem {
    private static final long serialVersionUID = 1774495741412050105L;
    private String itemDesc;
    private String icon;
    private String address;
    private String nickName;
}
