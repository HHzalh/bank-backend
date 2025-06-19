package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.model.dto.TradeQueryDTO;
import com.king.bankbackend.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 交易管理控制器
 */
@RestController
@RequestMapping("/Admin/trade")
@Slf4j
public class TradeController {

    @Autowired
    private TradeService tradeService;


    /**
     * 删除交易记录
     *
     * @param tradeid
     * @return
     */
    @DeleteMapping("/deleteTrade/{tradeid}")
    public BaseResponse deleteTrade(@PathVariable Long tradeid) {
        tradeService.deleteTrade(tradeid);
        return Result.success();
    }


    /**
     * 范围分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param begin
     * @param end
     * @return
     */
    @PostMapping("/pageTrade")
    public BaseResponse<PageResult> pageTrade(
            @RequestBody TradeQueryDTO tradeQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        PageResult pageResult = tradeService.pageTrade(tradeQueryDTO, begin, end);
        return Result.success(pageResult);
    }
} 