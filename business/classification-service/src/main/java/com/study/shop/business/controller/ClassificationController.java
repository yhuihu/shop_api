package com.study.shop.business.controller;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbClassificationService;
import com.study.shop.provider.domain.TbClassification;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2019-11-20
 * @see com.study.shop.business.controller
 **/
@RestController
public class ClassificationController {

    @Reference(version = "1.0.0")
    private TbClassificationService tbClassificationService;

    @GetMapping()
    public ResponseResult getClassification(@RequestParam(name = "id", defaultValue = "0", required = false) Long classificationId,
                                            @RequestParam(name = "parentId", defaultValue = "0", required = false) int parentId) {
        System.out.println("id:" + classificationId);
        System.out.println("parentId:" + parentId);
        if (classificationId == 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取分类信息", tbClassificationService.getClassificationList((long) parentId));
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "分类具体信息", tbClassificationService.getClassificationById(classificationId));
        }
    }

    @PostMapping("cf")
    public ResponseResult addClassification(TbClassification tbClassification) {
        long snowId = SnowIdUtils.uniqueLong();
        tbClassification.setId(snowId);
        int flag = tbClassificationService.addClassification(tbClassification);
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "添加分类信息", tbClassification);
        } else {
            throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
        }
    }

    @PutMapping("cf")
    public ResponseResult updateClassification(TbClassification tbClassification) {
        int flag = tbClassificationService.updateClassification(tbClassification);
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "更新成功", tbClassification);
        } else {
            throw new BusinessException(ExceptionStatus.UPDATE_ERROR);
        }
    }

    @DeleteMapping("cf/{id}")
    public ResponseResult deleteClassification(@PathVariable Long id) {
        tbClassificationService.deleteClassification(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "删除成功");
    }
}
