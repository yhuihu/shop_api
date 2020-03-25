package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbCommunication;
import com.study.shop.provider.vo.MessageVO;

import java.util.List;

/**
 * @author admin
 */
public interface TbCommunicationService {
    /**
     * 获取我的消息
     *
     * @param userId id
     * @return {@link MessageVO}
     */
    List<MessageVO> getMyMessage(Long userId);

    /**
     * 插入新信息
     * @param tbCommunication {@link TbCommunication}
     * @return 0,1
     */
    int insertMessage(TbCommunication tbCommunication);
}



