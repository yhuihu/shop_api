package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbAddressService;
import com.study.shop.provider.dto.AddressDTO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Tiger
 * @date 2020-01-29
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping(value = "address")
public class AddressController {
    @Reference(version = "1.0.0")
    private TbAddressService tbAddressService;

    @GetMapping()
    public ResponseResult getAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbAddressService.getAllAddress(authentication.getName()));
    }

    @PostMapping()
    public ResponseResult addAddress(@RequestBody @Valid AddressDTO addressDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (addressDTO.getAddressId() == null) {
            long snowId = SnowIdUtils.uniqueLong();
            addressDTO.setAddressId(snowId);
            int i = tbAddressService.addAddress(authentication.getName(), addressDTO);
            if (i > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbAddressService.getAllAddress(authentication.getName()));
            } else {
                return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "出现未知错误");
            }
        } else {
            int i = tbAddressService.updateAddress(authentication.getName(), addressDTO);
            if (i > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbAddressService.getAllAddress(authentication.getName()));
            } else {
                return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "出现未知错误");
            }
        }
    }

    @DeleteMapping()
    public ResponseResult deleteAddress(@RequestParam(value = "addressId",required = true) String addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int i = tbAddressService.deleteAddress(authentication.getName(), Long.valueOf(addressId));
        if (i>0){
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbAddressService.getAllAddress(authentication.getName()));
        }else{
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "出现未知错误");
        }
    }
}
