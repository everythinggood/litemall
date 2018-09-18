package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallGameRechargeMapper;
import org.linlinjava.litemall.db.domain.LitemallGameRecharge;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LitemallGameRechargeService {
    @Resource
    private LitemallGameRechargeMapper litemallGameRechargeMapper;

    public void add(LitemallGameRecharge litemallGameRecharge){
        litemallGameRechargeMapper.insert(litemallGameRecharge);
    }

    public void update(LitemallGameRecharge litemallGameRecharge){
        litemallGameRechargeMapper.updateByPrimaryKeySelective(litemallGameRecharge);
    }
}
