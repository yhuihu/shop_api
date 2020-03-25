package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbCommunicationService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbCommunication;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.CommunicationDetailDTO;
import com.study.shop.provider.dto.CommunicationUserListDTO;
import com.study.shop.provider.vo.MessageVO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tiger
 * @date 2020-03-23
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("/communication")
public class CommunicationController {
    @Reference(version = "1.0.0")
    TbUserService tbUserService;
    @Reference(version = "1.0.0")
    TbCommunicationService tbCommunicationService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 初始化个人数据内容
     *
     * @return map集合
     */
    @GetMapping()
    public ResponseResult getMyMessage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        List<MessageVO> myMessage = tbCommunicationService.getMyMessage(tbUser.getId());
        Set<CommunicationUserListDTO> userList = new HashSet<>();
        //设置用户列表
        myMessage.forEach(item -> {
            CommunicationUserListDTO communicationUserListDTO = new CommunicationUserListDTO();
            communicationUserListDTO.setCreateTime(item.getCreateTime());
//            如果接收方用户编号等于该用户的编号，则对应的发送方为fromUser
            if (item.getFromId() != null) {
                if (Long.valueOf(item.getToId()).equals(tbUser.getId())) {
                    communicationUserListDTO.setUserId(item.getFromId());
                    communicationUserListDTO.setIcon(item.getFromIcon());
                    communicationUserListDTO.setNickName(item.getFromNickName());
                    //因为自己是接收方，需要判断是否已读
                    communicationUserListDTO.setIsRead(item.getIsRead());
                } else {
                    communicationUserListDTO.setUserId(item.getToId());
                    communicationUserListDTO.setIcon(item.getToIcon());
                    communicationUserListDTO.setNickName(item.getToNickName());
                    //自己发送出去的内容，默认为已读
                    communicationUserListDTO.setIsRead(1);
                }
                userList.add(communicationUserListDTO);
            }
        });
        Map<String, Object> listMap = new HashMap<>();
        userList.forEach(item -> {
            listMap.put(item.getUserId(), getUserMessage(item.getUserId(), myMessage));
        });
        //系统消息没有fromId
        listMap.put("SECOND_HAND_MESSAGE", getUserMessage(null, myMessage));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userList", userList);
        resultMap.put("chatMap", listMap);
        return new ResponseResult(ResponseResult.CodeStatus.OK, resultMap);
    }

    /**
     * 处理数据库中消息内容
     *
     * @param id        用户编号，用与判断消息是自己发送的还是别人发给自己的
     * @param myMessage 消息列表
     * @return {@link CommunicationDetailDTO}
     */
    private List<CommunicationDetailDTO> getUserMessage(String id, List<MessageVO> myMessage) {
        List<CommunicationDetailDTO> list = new ArrayList<>();
        myMessage.forEach(messageVO -> {
            if (id == null) {
                if (messageVO.getFromId() == null) {
                    CommunicationDetailDTO communicationDetailDTO = new CommunicationDetailDTO();
                    communicationDetailDTO.setContent(messageVO.getContent());
                    communicationDetailDTO.setContentType(0);
                    communicationDetailDTO.setCreateTime(messageVO.getCreateTime());
                    communicationDetailDTO.setIsMine(0);
                    list.add(communicationDetailDTO);
                }
            } else if (id.equals(messageVO.getFromId())) {
                CommunicationDetailDTO communicationDetailDTO = new CommunicationDetailDTO();
                communicationDetailDTO.setContent(messageVO.getContent());
                communicationDetailDTO.setContentType(messageVO.getContentType());
                communicationDetailDTO.setCreateTime(messageVO.getCreateTime());
                communicationDetailDTO.setIcon(messageVO.getFromIcon());
                communicationDetailDTO.setIsMine(0);
                list.add(communicationDetailDTO);
            }
            //收信息的人为目标，则发件人为自己
            else if (messageVO.getFromId() != null && id.equals(messageVO.getToId())) {
                CommunicationDetailDTO communicationDetailDTO = new CommunicationDetailDTO();
                communicationDetailDTO.setContent(messageVO.getContent());
                communicationDetailDTO.setContentType(messageVO.getContentType());
                communicationDetailDTO.setCreateTime(messageVO.getCreateTime());
                communicationDetailDTO.setIcon(messageVO.getFromIcon());
                communicationDetailDTO.setIsMine(1);
                list.add(communicationDetailDTO);
            }
        });
        Collections.sort(list, Comparator.comparing(CommunicationDetailDTO::getCreateTime));
        return list;
    }

    @PostMapping()
    public ResponseResult pushMessage(@RequestBody MessageVO messageVO) {
        Date date=new Date();
        TbCommunication tbCommunication = new TbCommunication();
        tbCommunication.setId(SnowIdUtils.uniqueLong());
        tbCommunication.setToId(Long.valueOf(messageVO.getToId()));
        tbCommunication.setFromId(Long.valueOf(messageVO.getFromId()));
        tbCommunication.setContent(messageVO.getContent());
        tbCommunication.setCreateTime(date);
        tbCommunication.setIsRead(0);
        tbCommunication.setContentType(messageVO.getContentType());
        tbCommunicationService.insertMessage(tbCommunication);
        redisTemplate.convertAndSend("CHAT_CHANNEL", messageVO);
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }
}
