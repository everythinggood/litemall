var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userPackage:[
      {
        name: '丰田卡罗兰',
        num: 100,
        type: 'piece'
      },
      {
        name: '成长人参果',
        num: 100,
        type: 'fruit'
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
    // if (!app.globalData.hasLogin) {
    //   wx.navigateTo({
    //     url: "/pages/auth/login/login"
    //   });
    // }
    this.getUserPackage();
  },
  getUserPackage(){
    let that = this;
    util.request(api.GameUserPackage).then(function(res){
      if(res.errno == 0){
        that.setData({
          userPackage:res.data.userPackages
        })
      };
    })
  },
  exchange(event){
    console.log(event);
    let giftId = event.currentTarget.dataset.id;

    let that = this;
    util.request(api.GameExchange,{"giftId":giftId}).then(function(res){
      if(res.errno == 0){
        console.log(res.data);
        wx.showModal({
          title: '兑换成功',
          content: '您获得了【'+res.data.exchange_gift.name+'】礼品',
        });
        that.getUserPackage();
      } else {
        wx.showModal({
          title: '兑换失败',
          content: res.errmsg,
        })
      }
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