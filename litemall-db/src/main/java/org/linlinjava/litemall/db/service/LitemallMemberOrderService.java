package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallMemberOrderMapper;
import org.linlinjava.litemall.db.domain.LitemallMemberOrder;
import org.linlinjava.litemall.db.domain.LitemallMemberOrderExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LitemallMemberOrderService {

    @Resource
    private LitemallMemberOrderMapper litemallMemberOrderMapper;

    public void add(LitemallMemberOrder litemallMemberOrder){
        litemallMemberOrderMapper.insert(litemallMemberOrder);
    }

    public void update(LitemallMemberOrder litemallMemberOrder){
        litemallMemberOrderMapper.updateByPrimaryKeySelective(litemallMemberOrder);
    }

    public int countBySelective(String memberOrderSn){
        LitemallMemberOrderExample litemallMemberOrderExample = new LitemallMemberOrderExample();
        litemallMemberOrderExample.or().andMemberOrderIdEqualTo(memberOrderSn).andDeletedEqualTo(false);

        return (int)litemallMemberOrderMapper.countByExample(litemallMemberOrderExample);
    }
}
