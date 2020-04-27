package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.domain.TbLog;
import com.study.shop.provider.dto.AdminSearchDTO;

/**
 * @author admin
 */
public interface TbLogService {

    /**
     * 插入日志
     *
     * @param tbLog log
     * @return 0, 1
     */
    int insertLog(TbLog tbLog);

    /**
     * 分页查询日志
     * @param adminSearchDTO {@link AdminSearchDTO}
     * @return {@link PageInfo}
     */
    PageInfo<TbLog> getLogByPage(AdminSearchDTO adminSearchDTO);
}

