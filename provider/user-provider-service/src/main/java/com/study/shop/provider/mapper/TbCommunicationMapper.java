package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbCommunication;
import com.study.shop.provider.vo.MessageVO;import tk.mybatis.mapper.common.Mapper;import java.util.List;

/**
 * @author admin
 */
public interface TbCommunicationMapper extends Mapper<TbCommunication> {
    /**
     * 获取我的消息
     *
     * @param userId userId
     * @return {@link MessageVO}
     */
    List<MessageVO> getMyMessage(Long userId);
}
