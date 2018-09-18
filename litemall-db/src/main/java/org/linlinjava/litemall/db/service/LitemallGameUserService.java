package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallGameUserMapper;
import org.linlinjava.litemall.db.domain.LitemallGameUser;
import org.linlinjava.litemall.db.domain.LitemallGameUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallGameUserService {

    @Resource
    private LitemallGameUserMapper litemallGameUserMapper;

    public int add(LitemallGameUser gameUser){ return litemallGameUserMapper.insert(gameUser);}

    public LitemallGameUser findByUserId(Integer userId){
        LitemallGameUserExample litemallGameUserExample = new LitemallGameUserExample();
        litemallGameUserExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        litemallGameUserExample.setOrderByClause(LitemallGameUser.Column.addTime.desc());

        return litemallGameUserMapper.selectOneByExample(litemallGameUserExample);
    }

    public void update(LitemallGameUser litemallGameUser){
        litemallGameUserMapper.updateByPrimaryKeySelective(litemallGameUser);
    }

    public List<LitemallGameUser> findAll(){
        LitemallGameUserExample litemallGameUserExample = new LitemallGameUserExample();
        litemallGameUserExample.or().andDeletedEqualTo(false);

        return litemallGameUserMapper.selectByExample(litemallGameUserExample);
    }
}
