package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallGameLogMapper;
import org.linlinjava.litemall.db.domain.LitemallGameLog;
import org.linlinjava.litemall.db.domain.LitemallGameLogExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Service
public class LitemallGameLogService {
    @Resource
    private LitemallGameLogMapper litemallGameLogMapper;

    public LitemallGameLog findOneByUser(Integer userId){
        LitemallGameLogExample litemallGameLogExample = new LitemallGameLogExample();
        litemallGameLogExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        litemallGameLogExample.setOrderByClause(LitemallGameLog.Column.addTime.desc());
        return litemallGameLogMapper.selectOneByExample(litemallGameLogExample);
    }
    public void deleteById(Integer id){
        litemallGameLogMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LitemallGameLog litemallGameLog){
        litemallGameLogMapper.insertSelective(litemallGameLog);
    }

    public void update(LitemallGameLog litemallGameLog){
        litemallGameLogMapper.updateByPrimaryKeySelective(litemallGameLog);
    }

    public Integer countBySelective(Integer userId,String type,String status){
        LitemallGameLogExample litemallGameLogExample = new LitemallGameLogExample();
        litemallGameLogExample.or().andUserIdEqualTo(userId).andTypeEqualTo(type).andStatusEqualTo(status).andDeletedEqualTo(false);
        litemallGameLogExample.setOrderByClause(LitemallGameLog.Column.addTime.desc());

        return (int)litemallGameLogMapper.countByExample(litemallGameLogExample);
    }

    public Integer countBySelective(Integer userId,String status,boolean today){
        LitemallGameLogExample litemallGameLogExample = new LitemallGameLogExample();
        LitemallGameLogExample.Criteria criteria = litemallGameLogExample.or();
        criteria = criteria.andUserIdEqualTo(userId).andStatusEqualTo(status);
        if(today){
            criteria = criteria.andUpdateTimeBetween(LocalDate.now().atTime(0,0,0),LocalDate.now().atTime(23,59,59));
        }
        criteria = criteria.andDeletedEqualTo(false);
        return (int)litemallGameLogMapper.countByExample(litemallGameLogExample);
    }

    public LitemallGameLog findOneBySelective(Integer userId,String type,String status){
        LitemallGameLogExample litemallGameLogExample = new LitemallGameLogExample();
        litemallGameLogExample.or().andUserIdEqualTo(userId).andTypeEqualTo(type).andStatusEqualTo(status).andDeletedEqualTo(false);
        litemallGameLogExample.setOrderByClause(LitemallGameLog.Column.addTime.asc());

        return litemallGameLogMapper.selectOneByExample(litemallGameLogExample);
    }

    public List<LitemallGameLog> findByUserId(Integer userId){
        LitemallGameLogExample litemallGameLogExample = new LitemallGameLogExample();
        litemallGameLogExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        litemallGameLogExample.setOrderByClause(LitemallGameLog.Column.addTime.asc());

        return litemallGameLogMapper.selectByExample(litemallGameLogExample);
    }


}
