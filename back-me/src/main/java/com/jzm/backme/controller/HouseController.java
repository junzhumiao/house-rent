package com.jzm.backme.controller;

import com.jzm.backme.controller.base.BaseController;
import com.jzm.backme.model.to.house.HouseAddTo;
import com.jzm.backme.model.to.house.HouseDeleteTo;
import com.jzm.backme.model.to.house.HousePassTo;
import com.jzm.backme.model.to.house.HouseQueryTo;
import com.jzm.backme.domain.House;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.vo.HouseVo;
import com.jzm.backme.service.HouseService;
import com.jzm.backme.service.LoginService;
import com.jzm.backme.service.MainContractService;
import com.jzm.backme.util.NotEmptyUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.user.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 房屋表 -- 存储房屋信息 前端控制器
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/house")
public class HouseController extends BaseController
{
    @Resource
    private HouseService houseService;

    @Resource
    private MainContractService mainContractService;

    @Resource
    private LoginService loginService;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public AjaxResult create(@RequestBody HouseAddTo hdT){
        loginService.checkLandlord();
        hdT.setLandlordId(loginService.getUserId());
        String errPre = "添加房屋失败:";
        String errMes = NotEmptyUtil.checkEmptyFiled(hdT);
        if(StringUtil.isNotEmpty(errMes)){
            return error(errPre + errMes);
        }
        if(!UserUtil.verifyPhone(hdT.getLinkPhone())){
            return error(errPre + "联系人电话格式不对");
        }
        if(!UserUtil.verifyUsername(hdT.getLinkMan())){
            return error(errPre + "联系人名称格式不对");
        }
        boolean end = houseService.save(hdT);
        if(end){ // 上链
            mainContractService.addHouse(loginService.getAccountAddress(),hdT.getHouseId(),hdT.getPassword());
        }
        return toAjax(end);
    }


    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public AjaxResult getAll(@RequestBody HouseQueryTo houseQueryTo){
        int page = houseQueryTo.getPage();
        int pageSize = houseQueryTo.getPageSize();
        List<HouseVo> houseVos =  startOrderPage(page, pageSize, HouseVo.class,() ->
        {
            houseService.getAll(houseQueryTo);
        });
        return getAll(houseVos);
    }

    private AjaxResult getAll(List<HouseVo> houseVos)
    {
        for (HouseVo houseVo : houseVos)
        {
            Long houseId = houseVo.getHouseId();
            String password = mainContractService.getHouseAdmin(houseId);
            houseVo.setPassword(password);
        }
        return success(houseVos,houseVos.size());
    }

    @RequestMapping(value = "/updatePass",method = RequestMethod.POST)
    @ApiOperation("修改房屋密码")
    public  AjaxResult updatePass(@RequestBody HousePassTo housePassTo){
        String errPre = "修改房屋密码失败：";
        Long houseId = housePassTo.getHouseId();
       String password =  mainContractService.getHouseAdmin(houseId);
       if(StringUtil.equals(password,housePassTo.getOldPass()))
       {
          return error(errPre+"输入的旧密码与原先密码不一致!");
       }
        mainContractService.changeHousePass(houseId,password);
        return success();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public AjaxResult delete(@RequestBody HouseDeleteTo houseDeleteTo){
        List<Long> houseIds = houseDeleteTo.getHouseIds();
        boolean end = houseService.removeBatchByIds(houseIds);
        return toAjax(end);
    }

    @RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
    @ApiOperation("修改房屋状态")
    public  AjaxResult updateStatus(@RequestBody House house){
        boolean end = houseService.updateStatus(house);
        return toAjax(end);
    }

}
