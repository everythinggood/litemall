package org.linlinjava.litemall.wx.convert;

import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.wx.convert.vo.OrderGoodsVo;

import java.util.ArrayList;
import java.util.List;

public class OrderGoodsVoListConvert {

    public List<OrderGoodsVo> convertToOrderGoodsVoList(List<LitemallOrderGoods> orderGoodsList){
        List<OrderGoodsVo> orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
        for(LitemallOrderGoods litemallOrderGoods : orderGoodsList){
            OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
            orderGoodsVo.setId(litemallOrderGoods.getId());
            orderGoodsVo.setGoodsId(litemallOrderGoods.getGoodsId());
            orderGoodsVo.setGoodsName(litemallOrderGoods.getGoodsName());
            orderGoodsVo.setGoodsSpecificationValues(litemallOrderGoods.getSpecifications());
            orderGoodsVo.setNumber(litemallOrderGoods.getNumber());
            orderGoodsVo.setOrderId(litemallOrderGoods.getOrderId());
            orderGoodsVo.setPicUrl(litemallOrderGoods.getPicUrl());
            orderGoodsVo.setRetailPrice(litemallOrderGoods.getPrice());
            orderGoodsVoList.add(orderGoodsVo);
        }

        return orderGoodsVoList;

    }
}
