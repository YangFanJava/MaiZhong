package com.maizhong.portal.controller;

import com.maizhong.common.result.JsonResult;
import com.maizhong.portal.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Xushd on 2017/3/21.
 */
@RestController
public class ActionController {

    @Autowired
    private ActionService actionService;
    /**
     * 电话咨询
     * @param phone
     * @return
     */
    @RequestMapping(value = "/doPhoneLink",method = RequestMethod.POST)
    public JsonResult doPhoneLink(String phone){
        actionService.doPhoneLink(phone);
        return JsonResult.OK();
    }
}
