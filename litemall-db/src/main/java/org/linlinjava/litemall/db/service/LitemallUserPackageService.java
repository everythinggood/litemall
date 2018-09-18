package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallUserPackageMapper;
import org.linlinjava.litemall.db.domain.LitemallUserPackage;
import org.linlinjava.litemall.db.domain.LitemallUserPackageExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallUserPackageService {

    @Resource
    private LitemallUserPackageMapper litemallUserPackageMapper;

    public void add(LitemallUserPackage litemallUserPackage){
        litemallUserPackageMapper.insert(litemallUserPackage);
    }

    public void update(LitemallUserPackage litemallUserPackage){
        litemallUserPackageMapper.updateByPrimaryKeySelective(litemallUserPackage);
    }

    public int countBySelective(Integer userId,Integer giftId,String status){
        LitemallUserPackageExample litemallUserPackageExample = new LitemallUserPackageExample();
        litemallUserPackageExample.or().andGiftIdEqualTo(giftId).andStatusEqualTo(status).andDeletedEqualTo(false);

        return (int)litemallUserPackageMapper.countByExample(litemallUserPackageExample);
    }

    public List<LitemallUserPackage> findBySelective(Integer userId,Integer giftId,String status){
        LitemallUserPackageExample litemallUserPackageExample = new LitemallUserPackageExample();
        litemallUserPackageExample.or().andGiftIdEqualTo(giftId).andStatusEqualTo(status).andDeletedEqualTo(false);

        return litemallUserPackageMapper.selectByExample(litemallUserPackageExample);
    }

    public List<LitemallUserPackage> findBySelective(Integer userId,String status){
        LitemallUserPackageExample litemallUserPackageExample = new LitemallUserPackageExample();
        litemallUserPackageExample.or().andStatusEqualTo(status).andDeletedEqualTo(false);

        return litemallUserPackageMapper.selectByExample(litemallUserPackageExample);
    }

}
