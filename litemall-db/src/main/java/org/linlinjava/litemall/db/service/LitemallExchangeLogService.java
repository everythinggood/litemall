package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallExchangeLogMapper;
import org.linlinjava.litemall.db.domain.LitemallExchangeLog;
import org.linlinjava.litemall.db.domain.LitemallExchangeLogExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallExchangeLogService {

    @Resource
    private LitemallExchangeLogMapper litemallExchangeLogMapper;

    public int add(LitemallExchangeLog litemallExchangeLog){
        return litemallExchangeLogMapper.insert(litemallExchangeLog);
    }
    public int update(LitemallExchangeLog litemallExchangeLog){
        return litemallExchangeLogMapper.updateByPrimaryKeySelective(litemallExchangeLog);
    }

    public List<LitemallExchangeLog> findBySelective(Integer userId){
        LitemallExchangeLogExample litemallExchangeLogExample = new LitemallExchangeLogExample();
        litemallExchangeLogExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        return litemallExchangeLogMapper.selectByExample(litemallExchangeLogExample);
    }

    public List<LitemallExchangeLog> findBySelective(Integer userId,String status){
        LitemallExchangeLogExample litemallExchangeLogExample = new LitemallExchangeLogExample();
        litemallExchangeLogExample.or().andUserIdEqualTo(userId).andStatusEqualTo(status).andDeletedEqualTo(false);

        return litemallExchangeLogMapper.selectByExample(litemallExchangeLogExample);
    }



}
