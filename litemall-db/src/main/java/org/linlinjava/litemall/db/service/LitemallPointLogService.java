package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallPointLogMapper;
import org.linlinjava.litemall.db.domain.LitemallPointLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LitemallPointLogService {
    @Resource
    private LitemallPointLogMapper litemallPointLogMapper;

    public void add(LitemallPointLog litemallPointLog){
        litemallPointLogMapper.insert(litemallPointLog);
    }

    public void update(LitemallPointLog litemallPointLog){
        litemallPointLogMapper.updateByPrimaryKeySelective(litemallPointLog);
    }
}
