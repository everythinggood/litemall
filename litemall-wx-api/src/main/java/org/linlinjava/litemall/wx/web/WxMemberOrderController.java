package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.db.service.LitemallMemberOrderService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(name = "/wx/member")
public class WxMemberOrderController {

    @Resource
    private LitemallMemberOrderService memberOrderService;
    @Resource
    private LitemallUserService userService;



}
