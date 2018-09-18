package org.linlinjava.litemall.wx.web;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.HomeCacheManager;
import org.linlinjava.litemall.wx.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/wx/game")
@Validated
public class WxGameController {
    private final Log logger = LogFactory.getLog(WxGameController.class);

    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private LitemallGameUserService gameUserService;
    @Autowired
    private LitemallGameLogService gameLogService;
    @Autowired
    private LitemallGameGiftService gameGiftService;
    @Autowired
    private LitemallMemberLevelService memberLevelService;
    @Autowired
    private LitemallRechargeRuleService rechargeRuleService;
    @Autowired
    private LitemallExperienceLogService experienceLogService;
    @Autowired
    private LitemallPointLogService pointLogService;
    @Autowired
    private LitemallUserPackageService userPackageService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallExchangeLogService exchangeLogService;
    @Autowired
    private LitemallMemberOrderService memberOrderService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private LitemallUserFormIdService formIdService;
    @Autowired
    private LitemallGameRechargeService gameRechargeService;
    @Autowired
    private StatService statService;


    @GetMapping("gifts")
    public Object gifts(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallGameUser gameUser = gameUserService.findByUserId(userId);
        if (gameUser == null) {
            gameUser = generateGameUser(userId);
            gameUserService.add(gameUser);
        }

        if (HomeCacheManager.hasData(HomeCacheManager.GIFT_LIST)) {
            return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.GIFT_LIST));
        }
        List<LitemallGameGift> giftList = gameGiftService.findAll();
        HashMap<String, Object> data = new HashMap<>();
        data.put("giftList", giftList);
        HomeCacheManager.loadData(HomeCacheManager.GIFT_LIST, data);
        return ResponseUtil.ok(data);
    }

    private LitemallGameUser generateGameUser(Integer userId) {
        LitemallGameUser gameUser = new LitemallGameUser();
        gameUser.setXianyuan(0);
        gameUser.setPoint(0);
        gameUser.setAddTime(LocalDateTime.now());
        gameUser.setDeleted(false);
        gameUser.setExperience(0);
        gameUser.setUserId(userId);

        return gameUser;
    }

    @GetMapping("userInfo")
    public Object gameUserInfo(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = userService.findById(userId);
        LitemallGameUser gameUser = gameUserService.findByUserId(userId);
        int level = user.getUserLevel();
        String memberName = "";
        if (level > 0) {
            LitemallMemberLevel memberLevel = memberLevelService.findOneBySelective(level);
            memberName = memberLevel.getName();
        } else {
            memberName = "游客";
        }
        if (gameUser == null) {
            gameUser = generateGameUser(userId);
            gameUserService.add(gameUser);
        }
        LitemallGameGift gameGift = gameGiftService.findOneByType("fruit");
        int fruit = userPackageService.countBySelective(userId, gameGift.getId(), "pending");
        HashMap<String, Object> data = new HashMap<>();
        data.put("experience", gameUser.getExperience());
        data.put("xianyuan", gameUser.getXianyuan());
        data.put("point", gameUser.getPoint());
        data.put("name", user.getNickname());
        data.put("avatarUrl", user.getAvatar());
        data.put("level", level);
        data.put("memberName", memberName);
        data.put("fruit", fruit);
        return ResponseUtil.ok(data);
    }

    @GetMapping("start")
    public Object start(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallGameUser gameUser = gameUserService.findByUserId(userId);
        if (gameUser == null) {
            gameUser = generateGameUser(userId);
            gameUserService.add(gameUser);
        }

        LitemallGameGift gift = null;
        //免费机会
        LitemallGameLog gameLog = gameLogService.findOneBySelective(userId, "give", "pending");
        if (gameLog != null) {
            gameLog.setStatus("success");
            gameLogService.update(gameLog);
            //加入背包
            addUserPackage(userId, gameLog.getId(), gameLog.getGiftId());

            gift = gameGiftService.findOneById(gameLog.getGiftId());
            HashMap<String, Object> data = new HashMap<>();
            data.put("gift", gift);
            return ResponseUtil.ok(data);
        }

        if (!gameValidate(userId)) {
            return ResponseUtil.fail(500, "今天额度用完了");
        }

        //非免费机会
        if (gameUser.getXianyuan() < 150) {
            return ResponseUtil.fail(500, "仙缘不够");
        }

        //开启事务管理
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);

        try {
            gameLog = gameLogService.findOneBySelective(userId, "generate", "pending");
            if (gameLog == null) {
                LitemallGameGift gameGift = gameGiftService.findOneByType("fruit");
                gameLog = generateGameLog(userId, gameGift.getId());
                gameLogService.add(gameLog);
            }
            gameLog.setStatus("success");
            gameLog.setUpdateTime(LocalDateTime.now());
            gameLogService.update(gameLog);

            gameUser.setXianyuan(gameUser.getXianyuan() - 150);
            gameUser.setPoint(gameUser.getPoint() + 300);
            gameUserService.update(gameUser);
            //加入背包
            addUserPackage(userId, gameLog.getId(), gameLog.getGiftId());
            //参与游戏一次增加1EXP（每天一次）
            addExperienceByPlayGame(userId);
            //参与游戏一次增加300仙缘积分
            pointLogService.add(generatePointLog(userId, "playGame", true, 300));

            gift = gameGiftService.findOneById(gameLog.getGiftId());
        }catch (Exception e){
            txManager.rollback(status);
            logger.error(e);
            e.printStackTrace();
        }
        txManager.commit(status);

        HashMap<String, Object> data = new HashMap<>();
        data.put("gift", gift);
        return ResponseUtil.ok(data);
    }

    @GetMapping("recharge/rules")
    public Object rechargeRules() {
        if (HomeCacheManager.hasData(HomeCacheManager.RECHARGE_RULE)) {
            return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.RECHARGE_RULE));
        }
        List<LitemallRechargeRule> rules = rechargeRuleService.findAll();
        HashMap<String, Object> data = new HashMap<>();
        data.put("rechargeRules", rules);
        HomeCacheManager.loadData(HomeCacheManager.RECHARGE_RULE, data);
        return ResponseUtil.ok(data);
    }

    @GetMapping("member/levels")
    public Object memberLevels() {
        if (HomeCacheManager.hasData(HomeCacheManager.MEMBER_LEVEL)) {
            return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.MEMBER_LEVEL));
        }
        List<LitemallMemberLevel> levels = memberLevelService.findAll();
        HashMap<String, Object> data = new HashMap<>();
        data.put("memberLevels", levels);
        HomeCacheManager.loadData(HomeCacheManager.MEMBER_LEVEL, data);
        return ResponseUtil.ok(data);
    }

    @GetMapping("sign")
    public Object sign(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallGameUser gameUser = gameUserService.findByUserId(userId);
        if (gameUser == null) {
            gameUser = generateGameUser(userId);
            gameUserService.add(gameUser);
        }

        if (experienceLogService.countBySelective(userId, "sign") > 0) {
            return ResponseUtil.fail(500, "你今天已经签到过了");
        }

        LitemallExperienceLog experienceLog = generateExperienceLog(userId, 5, "sign", true);
        experienceLogService.add(experienceLog);

        HashMap<String, Object> data = new HashMap<>();
        data.put("experienceLog", experienceLog);
        return ResponseUtil.ok(data);
    }

    @GetMapping("/exchange")
    public Object exchange(@LoginUser Integer userId, Integer giftId) {
        if (userId == null) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallGameGift fruit = gameGiftService.findOneByType("fruit");
        if (fruit == null) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallGameGift gift = gameGiftService.findOneById(giftId);
        int pieceNum = gift.getNeedPiece();
        int fruitNum = gift.getNeedFruit();
        List<LitemallUserPackage> pieces = new ArrayList<>();
        List<LitemallUserPackage> fruits = new ArrayList<>();
        if (pieceNum > 0) {
            pieces = userPackageService.findBySelective(userId, giftId, "pending");
            if (pieces.size() < pieceNum) {
                return ResponseUtil.fail(500, "礼品碎片不足");
            }
        }
        if (fruitNum > 0) {
            fruits = userPackageService.findBySelective(userId, fruit.getId(), "pending");
            if (fruits.size() < fruitNum) {
                return ResponseUtil.fail(500, "人参果不足");
            }
        }
        if (fruitNum == 0 || pieceNum ==0){
            return ResponseUtil.fail(500,"此物品不可兑换，只能配合使用");
        }
        DefaultTransactionDefinition dtf = new DefaultTransactionDefinition();
        dtf.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(dtf);

        try{

            //加入已兑换列表
            LitemallExchangeLog exchangeLog = generateExchangeLog(userId, gift.getId(), gift.getName(), fruitNum, pieceNum, "pending");
            exchangeLogService.add(exchangeLog);
            //进行消耗碎片和人参果
            pieces.forEach(piece -> {
                piece.setStatus("success");
                userPackageService.update(piece);
            });
            fruits.forEach(item -> {
                item.setStatus("success");
                userPackageService.update(item);
            });
        }catch (Exception e){
            txManager.rollback(status);
            logger.error(e);
            e.printStackTrace();
        }
        txManager.commit(status);

        HashMap<String, Object> data = new HashMap<>();
        data.put("consume_piece", pieceNum);
        data.put("consume_fruit", fruitNum);
        data.put("exchange_gift", gift);

        return ResponseUtil.ok(data);
    }

    @PostMapping("/recharge/member")
    public Object memberRecharge(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = userService.findById(userId);
        int oldLevel = user.getUserLevel();
        Integer memberMoney = JacksonUtil.parseInteger(body, "memberMoney");
        String memberOrderSn = generateMemberOrderSn();

        WxPayMpOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setAttach("member_order");
            orderRequest.setOpenid(user.getWeixinOpenid());
            orderRequest.setOutTradeNo(memberOrderSn);
            orderRequest.setBody("订单：" + memberOrderSn);

            // 元转成分
            Integer fee = 0;
            BigDecimal actualPrice = new BigDecimal(memberMoney);
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);

            //缓存prepayID用于后续模版通知
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");

            LitemallMemberOrder memberOrder = generateMemberOrder(userId, memberMoney, oldLevel, memberOrderSn, prepayId);
            memberOrderService.add(memberOrder);

            LitemallUserFormid userFormid = new LitemallUserFormid();
            userFormid.setOpenid(user.getWeixinOpenid());
            userFormid.setFormid(prepayId);
            userFormid.setIsprepay(true);
            userFormid.setUseamount(3);
            userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
            formIdService.addUserFormid(userFormid);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.fail(403, "订单不能支付");
        }

        return ResponseUtil.ok(result);
    }

    @PostMapping("/recharge/xianyuan")
    public Object rechargeXianYuan(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = userService.findById(userId);
        Integer money = JacksonUtil.parseInteger(body, "money");
        LitemallRechargeRule rechargeRule = rechargeRuleService.findOneBySelective(String.valueOf(money));
        String gameRechargeSn = generateGameRechargeSn();

        WxPayMpOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setAttach("game_recharge");
            orderRequest.setOpenid(user.getWeixinOpenid());
            orderRequest.setOutTradeNo(gameRechargeSn);
            orderRequest.setBody("订单：" + gameRechargeSn);

            // 元转成分
            Integer fee = 0;
            BigDecimal actualPrice = new BigDecimal(money);
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);

            //缓存prepayID用于后续模版通知
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");

            LitemallGameRecharge gameRecharge = generateGameRecharge(userId, money, gameRechargeSn, prepayId, rechargeRule.getXianyuan());
            gameRechargeService.add(gameRecharge);

            addUserFormId(user, prepayId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.fail(403, "订单不能支付");
        }

        return ResponseUtil.ok(result);
    }

    @GetMapping("userPackage")
    public Object userPackage(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        List<Map> userPackages = statService.statUserPackages(userId, "pending");
        HashMap<String, Object> data = new HashMap<>();
        data.put("userPackages", userPackages);
        return ResponseUtil.ok(data);
    }

    @GetMapping("exchangeRecord")
    public Object exchangeRecord(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        List<LitemallExchangeLog> exchangeLogs = exchangeLogService.findBySelective(userId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("exchangRecords", exchangeLogs);
        return ResponseUtil.ok(data);
    }

    private LitemallGameRecharge generateGameRecharge(Integer userId, Integer money, String rechargeSn, String prepayId, Integer xianyuan) {
        LitemallGameRecharge gameRecharge = new LitemallGameRecharge();
        gameRecharge.setAddTime(LocalDateTime.now());
        gameRecharge.setDeleted(false);
        gameRecharge.setPrepayId(prepayId);
        gameRecharge.setRechargeId(rechargeSn);
        gameRecharge.setRechargeMoney(money);
        gameRecharge.setStatus("pending");
        gameRecharge.setUserId(userId);
        gameRecharge.setXianyuan(xianyuan);
        return gameRecharge;
    }

    private void addUserFormId(LitemallUser user, String prepayId) {
        LitemallUserFormid userFormid = new LitemallUserFormid();
        userFormid.setOpenid(user.getWeixinOpenid());
        userFormid.setFormid(prepayId);
        userFormid.setIsprepay(true);
        userFormid.setUseamount(3);
        userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
        formIdService.addUserFormid(userFormid);
    }

    private LitemallMemberOrder generateMemberOrder(Integer userId, Integer memberMoney, Integer oldLevel, String memberOrderSn, String prepayId) {
        LitemallMemberOrder memberOrder = new LitemallMemberOrder();
        memberOrder.setAddTime(LocalDateTime.now());
        memberOrder.setDeleted(false);
        memberOrder.setMemberMoney(memberMoney);
        LitemallMemberLevel memberLevel = memberLevelService.findOneBySelective(memberMoney);
        memberOrder.setNowLevel(memberLevel.getLevel());
        memberOrder.setOldLevel(oldLevel);
        memberOrder.setMemberOrderId(memberOrderSn);
        memberOrder.setPrepayId(prepayId);
        memberOrder.setStatus("pending");
        memberOrder.setUserId(userId);

        return memberOrder;
    }

    private String generateMemberOrderSn() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String sn = df.format(LocalDateTime.now());
        for (sn = "mo_" + sn + getRandomNum(4); memberOrderService.countBySelective(sn) > 0; sn = "mo_" + sn + getRandomNum(4)) {
        }
        return sn;
    }

    private String generateGameRechargeSn() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHiiss");
        String sn = df.format(LocalDateTime.now());
        for (sn = "re_" + sn + getRandomNum(4); memberOrderService.countBySelective(sn) > 0; sn = "re_" + sn + getRandomNum(4)) {
        }
        return sn;
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < num; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    private LitemallExchangeLog generateExchangeLog(Integer userId, Integer giftId, String giftName, int needFruit, int needPiece, String status) {
        LitemallExchangeLog exchangeLog = new LitemallExchangeLog();
        exchangeLog.setAddTime(LocalDateTime.now());
        exchangeLog.setDeleted(false);
        exchangeLog.setGiftId(giftId);
        exchangeLog.setGiftName(giftName);
        exchangeLog.setNeedFruit(needFruit);
        exchangeLog.setNeedPiece(needPiece);
        exchangeLog.setStatus(status);
        exchangeLog.setUserId(userId);

        return exchangeLog;
    }

    private void addExperienceByPlayGame(Integer userId) {
        if (experienceLogService.countBySelective(userId, "playGame") < 1) {
            experienceLogService.add(generateExperienceLog(userId, 1, "playGame", true));
        }
    }

    private LitemallExperienceLog generateExperienceLog(Integer userId, Integer experience, String action, boolean opera) {
        LitemallExperienceLog experienceLog = new LitemallExperienceLog();
        experienceLog.setAction(action);
        experienceLog.setAddTime(LocalDateTime.now());
        experienceLog.setDeleted(false);
        experienceLog.setExperience(experience);
        experienceLog.setOpera(opera);
        experienceLog.setUserId(userId);
        return experienceLog;
    }

    private LitemallPointLog generatePointLog(Integer userId, String action, boolean opera, Integer point) {
        LitemallPointLog pointLog = new LitemallPointLog();
        pointLog.setAction(action);
        pointLog.setAddTime(LocalDateTime.now());
        pointLog.setDeleted(false);
        pointLog.setOpera(opera);
        pointLog.setPoint(point);
        pointLog.setUserId(userId);
        return pointLog;
    }

    private LitemallUserPackage addUserPackage(Integer userId, Integer gameId, Integer giftId) {
        LitemallUserPackage userPackage = new LitemallUserPackage();
        userPackage.setAddTime(LocalDateTime.now());
        userPackage.setDeleted(false);
        userPackage.setGameId(gameId);
        userPackage.setGiftId(giftId);
        //status=0 为待使用
        userPackage.setUserId(userId);
        userPackage.setStatus("pending");
        userPackageService.add(userPackage);
        return userPackage;
    }

    private boolean isMember(Integer userId) {
        LitemallUser user = userService.findById(userId);
        if (user == null) {
            return false;
        }
        if (user.getUserLevel() > 0) {
            return true;
        }
        return false;
    }

    private boolean gameValidate(Integer userId) {
        int week = LocalDate.now().getDayOfWeek().getValue();
        int num = gameLogService.countBySelective(userId, "success", true);
        if (week < 6) {
            //会员
            if (isMember(userId)) {
                if (num < 8) return true;
            } else {
                //非会员
                if (num < 2) return true;
            }
            return false;
        }
        //按会员等级来看
        int level = userService.findById(userId).getUserLevel();
        if (level == 0 && num < 15) return true;
        if (level == 1 && num < 20) return true;
        if (level == 2 && num < 25) return true;
        if (level == 3 && num < 30) return true;
        if (level == 4 && num < 35) return true;
        return false;
    }

    @GetMapping("canPlayGameNum")
    public Object canPlayGameNum(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        int canPlayGameNum = 0;
        int week = LocalDate.now().getDayOfWeek().getValue();
        int num = gameLogService.countBySelective(userId, "success", true);
        if (week < 6) {
            //会员
            if (isMember(userId)) {
                canPlayGameNum = 8 - num;
            } else {
                //非会员
                if (num < 2) canPlayGameNum = 2 - num;
            }
        } else {
            //按会员等级来看
            int level = userService.findById(userId).getUserLevel();
            if (level == 0 && num < 15) canPlayGameNum = 15 - num;
            if (level == 1 && num < 20) canPlayGameNum = 20 - num;
            if (level == 2 && num < 25) canPlayGameNum = 25 - num;
            if (level == 3 && num < 30) canPlayGameNum = 30 - num;
            if (level == 4 && num < 35) canPlayGameNum = 35 - num;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("canPlayGameNum", canPlayGameNum);
        return ResponseUtil.ok(data);
    }

    /**
     * 1分钟执行一次
     */
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void autoGenerateGameLog() {
        logger.debug(LocalDateTime.now() + "autogenerateGameLog start");
        List<LitemallGameGift> giftList = gameGiftService.findAll();
        List<LitemallGameUser> userList = gameUserService.findAll();

        userList.forEach(gameUser -> {
            try {
                Integer userId = gameUser.getUserId();
                List<LitemallGameLog> gameLogList = gameLogService.findByUserId(userId);
                LitemallGameGift gameGift = gameGiftService.findOneByType("fruit");
                Integer fruitId = gameGift.getId();
                List<LitemallGameLog> newGameLogList = null;
                newGameLogList = generateGameLogList(50, generateGameLog(userId, fruitId));

                gameLogList.addAll(newGameLogList);
                int size = gameLogList.size();

                for (int i = 0; i < giftList.size(); i++) {
                    LitemallGameGift gift = giftList.get(i);
                    int before = gift.getBefore();
                    int after = gift.getAfter();

                    if (size < before) continue;
                    gameLogList = checkGiftRuleOnGameLog(gameLogList, 0, before, gift.getId(), fruitId);
                    int limit = before + after;
                    while (size > limit) {
                        int start = limit - after;
                        gameLogList = checkGiftRuleOnGameLog(gameLogList, start, limit, gift.getId(), fruitId);
                        limit += after;
                    }
                }

                logger.debug(gameLogList.size());
                List<LitemallGameLog> insertGameLog = new ArrayList<>();
                gameLogList.forEach(gameLog -> {
                    if (gameLog.getId() == null) {
                        insertGameLog.add(gameLog);
                    }
                });
                insertGameLog.forEach(gameLog -> {
                    gameLogService.add(gameLog);
                });
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        logger.debug(LocalDateTime.now() + "autogenerateGameLog end");
    }

    private List<LitemallGameLog> checkGiftRuleOnGameLog(List<LitemallGameLog> list, int start, int end, Integer giftId, Integer fruitId) {
        List<Integer> index = new ArrayList<>();
        for (; start < end; start++) {
            if (Objects.equals(list.get(start).getGiftId(), giftId)) return list;
            if (Objects.equals(list.get(start).getGiftId(), fruitId)) {
                index.add(start);
            }
        }
        if (index.size() < 1) {
            return list;
        }
        Random random = new Random();
        int listIndex = random.nextInt(index.size());
        LitemallGameLog gameLog = list.get(index.get(listIndex));
        gameLog.setGiftId(giftId);
        gameLog.setType("generate");
        list.set(index.get(listIndex), gameLog);

        return list;
    }

    private LitemallGameLog generateGameLog(Integer userId, Integer giftId) {
        LitemallGameLog gameLog = new LitemallGameLog();
        gameLog.setStatus("pending");
        gameLog.setDeleted(false);
        gameLog.setAddTime(LocalDateTime.now());
        gameLog.setUserId(userId);
        gameLog.setType("generate");
        gameLog.setGiftId(giftId);
        return gameLog;
    }

    private List<LitemallGameLog> generateGameLogList(int size, LitemallGameLog gameLog) throws CloneNotSupportedException {
        if (gameLog == null) {
            return null;
        }
        List<LitemallGameLog> gameLogList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            gameLogList.add(gameLog.clone());
        }
        return gameLogList;
    }


}
