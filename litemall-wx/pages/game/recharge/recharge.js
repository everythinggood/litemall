var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    memberLevels:[
      {
        id: 1,
        name: '青铜会员',
        money: 100
      }
    ],
    rechargeRules:[
      {
        id: 1,
        name: '1000仙缘',
        money: 100
      }
    ]
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
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if(!app.globalData.hasLogin){
      wx.navigateTo({
        url: '/pages/auth/login/login',
      })
    }
    this.getMemberLevels();
    this.getRechargeRules();
  },
  getMemberLevels(){
    let that = this;
    util.request(api.GameMemberLevels).then(function(res){
      if(res.errno == 0){
        that.setData({
          memberLevels:res.data.memberLevels
        });
      }
    });
  },
  getRechargeRules(){
    let that = this;
    util.request(api.GameRechargeRules).then(function(res){
      if(res.errno == 0){
        that.setData({
          rechargeRules:res.data.rechargeRules
        });
      }
    });
  },
  rechargeMember(event){
    console.log(event);
    let money = event.currentTarget.dataset.money;
    let that = this;
    util.request(api.GameRechargeMember,{"memberMoney":money},'POST').then(function(res){
      if(res.errno == 0){
        console.log(res.data);
      }else{
        wx.showModal({
          title: '充值失败',
          content: res.errmsg,
        })
      }
    }).catch(function(res){
      wx.showModal({
        title: '充值失败',
        content: res.errmsg,
      })
    });
  },
  rechargeXianYuan(){
    console.log(event);
    let money = event.currentTarget.dataset.money;
    let that = this;
    util.request(api.GameRechargeMember, { "money": money },'POST').then(function (res) {
      if (res.errno == 0) {
        console.log(res.data);
      } else {
        wx.showModal({
          title: '充值失败',
          content: res.errmsg,
        })
      }
    });
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