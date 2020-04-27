package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbClassificationService;
import com.study.shop.provider.domain.TbClassification;
import com.study.shop.provider.vo.TbClassificationVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2019-11-20
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("classification")
public class ClassificationController {

    @Reference(version = "1.0.0")
    private TbClassificationService tbClassificationService;

    @GetMapping()
    public ResponseResult getClassification(@RequestParam(name = "id", defaultValue = "0", required = false) Long classificationId,
                                            @RequestParam(name = "parentId", defaultValue = "0", required = false) Long parentId) {
        if (classificationId == 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取分类信息", tbClassificationService.getAllClassification());
        } else {
            TbClassification classificationById = tbClassificationService.getClassificationById(classificationId);
            TbClassificationVO tbClassificationVO=new TbClassificationVO();
            BeanUtils.copyProperties(classificationById,tbClassificationVO);
            tbClassificationVO.setId(String.valueOf(classificationById.getId()));
            tbClassificationVO.setParentId(String.valueOf(classificationById.getParentId()));
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "分类具体信息", tbClassificationVO);
        }
    }
}
