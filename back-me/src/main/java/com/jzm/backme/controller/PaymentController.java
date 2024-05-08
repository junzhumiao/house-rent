package com.jzm.backme.controller;

import com.jzm.backme.controller.base.BaseController;
import com.jzm.backme.domain.Contract;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.to.payment.PaymentAddTo;
import com.jzm.backme.model.to.payment.PaymentQueryTo;
import com.jzm.backme.model.vo.PaymentVo;
import com.jzm.backme.service.ContractService;
import com.jzm.backme.service.LoginService;
import com.jzm.backme.service.MainContractService;
import com.jzm.backme.service.PaymentService;
import com.jzm.backme.util.StringUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 缴纳记录表 -- 记录租金缴纳信息 前端控制器
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController
{

    @Resource
    private ContractService contractService;

    @Resource
    private PaymentService paymentService;

    @Resource
    private MainContractService mainContractService;

    @Resource
    private LoginService loginService;


    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public AjaxResult create(@RequestBody PaymentAddTo pt){
        loginService.checkTenant();
        String errPre = "添加缴纳记录错误:";
        String address = loginService.getAccountAddress();
        Contract contract = contractService.getById(pt.getContractId());
        if(StringUtil.isEmpty(contract)){
            return error(errPre + "合同编号不存在");
        }
        if(StringUtil.isNotEmpty(contract.getStopTime())){
            return error(errPre + "合同已经终止");
        }
        pt.setPayer(address);
        pt.setPayerId(loginService.getUserId());
        pt.setContractNum(contract.getContractNum());
        if(pt.getMoney() <= 10){
            return error(errPre +"缴费金额不能小于10元");
        }
        boolean end = paymentService.save(pt);
        if(end){
            mainContractService.payRent(address,pt);
        }
        return toAjax(end);
    }


    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public AjaxResult getAll(@RequestBody PaymentQueryTo qt){
        int page = qt.getPage();
        int pageSize = qt.getPageSize();
        List<PaymentVo> paymentVos = startOrderPage(page, pageSize, PaymentVo.class, () -> {
            paymentService.getAll(qt);
        });
        return getAll(paymentVos);
    }

    private AjaxResult getAll(List<PaymentVo> paymentVos)
    {
        for (PaymentVo paymentVo : paymentVos)
        {
            Long paymentId = paymentVo.getPaymentId();
            mainContractService.getPayRent(paymentId,paymentVo);
        }
        return success(paymentVos,paymentVos.size());
    }
}
