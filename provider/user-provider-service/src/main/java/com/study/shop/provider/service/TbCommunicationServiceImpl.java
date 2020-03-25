package com.study.shop.provider.service;

import com.study.shop.provider.api.TbCommunicationService;
import com.study.shop.provider.domain.TbCommunication;
import com.study.shop.provider.mapper.TbCommunicationMapper;
import com.study.shop.provider.vo.MessageVO;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Service(version = "1.0.0", parameters = {"insertMessage.retries", "0", "insertMessage.timeout", "30000"})
public class TbCommunicationServiceImpl implements TbCommunicationService {

    @Resource
    private TbCommunicationMapper tbCommunicationMapper;

    @Override
    public List<MessageVO> getMyMessage(Long userId) {
        return tbCommunicationMapper.getMyMessage(userId);
    }

    @Override
    public int insertMessage(TbCommunication tbCommunication) {
        return tbCommunicationMapper.insert(tbCommunication);
    }
}



