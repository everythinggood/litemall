package org.linlinjava.litemall.core.qcode;

import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.db.domain.LitemallGroupon;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class QCodeGroupon extends QCodeBase {
    @Override
    protected String getKeyName(String id) {
        return null;
    }

    public void createGrouponShareImage(String goodName, String goodPicUrl, LitemallGroupon groupon) {
        try {
            BufferedImage qrCodeImage = getQCode("groupon," + groupon.getId(), "pages/index/index");
            //将商品图片，商品名字,商城名字画到模版图中
            byte[] imageData = drawPicture(qrCodeImage, goodPicUrl, goodName, SystemConfig.getMallName());
            saveImage(groupon.getId().toString(), imageData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
