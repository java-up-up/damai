<template>
  <!--点击进入单独界面详情-->
  <div class="app-container">
    <Header></Header>
    <div class="app-container">
      <div class="wrapper">
        <div class="box-left">
          <div class="box-detail">
            <div class="count">
              <div class="box-img"><img :src="detailList.itemPicture" alt=""></div>
              <div class="order">
                <div class="title">
                  <span class="tips">电子票</span>
                  <span>{{ detailList.title }}</span>
                </div>
                <div class="address">
                  <div class="time">时间：{{ formatDateWithWeekday(detailList.showTime, detailList.showWeekTime) }}</div>
                  <div class="place">
                    <div class="addr">场馆：{{ detailList.areaName }}|{{ detailList.place }}</div>
                  </div>
                </div>
                <!--                预售-->
                <div class="notice" v-show="detailList.preSell=='1'">
                  <div class="ticket-type"><span v-if="detailList.preSell=='1'">预售</span></div>
                  <div class="content">
                    <div>{{ detailList.preSellInstruction }}</div>
                    <div class="notice-content">
                      {{ detailList.importantNotice }}
                    </div>
                  </div>
                </div>
                <div class="citys">
                  <span>城市</span>
                  <div class="city-list">
                    <div class="city-item activeCity" :id="detailList.areaId">{{ detailList.areaName }}</div>
                  </div>
                  <div class="city-more">查看更多</div>
                </div>
                <div class="order-box">
                  <div class="notice-time">场次时间均为演出当地时间</div>
                  <div class="order-time">
                    <div class="order-name">场次</div>
                    <div class="select">
                      <div class="select-list">
                        <div class="select-list-item activeCity">
                          <span>{{ formatDateWithWeekday(detailList.showTime, detailList.showWeekTime) }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="order-box">
                  <div class="order-time">
                    <div class="order-name">票档</div>
                    <div class="select">
                      <div class="select-list" v-for="(item,index) in  ticketCategoryVoList">
                        <div class="select-list-item " @click="ticketClick(item,index)"
                             :class="{ticket: actvieIndex == index}">
                          <span>{{ item.introduce }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="order-price">
                  <div class="num">数量</div>
                  <div class="count">
                    <div class="count-info">
                      <el-input-number v-model="num" :min="1" :max="6" @change="handleChange"/>
                    </div>
                    <div class="num-limit">每笔订单限购6张</div>
                  </div>
                </div>
                <div class="order-box">
                  <div class="order-time">
                    <div class="order-name">合计</div>
                    <div class="order-count" v-if="allPrice==''">￥{{countPrice }}</div>
                    <div class="order-count" v-else>￥{{allPrice }}</div>
                  </div>
                </div>
                <div class="buy">
                  <div class="buy-link-now" @click="nowBuy">立即购买</div>
                  <!--                    <router-link class="buy-link" to="/order/index">不，选座购买</router-link>-->
                  <!--                  <div class="subtitle">请您移步手机端购买</div>
                                    <div class="qrcode">
                                      <div class="tip">手机扫码购买更便捷</div>
                                      <div class="J_qrcodeImg"></div>
                                      <div class="buy-link" @click="nowBuy">不，立即购买</div>

                                    </div>-->
                </div>

              </div>
            </div>
          </div>
          <div class="box-item">
            <div class="box-menu">
              <router-link class="menu-children" to="#projectDetial" @click="detialClick('#projectDetial',1)"
                           :class="{menuActive: menuActive == 1}">项目详情
              </router-link>
              <router-link class="menu-children" to="#ticketNeed" @click="detialClick('#ticketNeed',2)"
                           :class="{menuActive: menuActive == 2}">购票须知
              </router-link>
              <router-link class="menu-children" to="#watchNeed" @click="detialClick('#watchNeed',3)"
                           :class="{menuActive: menuActive == 3}">观演须知
              </router-link>
            </div>
            <div id="projectDetial">
              <div class="proDetial">活动介绍</div>
              <img :src="detailList.detail" alt="">
            </div>
            <div id="ticketNeed">
              <div class="proDetial">购票须知</div>
              <ul>
                <li v-for="item in ticketNeedInfo">
                  <span>{{ item.name }}</span>
                  <div>{{ item.value }}</div>
                </li>

              </ul>
            </div>
            <div id="watchNeed">
              <div class="proDetial">观演须知</div>
              <ul>
                <li v-for="item in watchNeedInfo">
                  <span v-if="item.value!=''">{{ item.name }}</span>
                  <div v-if="item.value!=''">{{ item.value }}</div>
                </li>

              </ul>
            </div>
          </div>
        </div>
        <div class="box-right">
          <div class="service">

            <div class="sit" v-show="detailList.permitChooseSeat=='1'">查看座位图</div>
            <div class="service-note">
              <div class="service-name" v-if="detailList.permitRefund!=''">
                <i class="icon-no" v-if="detailList.permitRefund=='0'"></i><span v-if="detailList.permitRefund=='0'">不支持退</span>
                <i class="icon-yes" v-if="detailList.permitRefund=='1'"></i><span v-if="detailList.permitRefund=='1'">条件退</span>
                <i class="icon-yes" v-if="detailList.permitRefund=='2'"></i><span v-if="detailList.permitRefund=='2'">全部退</span>
              </div>
              <div class="service-desc" v-if="detailList.refundExplain!=''">{{ detailList.refundExplain }}</div>
              <div class="service-name" v-if="detailList.relNameTicketEntrance!=''">
                <i class="icon-no" v-if="detailList.relNameTicketEntrance=='0'"></i><span
                  v-if="detailList.relNameTicketEntrance=='0'">不实名购票和入场</span>
                <i class="icon-yes" v-if="detailList.relNameTicketEntrance=='1'"></i><span
                  v-if="detailList.relNameTicketEntrance=='1'">实名购票和入场</span>

              </div>
              <div class="service-desc"  v-if="detailList.relNameTicketEntranceExplain!=''">{{ detailList.relNameTicketEntranceExplain }}</div>
              <div class="service-name"   v-if="detailList.permitChooseSeat!=''">
                <i class="icon-no" v-if="detailList.permitChooseSeat=='0'"></i><span
                  v-if="detailList.permitChooseSeat=='0'">不支持选座</span>
                <i class="icon-yes" v-if="detailList.permitChooseSeat=='1'"></i><span
                  v-if="detailList.permitChooseSeat=='1'">支持选座</span>

              </div>
              <div class="service-desc"  v-if="detailList.chooseSeatExplain!=''">{{ detailList.chooseSeatExplain }}</div>
              <div class="service-name" v-if="detailList.electronicDeliveryTicket!=''">
                <i class="icon-no" v-if="detailList.electronicDeliveryTicket=='0'"></i><span
                  v-if="detailList.electronicDeliveryTicket=='0'">无票</span>
                <i class="icon-yes" v-if="detailList.electronicDeliveryTicket=='1'"></i><span
                  v-if="detailList.electronicDeliveryTicket=='1'">电子票</span>
                <i class="icon-yes" v-if="detailList.electronicDeliveryTicket=='2'"></i><span
                  v-if="detailList.electronicDeliveryTicket=='2'">快递票</span>

              </div>
              <div class="service-desc"  v-if="detailList.electronicDeliveryTicketExplain!=''">{{ detailList.electronicDeliveryTicketExplain }}</div>
              <div class="service-name"  v-if="detailList.electronicInvoice!=''">
                <i class="icon-no" v-if="detailList.electronicInvoice=='0'"></i><span
                  v-if="detailList.electronicInvoice=='0'">纸质发票</span>
                <i class="icon-yes" v-if="detailList.electronicInvoice=='1'"></i><span
                  v-if="detailList.electronicInvoice=='1'">电子发票</span>

              </div>
              <div class="service-desc"  v-if="detailList.electronicInvoiceExplain!=''">{{ detailList.electronicInvoiceExplain }}</div>
            </div>

          </div>
          <div class="box-like">
            为你推荐
          </div>
          <ul class="search__box">
            <li class="search__item" v-for="item in recommendList">
              <router-link :to="{name:'detial',params:{id:item.id}}" class="link" >
                <img :src="item.itemPicture" alt="">
                <router-view :key="route.fullpath" />

              </router-link>

              <div class="search_item_info">
                <router-link :to="{name:'detial',params:{id:item.id}}"  class="link__title" >
                  <router-view :key="route.fullpath"/>
                  {{ item.title }}

                </router-link>
                <div class="search__item__info__venue">{{ item.place }}</div>
                <div class="search__item__info__venue">{{ formatDateWithWeekday(item.showTime, item.showWeekTime) }}</div>
                <div class="search__item__info__price">￥<strong>{{ item.minPrice }}</strong> 起</div>
              </div>

            </li>
          </ul>
        </div>
      </div>
    </div>

    <Footer></Footer>

  </div>

</template>

<script setup name="detial">
import Header from '@/components/header/index'
import Footer from '@/components/footer/index'
import {formatDateWithWeekday } from '@/utils/index'
import {useRoute, useRouter} from 'vue-router'
import {getProgramDetials} from '@/api/contentDetail'
import {ref} from 'vue'
import {   useMitt } from "@/utils/index";
import {getProgramRecommendList} from "@/api/recommendlist.js"
const emitter = useMitt();
//引入reactive
import {reactive} from 'vue'
const route = useRoute();
const router = useRouter();
// 获取路由参数
const paramValue = Number(route.params.id);
const detailList = ref([])
const ticketCategoryVoList = ref([])
const actvieIndex = ref('')
const menuActive = ref('')
const ticketNeedInfo = ref([])
const watchNeedInfo = ref([])
const num = ref(1)
const countPrice = ref('')
const allPrice = ref('')
//票档id
const ticketCategoryId = ref('')
//推荐节目列表入参
const recommendParams = reactive({
  areaId: undefined,
  parentProgramCategoryId: undefined,
  programId: undefined
})
recommendParams.programId = paramValue;
//推荐列表数据
const recommendList = ref(0)
getProgramDetialsList()

function getProgramDetialsList() {
  getProgramDetials({id: paramValue}).then(response => {
    detailList.value = response.data
    ticketCategoryVoList.value = detailList.value.ticketCategoryVoList
    countPrice.value=ticketCategoryVoList.value[0].price
    ticketCategoryId.value = ticketCategoryVoList.value[0].id
    allPrice.value = countPrice.value * num.value; // 初始总价 = 默认单价 × 默认数量（1）
    // allPrice.value = ''      原始代码
    ticketNeedInfo.value = [{
      name: '限购规则',
      value: detailList.value.purchaseLimitRule,
    }, {
      name: '退票/换票规则',
      value: detailList.value.refundTicketRule,
    }, {
      name: '入场规则',
      value: detailList.value.entryRule,
    }, {
      name: '儿童购票',
      value: detailList.value.childPurchase,
    }, {
      name: '发票说明',
      value: detailList.value.invoiceSpecification,
    }, {
      name: '实名购票规则',
      value: detailList.value.realTicketPurchaseRule,
    }, {
      name: '异常排单说明',
      value: detailList.value.abnormalOrderDescription,
    }]
    watchNeedInfo.value = [{
      name: '演出时长',
      value: detailList.value.performanceDuration
    }, {
      name: '入场时间',
      value: detailList.value.entryTime
    }, {
      name: '最低演出曲目',
      value: detailList.value.minPerformanceCount
    }, {
      name: '主要演员',
      value: detailList.value.mainActor
    }, {
      name: '最低演出时长',
      value: detailList.value.minPerformanceDuration
    }, {
      name: '禁止携带物品',
      value: detailList.value.prohibitedItem
    }, {
      name: '寄存说明',
      value: detailList.value.depositSpecification
    }]
  })
}

const ticketClick = (item, index) => {
  actvieIndex.value = index;
  countPrice.value = item.price; // 显式更新单价
  allPrice.value = item.price * num.value; // 总价 = 新单价 × 当前数量
  ticketCategoryId.value = item.id;
  // actvieIndex.value = index     原本代码
  // allPrice.value = item.price      原本代码
  // ticketCategoryId.value = item.id      原本代码
}
const detialClick = (url, index) => {
  menuActive.value = index


}
const handleChange = (value) => {
  const priceEach= countPrice.value
  allPrice.value = priceEach*value

}
const nowBuy=()=>{
  router.replace({path:'/order/index',state:
        {'detailList':JSON.stringify(detailList.value),'allPrice':allPrice.value,
          'countPrice':countPrice.value,'num':num.value,'ticketCategoryId':ticketCategoryId.value}})

}

getRecommendList()

//节目推荐列表
function getRecommendList(){
  getProgramRecommendList(recommendParams).then(response => {
    recommendList.value = response.data.slice(0,6);
  })
}







</script>

<style scoped lang="scss">
.app-container {
  width: 1200px;
  margin: 0 auto;
  overflow: auto;

  .wrapper {
    display: flex;

    .box-left {
      flex: 1;

      .box-detail {
        position: relative;
        padding: 40px 0 30px;
        min-height: 360px;

        .count {
          padding-left: 330px;
          font-size: 22px;
          color: #000;

          .box-img {
            img {
              position: absolute;
              left: 30px;
              top: 40px;
              width: 270px;
              height: 360px;
            }
          }

          .order {
            position: relative;
            padding-right: 30px;

            .title {
              .tips {
                display: inline-block;
                width: 60px;
                height: 24px;
                position: relative;
                top: -3px;
                text-align: center;
                line-height: 24px;
                //background: -webkit-linear-gradient(135deg, rgba(255, 55, 29, 0.85), #ff5593);
                //background: -moz-linear-gradient(135deg, rgba(255, 55, 29, 0.85) 0, #ff5593 100%);
                //background: linear-gradient(-45deg, rgba(255, 55, 29, 0.85), #ff5593);
                background: #96A3FF;
                z-index: 10;
                font-size: 14px;
                color: #fff;
                border-bottom-right-radius: 10px;
                border-top-left-radius: 3px;
                border-top-right-radius: 3px
              }

              span {

              }
            }

            .address {
              position: relative;
              font-size: 16px;
              color: #4a4a4a;
              margin-top: 21px;
              zoom: 1;

              .time {
                padding-bottom: 10px;
              }

              .place {
                .addr {
                  display: inline-block;
                }
              }
            }

            .notice {
              margin-top: 18px;
              padding: 12px 15px;
              font-size: 12px;
              background: #f6f6f6;
              border-radius: 4px;
              position: relative;

              .ticket-type {
                display: inline-block;
                height: 24px;
                line-height: 23px;
                text-align: center;
                padding: 0 7px;
                color: rgba(255, 55, 29, 0.85);
                background: #ffe7ef;
                border-radius: 0 100px 100px 0;
                margin-bottom: 10px;
                font-size: 14px;

                span {
                  vertical-align: middle;
                }
              }

              .content {
                -webkit-box-flex: 1;
                -webkit-flex: 1;
                -moz-box-flex: 1;
                flex: 1;
                display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;
                overflow: hidden;
                text-overflow: ellipsis;
                cursor: pointer;

                div {

                }

                .notice-content {
                  color: #999;
                }
              }
            }

            .citys {
              margin-top: 24px;

              span {
                display: inline-block;
                font-size: 16px;
                color: #000;
              }

              .city-list {
                display: inline-block;
                font-size: 12px;
                width: 420px;
                height: 40px;
                overflow: hidden;
                vertical-align: top;

                .city-item {
                  color: #000;
                  width: 78px;
                  height: 40px;
                  -webkit-box-sizing: border-box;
                  -moz-box-sizing: border-box;
                  box-sizing: border-box;
                  border: 1px solid #eee;
                  text-align: center;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                  display: -webkit-inline-flex;
                  display: inline-flex;
                  -webkit-box-align: center;
                  -webkit-align-items: center;
                  -moz-box-align: center;
                  align-items: center;
                  -webkit-box-pack: center;
                  -webkit-justify-content: center;
                  -moz-box-pack: center;
                  justify-content: center;
                  margin-right: 6px;
                  margin-bottom: 8px;
                  cursor: pointer;
                  border-radius: 3px;
                  margin-left: 17px;
                }


              }

              .city-more {
                float: right;
                font-size: 12px;
                color: #4a4a4a;
                cursor: pointer;
                margin-top: 10px;
              }
            }

            .order-box {
              .notice-time {
                color: #999;
                font-size: 12px;
                margin: 24px 0 9px;
              }

              .order-time {
                display: -webkit-box;
                display: -webkit-flex;
                display: -moz-box;
                display: flex;
                margin-top: 0px;

                .order-name {
                  display: inline-block;
                  font-size: 16px;
                  color: #000;
                  height: 48px;
                }

                .select {
                  display: inline-block;
                  vertical-align: top;
                  margin-left: 15px;
                  -webkit-box-flex: 1;
                  -webkit-flex: 1;
                  -moz-box-flex: 1;
                  flex: 1;

                  .select-list {
                    display: -webkit-box;
                    display: -webkit-flex;
                    display: -moz-box;
                    display: flex;
                    -webkit-box-orient: horizontal;
                    -webkit-box-direction: normal;
                    -webkit-flex-direction: row;
                    -moz-box-orient: horizontal;
                    -moz-box-direction: normal;
                    flex-direction: row;
                    -webkit-flex-wrap: wrap;
                    flex-wrap: wrap;
                    float: left;

                    .select-list-item {
                      -webkit-box-orient: vertical;
                      -webkit-box-direction: normal;
                      -webkit-flex-direction: column;
                      -moz-box-orient: vertical;
                      -moz-box-direction: normal;
                      flex-direction: column;
                      -webkit-box-pack: center;
                      -webkit-justify-content: center;
                      -moz-box-pack: center;
                      justify-content: center;
                      display: -webkit-box;
                      display: -webkit-flex;
                      display: -moz-box;
                      display: flex;
                      float: left;
                      font-size: 12px;
                      color: #000;
                      padding: 10px 24px;
                      margin: 0 6px 6px 0;
                      position: relative;
                      cursor: pointer;
                      border-radius: 3px;
                      background: #f6f7f8;
                      border: 1px solid rgba(0, 0, 0, .1);
                      text-align: left;

                      span {
                        text-align: left;
                        margin: 1px 0;
                      }

                      .notticket {
                        background-color: transparent;
                        color: #6a7a99 !important;
                        border-color: #6a7a99 !important;
                      }
                    }
                  }
                }

                .order-count {
                  font-size: 21px;
                  color: rgba(255, 55, 29, 0.85);
                  margin-left: 9px;
                  font-weight: bold;
                }
                .count-detial{
                  position: relative;
                  font-size: 12px;
                  color: #000;
                  cursor: pointer;
                  margin-top: 10px;
                  margin-left: 5px;
                }
              }


            }

            .order-price {
              display: flex;

              .num {
                font-size: 16px;
                color: #000;
                height: 48px;
                flex: 0.1;
              }

              .count {
                display: inline-block;
                padding-left: 0px;
                width: 340px;
                flex: 1;

                .count-info {
                  //margin-top: 30px;
                  float: left;
                }

                .num-limit {
                  font-size: 12px;
                  color: #999;
                  line-height: 22px;
                  vertical-align: text-top;
                  display: inline-block;
                  float: left;
                  margin-top: 12px;
                  margin-left: 10px;
                }
              }
            }

            .buy {
              display: inline-block;
              margin-top: 20px;
              .buy-link-now{
                width: 100px;
                display: block;
                margin-bottom: 24px;
                height: 35px;
                line-height: 35px;
                font-size: 12px;
                text-align: center;
                color: #fff;
                cursor: pointer;
                background-color: rgba(255, 55, 29, 0.85);
                border-radius: 36px;
              }


              .title {
                font-size: 18px;
                line-height: 24px;
                color: rgba(255, 55, 29, 0.85);
                cursor: pointer;
              }

              .subtitle {
                margin-top: 4px;
                font-size: 12px;
                line-height: 18px;
                color: #999;

              }

              .qrcode {
                margin-top: 12px;
                border: 1px solid #ededed;
                border-radius: 12px;
                padding: 0 28px;

                .tip {
                  font-size: 14px;
                  line-height: 22px;
                  color: #333;
                  margin: 12px auto 4px;
                }

                .J_qrcodeImg {
                  display: -webkit-box;
                  display: -webkit-flex;
                  display: -moz-box;
                  display: flex;
                  -webkit-box-align: center;
                  -webkit-align-items: center;
                  -moz-box-align: center;
                  align-items: center;
                  margin-bottom: 8px;
                }

                .buy-link {
                  font-size: 12px;
                  line-height: 18px;
                  color: #999;
                  margin: 4px auto 12px;
                  text-decoration: underline;
                  cursor: pointer;
                }
              }

            }

          }
        }

      }

      .box-item {
        width: 100%;
        height: 800px;

        .box-menu {
          height: 54px;
          line-height: 54px;
          padding-left: 30px;
          border-top: 1px solid #e2e2e2;
          border-bottom: 1px solid #e2e2e2;
          //padding: 60px 30px 0;

          .menu-children {
            font-size: 16px;
            color: #9c9ca5;
            margin-right: 60px;
            cursor: pointer;
          }

        }

        #projectDetial {
          //width: 100%;
          //height:100%;
          padding: 60px 30px 0;

          .proDetial {
            padding-bottom: 14px;
            margin-bottom: 23px;
            font-size: 20px;
            color: #000;
            border-bottom: 1px solid #e2e2e2;
          }

          img {
            width: 100%;
            height:100%;
            display: block;
            padding-bottom: 50px;
          }
        }

        #ticketNeed {
          width: 100%;
          height: 600px;
          padding: 60px 30px 0;

          .proDetial {
            padding-bottom: 14px;
            margin-bottom: 23px;
            font-size: 20px;
            color: #000;
            border-bottom: 1px solid #e2e2e2;
          }

          ul {
            margin: 0;
            padding: 0;

            li {
              list-style: none;

              span {
                width: 100%;
                height: 20px;
                line-height: 20px;
                display: block;
                color: #999;
                font-size: 13px;
              }

              div {
                line-height: 26px;
                padding-bottom: 15px;
                font-size: 16px;
                color: #4a4a4a;
              }
            }
          }
        }

        #watchNeed {
          width: 100%;
          height: 500px;
          padding: 60px 30px 0;
          //padding-bottom: 14px;
          //margin-bottom: 23px;
          //font-size: 20px;
          //color: #000;
          //border-bottom: 1px solid #e2e2e2;
          .proDetial {
            padding-bottom: 14px;
            margin-bottom: 23px;
            font-size: 20px;
            color: #000;
            border-bottom: 1px solid #e2e2e2;
          }

          ul {
            margin: 0;
            padding: 0;

            li {
              list-style: none;

              span {
                width: 100%;
                height: 20px;
                line-height: 20px;
                display: block;
                color: #999;
                font-size: 13px;
              }

              div {
                line-height: 26px;
                padding-bottom: 15px;
                font-size: 16px;
                color: #4a4a4a;
              }
            }
          }
        }
      }
    }

    .box-right {
      box-sizing: border-box;
      width: 320px;
      border-left: 1px solid #ebebeb;
      padding: 40px 18px 0;
      float: left;

      .service {
        padding: 24px 15px;
        background: #fafafa;
        border: 1px solid #ebebeb;

        .sit {
          display: block;
          margin-bottom: 24px;
          height: 35px;
          line-height: 35px;
          font-size: 12px;
          text-align: center;
          color: #fff;
          cursor: pointer;
          background-color: rgba(255, 55, 29, 0.85);
          border-radius: 36px;
        }

        .service-note {
          margin-bottom: 18px;

          .service-name {
            font-size: 14px;
            margin-bottom: 10px;

            .icon {
              display: inline-block;
              width: 12px;
              height: 12px;
              background-repeat: no-repeat;
              -webkit-background-size: 12px 12px;
              background-size: 12px 12px
            }

          }

          .service-desc {
            //margin-top: 6px;
            font-size: 12px;
            color: #999;
            margin-bottom: 6px;
          }
        }
      }
      .box-like{
        margin-top: 24px;
        margin-bottom: 24px;
        font-size: 20px;
        color: #000;
        line-height: 28px;
      }
      .search__box{
        list-style: none;
        margin: 0;
        padding: 0;
        .search__item{
          width: 100%;
          height: 160px;
          margin-bottom: 30px;
          .link{
            width: 120px;
            height: 100%;
            display: inline-block;
            img{
              float: left;
              width: 120px;
              height: 100%;
            }
          }
          .search_item_info{
            width: 157px;
            float: right;
            height: 160px;
            .link__title{
              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              overflow: hidden;
              font-size: 14px;
              color: #4a4a4a;
              padding-left: 17px;
            }
            .search__item__info__venue{
              margin-top: 12px;
              color: #9b9b9b;
              padding-left: 20px;
              font-size: 12px;

            }
            .search__item__info__price{
              font-size: 16px;
              color: rgba(255, 55, 29, 0.85);
              margin-top: 39px;
              padding-left: 20px;
              font-weight: bold;
            }
          }
        }
      }
    }
  }


}

.active {
  border-color: rgba(255, 55, 29, 0.85);
  color: rgba(255, 55, 29, 0.85);
  background: #fff;
}

.activeCity {
  color: rgba(255, 55, 29, 0.85) !important;
  border: 1px solid rgba(255, 55, 29, 0.85) !important;
}

.ticket {
  color: rgba(255, 55, 29, 0.85) !important;
  border: 1px solid rgba(255, 55, 29, 0.85) !important;
}

.menuActive {
  //position: relative;
  font-size: 20px;
  color: #000;
  border-bottom: 2px solid rgba(255, 55, 29, 0.85);
}

.icon-no {
  display: inline-block;
  width: 12px;
  height: 12px;
  background-repeat: no-repeat;
  background-size: 12px 12px;
  background: url('/src/assets/section/no.png')
}

.icon-yes {
  display: inline-block;
  width: 12px;
  height: 12px;
  background-repeat: no-repeat;
  background-size: 12px 12px;
  background: url('/src/assets/section/yes.png')
}
</style>
