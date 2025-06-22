package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerQueryDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CustomerLoginVO;
import com.king.bankbackend.service.CustomerService;
import com.king.bankbackend.utils.TencentCosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/Admin/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TencentCosUtils tencentCosUtils;

    /**
     * 管理员登录
     *
     * @param customerLoginDTO
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<CustomerLoginVO> login(@RequestBody CustomerLoginDTO customerLoginDTO) {
        ThrowUtils.throwIf(customerLoginDTO == null, ErrorCode.PARAMS_ERROR);
        //登录并获取带有token的登录结果
        CustomerLoginVO customerLoginVO = customerService.loginWithToken(customerLoginDTO);
        return Result.success(customerLoginVO);
    }

    /**
     * 获得管理员的信息
     *
     * @return
     */
    @GetMapping("/getAdminInfo")
    public BaseResponse<User> getAdminInfo() {
        User user = customerService.getAdminInfo();
        return Result.success(user);
    }

    /**
     * 新增用户
     *
     * @param customerDTO
     * @return
     */
    @PostMapping("/addCustomer")
    public BaseResponse addCustomer(@RequestBody CustomerDTO customerDTO) {
        ThrowUtils.throwIf(customerDTO == null, ErrorCode.PARAMS_ERROR);
        customerService.addCustomer(customerDTO);
        return Result.success();
    }

    /**
     * 更新用户信息
     *
     * @param customerUpdateDTO
     * @return
     */
    @PutMapping("/updateCustomer")
    public BaseResponse updateCustomer(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        customerService.updateCustomer(customerUpdateDTO);
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCustomer/{id}")
    public BaseResponse deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success();
    }

    /**
     * 范围分页查询
     *
     * @param customerQueryDTO
     * @return
     */
    @PostMapping("/pageCustomer")
    public BaseResponse<PageResult> pageCustomer(
            @RequestBody CustomerQueryDTO customerQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        PageResult pageResult = customerService.pageCustomer(customerQueryDTO, begin, end);
        return Result.success(pageResult);
    }

    /**
     * 上传图片到腾讯云对象存储
     *
     * @param file 上传的图片文件
     */
    @PostMapping("/uploadImage")
    public BaseResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        ThrowUtils.throwIf(file == null || file.isEmpty(), ErrorCode.PARAMS_ERROR, "上传文件不能为空");

        // 检查文件类型是否为图片
        String contentType = file.getContentType();
        ThrowUtils.throwIf(contentType == null || !contentType.startsWith("image/"),
                ErrorCode.PARAMS_ERROR, "只能上传图片文件");

        try {
            // 调用腾讯云工具类上传图片
            String imageUrl = tencentCosUtils.uploadFile(file);
            log.info("图片上传成功，URL: {}", imageUrl);
            customerService.updateAvatar(imageUrl);
            return Result.success(imageUrl);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null, "图片上传失败");
        }
    }
}
