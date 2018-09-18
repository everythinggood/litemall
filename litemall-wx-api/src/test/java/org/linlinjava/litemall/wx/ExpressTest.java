package org.linlinjava.litemall.wx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.litemall.core.express.ExpressService;
import org.linlinjava.litemall.core.express.dao.ExpressInfo;
import org.linlinjava.litemall.db.domain.LitemallGameGift;
import org.linlinjava.litemall.db.domain.LitemallGameLog;
import org.linlinjava.litemall.db.domain.LitemallGameUser;
import org.linlinjava.litemall.db.service.LitemallGameGiftService;
import org.linlinjava.litemall.db.service.LitemallGameLogService;
import org.linlinjava.litemall.db.service.LitemallGameUserService;
import org.linlinjava.litemall.wx.web.WxGameController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ExpressTest {

    @Test
    public void test() {
        ExpressService expressService = new ExpressService();
        ExpressInfo ei = null;
        try {
            ei = expressService.getExpressInfo("YTO", "800669400640887922");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(ei);
    }

//    @Test
//    public void testAutoGenerateGameLog(){
//        LitemallGameGiftService gameGiftService = new LitemallGameGiftService();
//        List<LitemallGameGift> giftList = gameGiftService.findAll();
//        System.out.print(giftList);
//    }

    public void autoGenerateGameLog(){
        LitemallGameLogService gameLogService = new LitemallGameLogService();
        LitemallGameGiftService gameGiftService = new LitemallGameGiftService();
        LitemallGameUserService gameUserService = new LitemallGameUserService();
        List<LitemallGameGift> giftList = gameGiftService.findAll();
        List<LitemallGameUser> userList = gameUserService.findAll();

        userList.forEach(gameUser->{
            Integer userId = gameUser.getUserId();
            List<LitemallGameLog> gameLogList = gameLogService.findByUserId(userId);
            LitemallGameGift gameGift = gameGiftService.findOneByType("fruit");
            Integer fruitId = gameGift.getId();
            List<LitemallGameLog> newGameLogList = generateGameLogList(50,generateGameLog(userId,fruitId));

            gameLogList.addAll(newGameLogList);
            int size = gameLogList.size();

            for (int i=0;i<giftList.size();i++){
                LitemallGameGift gift = giftList.get(i);
                int before = gift.getBefore();
                int after = gift.getAfter();

                if(size < before) continue;
                gameLogList = checkGiftRuleOnGameLog(gameLogList,0,before,gift.getId(),fruitId);
                int limit = before+after;
                while (size > limit){
                    int start = limit-after;
                    gameLogList = checkGiftRuleOnGameLog(gameLogList,start,limit,gift.getId(),fruitId);
                    limit += after;
                }
            }

            gameLogList.forEach(gameLog->{
                if(gameLog.getId() == null){
                    gameLogService.add(gameLog);
                }
            });

        });

    }

    private List<LitemallGameLog> checkGiftRuleOnGameLog(List<LitemallGameLog> list,int start,int end,Integer giftId,Integer fruitId){
        List<Integer> index = new ArrayList<>();
        for (; start<end; start++){
            if(Objects.equals(list.get(start).getGiftId(), giftId)) return list;
            if(Objects.equals(list.get(start).getGiftId(), fruitId)){
                index.add(start);
            }
        }
        if(index.size() < 1){
            return list;
        }
        Random random = new Random();
        int listIndex = random.nextInt(index.size());
        LitemallGameLog gameLog = list.get(listIndex);
        gameLog.setGiftId(giftId);
        gameLog.setType("generate");
        list.set(listIndex,gameLog);

        return list;
    }

    private LitemallGameLog generateGameLog(Integer userId,Integer giftId){
        LitemallGameLog gameLog = new LitemallGameLog();
        gameLog.setStatus("pending");
        gameLog.setDeleted(false);
        gameLog.setAddTime(LocalDateTime.now());
        gameLog.setUserId(userId);
        gameLog.setType("game");
        gameLog.setGiftId(giftId);
        return gameLog;
    }

    private List<LitemallGameLog> generateGameLogList(int size,LitemallGameLog gameLog){
        if(gameLog == null){
            return null;
        }
        List<LitemallGameLog> gameLogList = new ArrayList<>();
        for (int i=0;i<size;i++){
            gameLogList.add(gameLog);
        }
        return gameLogList;
    }
}
