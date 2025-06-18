package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.model.dto.DepositDTO;
import com.king.bankbackend.model.dto.DepositQueryDTO;
import com.king.bankbackend.model.dto.DepositUpdateDTO;
import com.king.bankbackend.model.entity.Deposit;
import com.king.bankbackend.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 存款类型管理控制器
 */
@RestController
@RequestMapping("/Admin/deposit")
@Slf4j
public class DepositController {

    @Autowired
    private DepositService depositService;

    /**
     * 新增存款类型
     *
     * @param depositDTO
     * @return
     */
    @PostMapping("/addDeposit")
    public BaseResponse addDeposit(@RequestBody DepositDTO depositDTO) {
        ThrowUtils.throwIf(depositDTO == null, ErrorCode.PARAMS_ERROR);
        depositService.addDeposit(depositDTO);
        return Result.success();
    }

    /**
     * 更新存款类型
     *
     * @param depositUpdateDTO
     * @return
     */
    @PutMapping("/updateDeposit")
    public BaseResponse updateDeposit(@RequestBody DepositUpdateDTO depositUpdateDTO) {
        depositService.updateDeposit(depositUpdateDTO);
        return Result.success();
    }

    /**
     * 删除存款类型
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteDeposit/{id}")
    public BaseResponse deleteDeposit(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return Result.success();
    }

    /**
     * 获取存款类型信息
     *
     * @param savingid
     * @return
     */
    @GetMapping("/getDeposit/{savingid}")
    public BaseResponse<Deposit> getDeposit(@PathVariable Long savingid) {
        Deposit deposit = depositService.getDeposit(savingid);
        return Result.success(deposit);
    }

    /**
     * 范围分页查询存款类型
     *
     * @param depositQueryDTO
     * @param begin
     * @param end
     * @return
     */
    @PostMapping("/pageDeposit")
    public BaseResponse<PageResult> pageDeposit(
            @RequestBody DepositQueryDTO depositQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        PageResult pageResult = depositService.pageDeposit(depositQueryDTO, begin, end);
        return Result.success(pageResult);
    }


}
