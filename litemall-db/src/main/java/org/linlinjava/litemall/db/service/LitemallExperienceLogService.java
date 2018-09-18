package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallExperienceLogMapper;
import org.linlinjava.litemall.db.domain.LitemallExperienceLog;
import org.linlinjava.litemall.db.domain.LitemallExperienceLogExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class LitemallExperienceLogService {

    @Resource
    private LitemallExperienceLogMapper litemallExperienceLogMapper;

    public void add(LitemallExperienceLog litemallExperienceLog){
        litemallExperienceLogMapper.insert(litemallExperienceLog);
    }

    public List<LitemallExperienceLog> findByUserId(Integer userId){
        LitemallExperienceLogExample litemallExperienceLogExample = new LitemallExperienceLogExample();
        litemallExperienceLogExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        litemallExperienceLogExample.setOrderByClause(LitemallExperienceLog.Column.addTime.desc());

        return litemallExperienceLogMapper.selectByExample(litemallExperienceLogExample);
    }
    public int countBySelective(Integer userId,String action){
        LitemallExperienceLogExample litemallExperienceLogExample = new LitemallExperienceLogExample();
        litemallExperienceLogExample.or().andUserIdEqualTo(userId).andActionEqualTo(action).andAddTimeBetween(LocalDate.now().atTime(0,0,0),LocalDate.now().atTime(23,59,59));

        return (int)litemallExperienceLogMapper.countByExample(litemallExperienceLogExample);
    }
}
