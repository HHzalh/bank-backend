package com.king.bankbackend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.constant.LocalDateConstant;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.DepositMapper;
import com.king.bankbackend.model.dto.DepositDTO;
import com.king.bankbackend.model.dto.DepositQueryDTO;
import com.king.bankbackend.model.dto.DepositUpdateDTO;
import com.king.bankbackend.model.entity.Deposit;
import com.king.bankbackend.model.vo.DepositQueryVO;
import com.king.bankbackend.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 存款类型服务实现类
 */
@Service
@Slf4j
public class DepositServiceImpl implements DepositService {

    @Autowired
    private DepositMapper depositMapper;

    /**
     * 新增存款类型
     *
     * @param depositDTO
     */
    public void addDeposit(DepositDTO depositDTO) {
        log.info("新增存款类型，存款类型信息：{}", depositDTO);

        // 参数校验
        if (depositDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型信息不能为空");
        }

        if (!StringUtils.hasText(depositDTO.getSavingname())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型名称不能为空");
        }
        Deposit deposit = depositMapper.getByName(depositDTO.getSavingname());
        if (deposit != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型名称已存在");
        }

        // 封装数据
        deposit = new Deposit();
        BeanUtils.copyProperties(depositDTO, deposit);

        depositMapper.insert(deposit);
    }

    /**
     * 更新存款类型
     *
     * @param depositUpdateDTO
     */
    public void updateDeposit(DepositUpdateDTO depositUpdateDTO) {
        log.info("更新存款类型，存款类型信息：{}", depositUpdateDTO);

        // 参数校验
        if (depositUpdateDTO == null || depositUpdateDTO.getSavingid() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型ID不能为空");
        }

        // 检查是否存在
        Deposit existingDeposit = depositMapper.getById(depositUpdateDTO.getSavingid());
        if (existingDeposit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "存款类型不存在");
        }

        // 封装数据
        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(depositUpdateDTO, deposit);

        depositMapper.update(deposit);
    }

    /**
     * 删除存款类型
     *
     * @param savingid
     */
    @Override
    public void deleteDeposit(Long savingid) {

        // 参数校验
        if (savingid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型ID不能为空");
        }

        // 检查是否存在
        Deposit existingDeposit = depositMapper.getById(savingid);
        if (existingDeposit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "存款类型不存在");
        }
        // 检查是否有存款记录正在使用该存款类型
        int count = depositMapper.countDeposit(savingid);
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "该存款类型正在被" + count + "条存款记录使用，无法删除");
        }

        depositMapper.deleteById(savingid);
    }

    /**
     * 根据ID获取存款类型
     *
     * @param savingid
     * @return
     */
    public Deposit getDeposit(Long savingid) {
        log.info("获取存款类型信息，ID：{}", savingid);

        // 参数校验
        if (savingid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存款类型ID不能为空");
        }

        Deposit deposit = depositMapper.getById(savingid);
        if (deposit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "存款类型不存在");
        }

        return deposit;
    }

    /**
     * 分页查询存款类型
     *
     * @param depositQueryDTO
     * @return
     */
    public PageResult pageDeposit(DepositQueryDTO depositQueryDTO, LocalDate begin, LocalDate end) {
        log.info("分页查询存款类型，查询条件：{}", depositQueryDTO);

        // 设置默认分页参数
        if (depositQueryDTO.getPage() <= 0) {
            depositQueryDTO.setPage(1);
        }
        if (depositQueryDTO.getPageSize() <= 0) {
            depositQueryDTO.setPageSize(10);
        }

        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;

        if (begin != null) {
            beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        } else {
            beginTime = LocalDateTime.of(LocalDateConstant.DEFAULT_TIMESTAMP, LocalTime.MIN);
        }

        if (end != null) {
            endTime = LocalDateTime.of(end, LocalTime.MAX);
        } else {
            endTime = LocalDateTime.now();
        }

        log.info("处理后的查询条件：{}, 开始时间：{}, 结束时间：{}", depositQueryDTO, beginTime, endTime);
        // 使用PageHelper进行分页
        PageHelper.startPage(depositQueryDTO.getPage(), depositQueryDTO.getPageSize());

        Page<DepositQueryVO> page = depositMapper.pageQuery(depositQueryDTO, beginTime, endTime);
        long total = page.getTotal();
        List<DepositQueryVO> records = page.getResult();

        return new PageResult(total, records);
    }
} 