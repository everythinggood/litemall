package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallRechargeRuleMapper;
import org.linlinjava.litemall.db.domain.LitemallRechargeRule;
import org.linlinjava.litemall.db.domain.LitemallRechargeRuleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallRechargeRuleService {
    @Resource
    private LitemallRechargeRuleMapper litemallRechargeRuleMapper;

    public List<LitemallRechargeRule> findAll(){
        LitemallRechargeRuleExample litemallRechargeRuleExample = new LitemallRechargeRuleExample();
        litemallRechargeRuleExample.or().andDeletedEqualTo(false);
        litemallRechargeRuleExample.setOrderByClause(LitemallRechargeRule.Column.addTime.desc());

        return litemallRechargeRuleMapper.selectByExample(litemallRechargeRuleExample);
    }

    public LitemallRechargeRule findOneBySelective(String money){
        LitemallRechargeRuleExample litemallRechargeRuleExample = new LitemallRechargeRuleExample();
        litemallRechargeRuleExample.or().andMoneyEqualTo(money).andDeletedEqualTo(false);

        return litemallRechargeRuleMapper.selectOneByExample(litemallRechargeRuleExample);
    }
}
