package com.study.shop.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbLogService;
import com.study.shop.provider.domain.TbLog;
import com.study.shop.provider.dto.AdminSearchDTO;
import com.study.shop.provider.mapper.TbLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Service(version = "1.0.0", timeout = 60000, retries = 0)
@Slf4j
public class TbLogServiceImpl implements TbLogService {

    @Resource
    private TbLogMapper tbLogMapper;

    @Override
    public int insertLog(TbLog tbLog) {
        try {
            tbLogMapper.insert(tbLog);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public PageInfo<TbLog> getLogByPage(AdminSearchDTO adminSearchDTO) {
        Example example = new Example(TbLog.class);
        Example.Criteria criteria = example.createCriteria();
        if (adminSearchDTO.getKeyword() != null && !"".equals(adminSearchDTO.getKeyword())) {
            criteria.orLike("name", adminSearchDTO.getKeyword()).orLike("ip", adminSearchDTO.getKeyword());
        }
        if (adminSearchDTO.getStartTime() != null && !"".equals(adminSearchDTO.getStartTime())) {
            criteria.andGreaterThanOrEqualTo("createDate", adminSearchDTO.getStartTime());
        }
        if (adminSearchDTO.getEndTime() != null && !"".equals(adminSearchDTO.getEndTime())) {
            criteria.andLessThanOrEqualTo("createDate", adminSearchDTO.getEndTime());
        }
        initPageParams(adminSearchDTO);
        List<TbLog> tbLogs = tbLogMapper.selectByExample(example);
        return new PageInfo<>(tbLogs);
    }

    static void initPageParams(AdminSearchDTO adminSearchDTO) {
        if (adminSearchDTO.getPage() == null || adminSearchDTO.getPage() <= 0) {
            adminSearchDTO.setPage(1);
        }
        if (adminSearchDTO.getSize() == null || adminSearchDTO.getSize() <= 0) {
            adminSearchDTO.setSize(5);
        }
        PageHelper.startPage(adminSearchDTO.getPage(), adminSearchDTO.getSize());
    }
}

