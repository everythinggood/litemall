package org.linlinjava.litemall.core.qcode;

import org.linlinjava.litemall.core.system.SystemConfig;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class QCodeGoodShare extends QCodeBase {

    @Override
    protected String getKeyName(String id) {
        return "GOOD_QCODE_" + id + ".jpg";
    }

    /**
     * 创建商品分享图
     *
     * @param goodId
     * @param goodPicUrl
     * @param goodName
     */
    public void createGoodShareImage(String goodId, String goodPicUrl, String goodName) {
        if (!SystemConfig.isAutoCreateShareImage())
            return;

        BufferedImage qrCodeImage = getQCode("goods," + goodId, "pages/index/index");
        //将商品图片，商品名字,商城名字画到模版图中
        byte[] imageData = new byte[0];
        try {
            imageData = drawPicture(qrCodeImage, goodPicUrl, goodName, SystemConfig.getMallName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveImage(goodId, imageData);
    }

}
