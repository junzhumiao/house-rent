package com.jzm.backme.model.vo;

import com.jzm.backme.annotation.NotEmpty;
import com.jzm.backme.domain.Contract;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: jzm
 * @date: 2024-04-16 22:19
 **/

@Data
public class ContractVo extends Contract
{
    // 签署结束时间
    private LocalDateTime signEndTime;
    // 生效
    private String begin;
    private LocalDateTime beginStartTime;
    private LocalDateTime  beginEndTime;
    private String stop;

    @NotEmpty(extra = "规定每月租金")
    private Integer rent;

    @NotEmpty(extra = "规定每月保证金")
    private Integer earnest;

    // 签署是否缴纳
    String isTenEarnest; // 租客是否缴纳保证金
    String  isTenRent; // 租客是否缴纳租金
    String  isLanEarnest; // 房东是否缴纳保证金

    String tenant; // 租客地址

    String landlord; // 房东地址
}
