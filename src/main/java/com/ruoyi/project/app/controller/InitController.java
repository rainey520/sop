package com.ruoyi.project.app.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.LoginService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.app.domain.Index;
import com.ruoyi.project.app.domain.SoftVersion;
import com.ruoyi.project.app.service.IInitService;
import com.ruoyi.project.app.service.ILanguageService;
import com.ruoyi.project.app.service.ISoftVersionService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class InitController extends BaseController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private IInitService iInitService;

    @Autowired
    private ISoftVersionService softVersionService;

    @Autowired
    private ILanguageService languageService;

    @PostMapping("/login")
    public AjaxResult ajaxLogin(@RequestBody User user) {
        try {
            System.out.println(user);
            return loginService.login(user.getLoginName(),user.getPassword(),user.getLangVersion());
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        } catch (BaseException b) {
            return error(b.getMessage());
        }
    }

    /**
     * 获取当天工单、菜单权限、公司信息
     * @return
     */
    @PostMapping("/index")
    public AjaxResult initIndex(@RequestBody Index index){
        try {
            return AjaxResult.success(iInitService.initIndex(index));
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    /**
     * 获取菜单
     * @param index
     * @return
     */
    @PostMapping("/menu")
    public AjaxResult initMenu(@RequestBody Index index){
        try {
            return AjaxResult.success(iInitService.initMenu(index));
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    /**
     * 获取工单号的接口
     */
    @RequestMapping("/getWorkCode")
    public AjaxResult getWorkCode(){
        return AjaxResult.success(iInitService.getWorkCode());
    }

    /**
     * 获取app版本
     */
    @RequestMapping("/version")
    public AjaxResult getSoftVersion(@RequestBody SoftVersion softVersion){
        return AjaxResult.success(softVersionService.selectSoftVersion(softVersion));
    }

    /**
     * 查看app本地化
     * @return 结果
     */
    @RequestMapping("/language")
    public AjaxResult getSoftVersion(){
        return AjaxResult.success(languageService.selectInfo());
    }

    /**
     * 获取看板硬件编码信息
     */
    @RequestMapping("/getDevKbCode")
    public AjaxResult getDevKbCode(){
        try {
            return AjaxResult.success(iInitService.getDevKbCode());
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
