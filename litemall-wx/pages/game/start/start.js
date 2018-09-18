var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
      userInfo :{
        name: '艺',
        level: 1,
        memberName: '青铜会员',
        experience: 100,
        xianyuan: 500,
        fruit: 200,
        point:200
      },
      giftList: [
        {
          name: '丰田卡罗拉',
          type: 'piece',
          ruleType: 'exchange',
          ruleName: '4888个碎片可兑换'
        }
      ],
      canGameNum: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    if(!app.globalData.hasLogin){
      wx.navigateTo({
        url: '/pages/auth/login/login'
      });
    }
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getGameUserInfo();
    this.getGiftList();
    this.getCanPlayGameNum();
  },
  getGameUserInfo(){
    let that = this;
    util.request(api.GameUserInfo).then(function(res){
      if(res.errno == 0){
        that.setData({
          userInfo:res.data
        })
      }
    })
  },
  getGiftList(){
    let that = this;
    util.request(api.GameGiftList).then(function(res){
      if(res.errno == 0){
        that.setData({
          giftList: res.data.giftList
        })
      }
    })
  },
  getCanPlayGameNum(){
    let that = this;
    util.request(api.CanPlayGameNum).then(function(res){
      if(res.errno == 0){
        that.setData({
          canGameNum:res.data.canPlayGameNum
        })
      }
    });
  },
  userPackage(){
    wx.navigateTo({
      url: '/pages/game/userPackage/userPackage'
    });
  },
  gameStart(){
    let that = this;
    util.request(api.GameStart).then(function(res){
      if(res.errno == 0){
        that.getCanPlayGameNum();
        console.log(res.data);
        wx.showToast({
          title: '恭喜得到['+res.data.gift.name+']礼品碎片',
        });
      }else{
        wx.showToast({
          title: res.errmsg,
          icon: 'none'
        });
      }
    });

  },
  recharge(){
    wx.navigateTo({
      url: '/pages/game/recharge/recharge',
    })
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})