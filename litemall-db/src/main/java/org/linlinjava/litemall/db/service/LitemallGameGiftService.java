package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallGameGiftMapper;
import org.linlinjava.litemall.db.dao.LitemallGameUserMapper;
import org.linlinjava.litemall.db.domain.LitemallGameGift;
import org.linlinjava.litemall.db.domain.LitemallGameGiftExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallGameGiftService {
    @Resource
    private LitemallGameGiftMapper litemallGameGiftMapper;

    public List<LitemallGameGift> findAll(){
        LitemallGameGiftExample litemallGameGiftExample = new LitemallGameGiftExample();
        litemallGameGiftExample.or().andDeletedEqualTo(false);
        return litemallGameGiftMapper.selectByExample(litemallGameGiftExample);
    }

    public LitemallGameGift findOneById(Integer id){
        return litemallGameGiftMapper.selectByPrimaryKey(id);
    }

    public LitemallGameGift findOneByType(String type){
        LitemallGameGiftExample litemallGameGiftExample = new LitemallGameGiftExample();
        litemallGameGiftExample.or().andTypeEqualTo(type).andDeletedEqualTo(false);

        return litemallGameGiftMapper.selectOneByExample(litemallGameGiftExample);
    }


}
