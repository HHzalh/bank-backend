package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.model.dto.CardDTO;
import com.king.bankbackend.model.dto.CardQueryDTO;
import com.king.bankbackend.model.dto.CardUpdatePwdDTO;
import com.king.bankbackend.model.dto.CardUpdateStatusDTO;
import com.king.bankbackend.model.vo.CardListVo;
import com.king.bankbackend.model.vo.CardQueryVO;
import com.king.bankbackend.model.vo.CardStatusVo;
import com.king.bankbackend.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 银行卡管理控制器
 */
@RestController
@RequestMapping("/Admin/card")
@Slf4j
public class CardController {

    @Autowired
    private CardService cardService;

    /**
     * 新增银行卡
     *
     * @param cardDTO
     * @return
     */
    @PostMapping("/addCard")
    public BaseResponse addCard(@RequestBody CardDTO cardDTO) {
        ThrowUtils.throwIf(cardDTO == null, ErrorCode.PARAMS_ERROR);
        cardService.addCard(cardDTO);
        return Result.success();
    }

    /**
     * 更新银行卡密码
     *
     * @param cardUpdatePwdDTO
     * @return
     */
    @PutMapping("/changePwd")
    public BaseResponse changePwd(@RequestBody CardUpdatePwdDTO cardUpdatePwdDTO) {
        cardService.changePwd(cardUpdatePwdDTO);
        return Result.success();
    }

    /**
     * 获取银行卡状态(是否挂失)
     *
     * @param cardid
     * @return
     */
    @GetMapping("/getStatus/{cardid}")
    public BaseResponse<CardStatusVo> getStatus(@PathVariable String cardid) {
        CardStatusVo cardStatusVo = cardService.getStatus(cardid);
        return Result.success(cardStatusVo);
    }

    /**
     * 更新银行卡状态(是否挂失)
     *
     * @param cardUpdateStatusDTO
     * @return
     */
    @PutMapping("/changeStatus")
    public BaseResponse changeStatus(@RequestBody CardUpdateStatusDTO cardUpdateStatusDTO) {
        cardService.changeStatus(cardUpdateStatusDTO);
        return Result.success();
    }

    /**
     * 删除银行卡
     *
     * @param cardid
     * @return
     */
    @DeleteMapping("/deleteCard/{cardid}")
    public BaseResponse deleteCard(@PathVariable String cardid) {
        cardService.deleteCard(cardid);
        return Result.success();
    }

    /**
     * 根据银行卡号获取银行卡信息
     *
     * @param cardid
     * @return
     */
    @GetMapping("/getCard/{cardid}")
    public BaseResponse<CardQueryVO> getCard(@PathVariable String cardid) {
        CardQueryVO CardQueryVO = cardService.getCard(cardid);
        return Result.success(CardQueryVO);
    }

    /**
     * 根据客户身份证获取银行卡列表
     *
     * @param pid
     * @return
     */
    @GetMapping("/getCardsByPid/{pid}")
    public BaseResponse<List<CardListVo>> getCardsByPid(@PathVariable String pid) {
        List<CardListVo> cards = cardService.getCardsByPid(pid);
        return Result.success(cards);
    }

    /**
     * 范围分页查询银行卡
     *
     * @param cardQueryDTO
     * @param begin
     * @param end
     * @return
     */
    @PostMapping("/pageCard")
    public BaseResponse<PageResult> pageCard(
            @RequestBody CardQueryDTO cardQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        PageResult pageResult = cardService.pageCard(cardQueryDTO, begin, end);
        return Result.success(pageResult);
    }
} 