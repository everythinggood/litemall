package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallMemberLevelMapper;
import org.linlinjava.litemall.db.domain.LitemallMemberLevel;
import org.linlinjava.litemall.db.domain.LitemallMemberLevelExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallMemberLevelService {
    @Resource
    private LitemallMemberLevelMapper litemallMemberLevelMapper;

    public List<LitemallMemberLevel> findAll(){
        LitemallMemberLevelExample litemallMemberLevelExample = new LitemallMemberLevelExample();
        litemallMemberLevelExample.or().andDeletedEqualTo(false);
        litemallMemberLevelExample.setOrderByClause(LitemallMemberLevel.Column.addTime.desc());

        return litemallMemberLevelMapper.selectByExampleSelective(litemallMemberLevelExample);
    }

    public LitemallMemberLevel findOneBySelective(int memberMoney){
        LitemallMemberLevelExample litemallMemberLevelExample = new LitemallMemberLevelExample();
        litemallMemberLevelExample.or().andMoneyEqualTo(memberMoney).andDeletedEqualTo(false);

        return litemallMemberLevelMapper.selectOneByExample(litemallMemberLevelExample);
    }
    public LitemallMemberLevel findOneBySelective(Integer id){
        return litemallMemberLevelMapper.selectByPrimaryKey(id);
    }
}
