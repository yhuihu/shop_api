package com.study.shop.business.controller.admin;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.aop.annotation.Log;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbClassificationService;
import com.study.shop.provider.domain.TbClassification;
import com.study.shop.provider.dto.ClassificationDTO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-04-18
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-classification")
public class AdminClassificationController {

    @Reference(version = "1.0.0")
    private TbClassificationService tbClassificationService;

    @PostMapping()
    @Log("添加分类信息")
    public ResponseResult addClassification(@RequestBody ClassificationDTO classificationDTO) {
        long snowId = SnowIdUtils.uniqueLong();
        TbClassification tbClassification = new TbClassification();
        BeanUtils.copyProperties(classificationDTO, tbClassification);
        tbClassification.setId(snowId);
        tbClassification.setIsParent("1".equals(classificationDTO.getIsParent()));
        tbClassification.setSortOrder(Integer.valueOf(classificationDTO.getStatus()));
        if (classificationDTO.getParentId() != null && !"".equals(classificationDTO.getParentId())) {
            tbClassification.setParentId(Long.valueOf(classificationDTO.getParentId()));
        }
        int flag = tbClassificationService.addClassification(tbClassification);
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "添加分类信息", tbClassification);
        } else {
            throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
        }
    }

    @PutMapping()
    @Log("更新分类")
    public ResponseResult updateClassification(@RequestBody ClassificationDTO classificationDTO) {
        TbClassification tbClassification = new TbClassification();
        BeanUtils.copyProperties(classificationDTO, tbClassification);
        tbClassification.setSortOrder(Integer.valueOf(classificationDTO.getStatus()));
        tbClassification.setId(Long.valueOf(classificationDTO.getId()));
        if (classificationDTO.getParentId() != null && !"".equals(classificationDTO.getParentId())) {
            tbClassification.setParentId(Long.valueOf(classificationDTO.getParentId()));
        }
        tbClassification.setIsParent("1".equals(classificationDTO.getIsParent()));
        int flag = tbClassificationService.updateClassification(tbClassification);
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "更新成功", tbClassification);
        } else {
            throw new BusinessException(ExceptionStatus.UPDATE_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Log("删除分类")
    public ResponseResult deleteClassification(@PathVariable Long id) {
        tbClassificationService.deleteClassification(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "删除成功");
    }
}
