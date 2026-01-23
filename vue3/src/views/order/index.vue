<template>
  <div class="app-container">
    <div class="confirm-order">
      <div class="basic-info1">
        <div class="top">
          <span class="title">{{detailList.title}}</span>
          <span class="local">{{ detailList.areaName }}|{{ detailList.place }}</span>
          <div class="line"></div>
          <div class="time"><span>{{ formatDateWithWeekday(detailList.showTime, detailList.showWeekTime) }}</span></div>
          <div class="money"><span>￥<span v-if="allPrice == ''">{{countPrice}}</span><span v-else>{{allPrice}}</span>票档</span><span >×<span  v-if="allPrice == ''">1</span><span  v-else>{{num}}</span>张</span></div>
          <!-- 选座信息展示 -->
          <div class="seat-info" v-if="isChooseSeat && selectedSeatsData.length > 0">
            <span class="seat-label">已选座位：</span>
            <span class="seat-list">
              <span v-for="seat in selectedSeatsData" :key="seat.id" class="seat-item">
                {{ seat.rowCode }}排{{ seat.colCode }}座(￥{{ seat.price }})
              </span>
            </span>
          </div>
          <div class="order-info">
            <span>按付款顺序配票，优先连座配票</span>
          </div>

      </div>
        <div class="bottom">
          <div class="service-box">
            <span class="service">服务</span>
            <div class="service-name" v-if="detailList.permitRefund!=''">
              <i class="icon-warn" v-if="detailList.permitRefund=='0'"></i><span v-if="detailList.permitRefund=='0'">不支持退</span>
              <i class="icon-yes-blue" v-if="detailList.permitRefund=='1'"></i><span v-if="detailList.permitRefund=='1'">条件退</span>
              <i class="icon-yes-blue" v-if="detailList.permitRefund=='2'"></i><span v-if="detailList.permitRefund=='2'">全部退</span>
            </div>
          <div class="service-name" v-if="detailList.relNameTicketEntrance!=''">
            <i class="icon-warn" v-if="detailList.relNameTicketEntrance=='0'"></i><span
              v-if="detailList.relNameTicketEntrance=='0'">不实名购票和入场</span>
            <i class="icon-yes-blue" v-if="detailList.relNameTicketEntrance=='1'"></i><span
              v-if="detailList.relNameTicketEntrance=='1'">实名购票和入场</span>
          </div>
          <div class="service-name"   v-if="detailList.permitChooseSeat!=''">
            <i class="icon-warn" v-if="detailList.permitChooseSeat=='0'"></i><span
              v-if="detailList.permitChooseSeat=='0'">不支持选座</span>
            <i class="icon-yes-blue" v-if="detailList.permitChooseSeat=='1'"></i><span
              v-if="detailList.permitChooseSeat=='1'">支持选座</span>
          </div>
          <div class="service-name" v-if="detailList.electronicDeliveryTicket!=''">
            <i class="icon-warn" v-if="detailList.electronicDeliveryTicket=='0'"></i><span
              v-if="detailList.electronicDeliveryTicket=='0'">无票</span>
            <i class="icon-yes-blue" v-if="detailList.electronicDeliveryTicket=='1'"></i><span
              v-if="detailList.electronicDeliveryTicket=='1'">电子票</span>
            <i class="icon-yes-blue" v-if="detailList.electronicDeliveryTicket=='2'"></i><span
              v-if="detailList.electronicDeliveryTicket=='2'">快递票</span>
          </div>
          <div class="service-name"  v-if="detailList.electronicInvoice!=''">
            <i class="icon-warn" v-if="detailList.electronicInvoice=='0'"></i><span
              v-if="detailList.electronicInvoice=='0'">纸质发票</span>
            <i class="icon-yes-blue" v-if="detailList.electronicInvoice=='1'"></i><span
              v-if="detailList.electronicInvoice=='1'">电子发票</span>
          </div>
          </div>
          <div class="line"></div>
        </div>
        <div class="isRealName">
          <div class="left"><span class="title">实名观演人</span><span class="notice">仅需选择一位，入场时需携带对应证件</span></div>
          <div class="right"><el-button class="btn" type="primary" circle @click="buyTicketInfo">新增</el-button></div>
          <div class="ticketInfo" v-if="ticketInfoArr!=''">
              <div  class="ticket" v-for="item in ticketInfoArr">
              <div class="info" v-if="isSHowInfo">
                <span class="title">{{item.relName}}</span>
               <div class="card">
                 <span class="cardType" v-if="item.idType == 1">身份证</span>
                 <span class="cardType" v-if="item.idType == 2">港澳台居民居住证</span>
                 <span class="cardType" v-if="item.idType == 3">港澳居民来往内地通行证</span>
                 <span class="cardType" v-if="item.idType == 4">台湾居民来往内地通行证</span>
                 <span class="cardType" v-if="item.idType == 5">护照</span>
                 <span class="cardType" v-if="item.idType == 6">外国人永久居住证</span>
                 <span class="cardId"> {{item.idNumber}}</span>
               </div>
              </div>
                <div class="chx"> <el-checkbox class="checkSelect" :value="item.id" size="large" @change="getSelectTicketUser(item.id, $event)"></el-checkbox></div>
              </div>
          </div>
        </div>
        <div class="line"></div>
        <div class="sendMethod">
          <div  class="sendMethodTitle">配送方式</div>
          <div class="ticketType"  v-if="detailList.electronicDeliveryTicket=='1'">电子票 <el-button   class="ticketbtn"  v-if="detailList.electronicDeliveryTicket=='1'">直接入场</el-button></div>
          <div class="ticketInfo"  v-if="detailList.electronicDeliveryTicket=='1'">支付成功后，无需取票，前往票夹查看入场凭证</div>
<!--          <div class="ticketType"  v-if="detailList.electronicDeliveryTicket=='2'">快递</div>-->
<!--          <div class="ticketInfo"  v-if="detailList.electronicDeliveryTicket=='2'"></div>-->
<!--          <div class="ticketType"  v-if="detailList.electronicDeliveryTicket=='2'">运费</div>-->
<!--          <div class="ten"  v-if="detailList.electronicDeliveryTicket=='2'">￥10.00</div>-->
        </div>
        <div class="sendline"></div>
        <div class="tel">
          <div class="title">联系方式</div>
          <div class="telNum">{{telNum}}</div>
        </div>
        <div class="sendline"></div>
        <div class="payMethod">
          <div class="title">支付方式</div>
          <div class="payMoney"><img :src="pay" alt=""><span>支付宝</span> <el-radio class="radioPay" value="1" size="large"></el-radio></div>
        </div>
        <div class="info">
          <div class="descript">由于票品为价票券，非普通商品，其背后承载的文化服务具有时效性、稀缺性等特征，一旦订购成功，不支持退换。</div>
        <div class="price">
          <span class="num" v-if="allPrice == ''">￥{{ countPrice }}</span>
          <span class="num" v-else>￥{{ allPrice }}</span>
          <span class="detail">明细</span>
<!--          <el-button type="primary" class="dialogShow"-->
<!--                     @click="dialogShow">点击显示弹框</el-button>-->
<!--          <el-button type="primary" class="dialogShow"-->
<!--                     v-loading.fullscreen.lock="loading"-->
<!--                     element-loading-text="请稍后..."-->
<!--                     :element-loading-spinner="svg"-->
<!--                     element-loading-svg-view-box="-10, -10, 50, 50"-->
<!--                     element-loading-background="rgba(122, 122, 122, 0.8)"-->
<!--                     @click="dialogLoading">点击loading</el-button>-->
          <el-button type="primary" class="submit" @click="submitOrder">提交订单</el-button>
        </div>
        </div>
      </div>
    </div>
    <el-dialog
        v-model="dialogVisible"
       style="width: 450px;height:500px;background: #FFE7BA;"

    >
      <div class="content">当前排队人数太多，请稍候再试~</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" class="btn1">返回</el-button>
          <el-button   class="submit btn2"    @click="dialogVisible = false">
            继续尝试
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="orderIndex">
import {ref, nextTick, onActivated, onMounted,onBeforeUnmount } from 'vue'
import pay from "@/assets/section/pay.png"
import {getCurrentDateTime,formatDateWithWeekday,useMitt} from '@/utils/index'
import {useRoute, useRouter} from 'vue-router'
import { getUserIdKey} from "@/utils/auth";
import { getPersonInfoId} from '@/api/personInfo'
import {getTicketUser} from "@/api/buyTicketUser";
import {getOrderCacheApi, orderCreateV1Api, orderCreateV2Api, orderCreateV3Api, orderCreateV4Api} from '@/api/order.js'
import {ElMessage} from "element-plus";
//获取用户信息
import useUserStore from "../../store/modules/user";

const useUser = useUserStore()
const router = useRouter();
const detailList = ref([])
const allPrice = ref('')
const countPrice = ref('')
const num = ref('')
const telNum = ref('')
const ticketInfoArr = ref([])
const dialogVisible = ref(false)
const isSHowInfo = ref(true)//此处设置是为了解决弹出框显示后此处界面也会显示到上层的问题。注意：关闭弹框的时候这里要设置为true
const ticketUserIdArr = ref([])
//票档id
const ticketCategoryId = ref('')
const orderNumberCache = ref('')
const loading = ref(false)
// 选座相关数据
const seatIdList = ref([])
const isChooseSeat = ref(false)
const selectedSeatsData = ref([])
const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>`
const pollingTimer = ref(null);
const timeoutTimer = ref(null);
// 10s的时间（毫秒）
const tenSecond = 10000;

//跳转后的接收值
onMounted(()=>{
  detailList.value  = JSON.parse(history.state.detailList)
  allPrice.value  = history.state.allPrice
  countPrice.value  =history.state.countPrice
  num.value  =history.state.num
  ticketCategoryId.value = history.state.ticketCategoryId
  // 接收选座数据
  if (history.state.isChooseSeat) {
    isChooseSeat.value = true
    seatIdList.value = JSON.parse(history.state.seatIdList || '[]')
    selectedSeatsData.value = JSON.parse(history.state.selectedSeats || '[]')
  }
})

getPersonInfoIdList()
getTicketUserList()

async function getPersonInfoIdList() {
  const id = getUserIdKey()
  getPersonInfoId({id: id}).then(response => {
    let {mobile } = response.data
    telNum.value = mobile
  })
}
async function getTicketUserList() {
  const id = getUserIdKey()
  getTicketUser({userId:id}).then(response=>{
    ticketInfoArr.value =response.data
  })
}


function buyTicketInfo(){
  router.replace({path:'/order/buyTicketUser'})

}

function getSelectTicketUser(ticketUserId,isChecked){
  if (isChecked) {
    ticketUserIdArr.value.push(ticketUserId);
  } else {
    ticketUserIdArr.value = ticketUserIdArr.value.filter((item) => item !== ticketUserId);
  }
}


function getOrderCache(orderNumber){
  const orderNumberParams = {orderNumber}
  getOrderCacheApi(orderNumberParams).then(response => {
    if (response.code == '0' && response.data != null){
      orderNumberCache.value = response.data;
    }
  })
}

//订单查询轮训
const startPolling = (orderNumber,startTime) => {
  pollingTimer.value = setInterval(() => {
    const currentTime = Date.now();
    if (currentTime - startTime >= tenSecond) {
      stopPolling();
      //1. 大于10秒，此订单被舍弃，显示排队弹框
      //2. loading弹出框关闭
      loadingClose();
      //3. 排队弹框显示
      dialogShow();
      return;
    }
    getOrderCache(orderNumber);
    if (orderNumberCache.value !== null && orderNumberCache.value !== '') {
      stopPolling();
      //执行到这里说明订单创建成功
      //loading弹框关闭
      loadingClose();
      router.replace({path:'/order/payMethod',state:{'orderNumber':orderNumberCache.value}})
    }
  }, 200); // 每200毫秒调用一次
};
//停止轮训
const stopPolling = () => {
  clearInterval(pollingTimer.value);
  pollingTimer.value = null;
  clearTimeout(timeoutTimer.value);
  timeoutTimer.value = null;
};

/**
 * 提交订单
 * */
function submitOrder(){

  if (ticketUserIdArr.value.length != num.value) {
    ElMessage({
      message:'选择的购票人和票张数量不一致',
      type: 'error',
    })
    return;
  }

  // 根据是否手动选座构建不同的请求参数
  let orderCreateParams = {
    'programId': detailList.value.id,
    'userId': useUser.userId,
    'ticketUserIdList': ticketUserIdArr.value
  }

  if (isChooseSeat.value && selectedSeatsData.value.length > 0) {
    // 手动选座：传 seatDtoList
    orderCreateParams.seatDtoList = selectedSeatsData.value.map(seat => ({
      id: seat.id,
      ticketCategoryId: seat.ticketCategoryId,
      rowCode: parseInt(seat.rowCode),
      colCode: parseInt(seat.colCode),
      price: seat.price
    }))
  } else {
    // 自动选座：传 ticketCategoryId 和 ticketCount
    orderCreateParams.ticketCategoryId = ticketCategoryId.value
    orderCreateParams.ticketCount = num.value
  }

  const createOrderVersion = import.meta.env.VITE_CREATE_ORDER_VERSION
  if (createOrderVersion == 1) {
    //v1版本的创建订单

    //loading弹出框显示
    loadingShow();
    
    orderCreateV1Api(orderCreateParams).then(response => {
      //loading弹出框关闭
      loadingClose();
      if (response.code == '0') {
        const orderNumber = response.data;
        router.replace({path:'/order/payMethod',state:{'orderNumber':orderNumber}})
      }else{
        // ElMessage({
        //   message:response.message,
        //   type: 'error',
        // })
        //排队弹框显示
        dialogShow();
      }
    })
  }else if (createOrderVersion == 2) {
    //v2版本的创建订单

    //loading弹出框显示
    loadingShow();
    
    orderCreateV2Api(orderCreateParams).then(response => {
      //loading弹出框关闭
      loadingClose();
      if (response.code == '0') {
        const orderNumber = response.data;
        router.replace({path:'/order/payMethod',state:{'orderNumber':orderNumber}})
      }else{
        // ElMessage({
        //   message:response.message,
        //   type: 'error',
        // })
        //排队弹框显示
        dialogShow();
      }
    })
  }else if (createOrderVersion == 3) {
    //v3版本的创建订单

    //loading弹出框显示
    loadingShow();
    
    orderCreateV3Api(orderCreateParams).then(response => {
      //loading弹出框关闭
      loadingClose();
      if (response.code == '0') {
        const orderNumber = response.data;
        router.replace({path:'/order/payMethod',state:{'orderNumber':orderNumber}})
      }else{
        // ElMessage({
        //   message:response.message,
        //   type: 'error',
        // })
        //排队弹框显示
        dialogShow();
      }
    })
  }else if (createOrderVersion == 4) {
    //v4版本的创建订单

    //loading弹出框显示
    loadingShow();
    
    orderCreateV4Api(orderCreateParams).then(response => {
      if (response.code == '0' && response.data != null) {
        console.log('异步订单创建成功 订单编号',response.data)
        //开始定时轮训查询
        startPolling(response.data,Date.now());
        // 设置一个10s后停止轮询的定时器
        timeoutTimer.value = setTimeout(() => {
          if (pollingTimer.value) {
            stopPolling();
          }
        }, tenSecond);
      }else{
        dialogShow();
      }
    })
  }
}
//弹出排队框
function dialogShow(){
  dialogVisible.value = true
  isSHowInfo.value=false
}

function dialogLoading(){
  loading.value = true
  isSHowInfo.value=false
  setTimeout(() => {
    loading.value = false
    isSHowInfo.value=true
  }, 2000)
}

function loadingShow(){
  loading.value = true
  isSHowInfo.value=false
}

function loadingClose(){
  loading.value = false;
  isSHowInfo.value=true;
}

onBeforeUnmount(() => {
  stopPolling();
});
</script>

<style scoped lang="scss">
.app-container {
  width: 100%;
  height: 100%;
  background: #ffffff;

  .confirm-order {
    position: relative;
    box-sizing: border-box;
    display: flex;
    -webkit-box-orient: vertical;
    flex-direction: column;
    align-content: flex-start;
    flex-shrink: 0;

    .basic-info1 {
      position: relative;
      //display: flex;
      overflow: hidden;
      width: 100%;
      height: auto;

      .top {
        position: absolute;
        display: flex;
        overflow: hidden;
        -webkit-box-orient: vertical;
        flex-direction: column;
        width: 100%;
        padding-top: 31px;
        height: 318px;
        background: rgba(255, 55, 29, 0.85);

        .title {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          margin-right: 43px;
          font-size: 37px;
          margin-left: 43px;
          width: 100%;
          max-width: 1800px;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          color: rgb(255, 255, 255);
          font-weight: bold;
          height: auto;
        }

        .local {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          margin-right: 43px;
          font-size: 24px;
          margin-left: 43px;
          width: fit-content;
          overflow: hidden;
          color: rgb(255, 255, 255);
          margin-top: 12px;
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          max-width: 1800px;
        }

        .line {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          margin-right: 43px;
          background-color: rgba(255, 55, 29, 0.85);
          place-self: center flex-end;
          margin-left: 43px;
          width: 100%;
          max-width: 1800px;
          margin-top: 24px;
          height: 2px;
        }

        .time {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          margin-right: 43px;
          font-size: 33px;
          margin-left: 43px;
          width: fit-content;
          overflow: hidden;
          color: rgb(255, 255, 255);
          margin-top: 24px;
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          max-width: 1800px;
          flex-shrink: 0;
          flex-grow: 0;
          height: fit-content;
          span {
            white-space: pre-wrap;
            line-height: 40px;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }

        .money {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          overflow: hidden;
          width: 100%;

          -webkit-box-orient: horizontal;
          flex-direction: row;
          margin-left: 43px;
          flex-shrink: 0;
          flex-grow: 0;
          height: fit-content;

          span{
            position: relative;
            display: flex;
            flex-shrink: 0;
            flex-grow: 0;
            font-size: 29px;
            width: fit-content;
            color: rgb(255, 255, 255);
            height: auto;
            -webkit-box-pack: start;
            justify-content: flex-start;
            -webkit-box-align: center;
            align-items: center;
            overflow: hidden;
            max-width: none;
          }
        }

        .order-info {
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          margin-right: 43px;
          font-size: 24px;
          visibility: visible;
          margin-left: 43px;
          width: 100%;
          max-width: 1800px;
          color: rgb(255, 255, 255);
          margin-top: 6px;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          flex-shrink: 0;
          flex-grow: 0;
          height: fit-content;
        }

        .seat-info {
          position: relative;
          display: flex;
          flex-wrap: wrap;
          margin-left: 43px;
          margin-top: 10px;
          font-size: 20px;
          color: rgb(255, 255, 255);
          
          .seat-label {
            margin-right: 10px;
          }
          
          .seat-list {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            
            .seat-item {
              background: rgba(255, 255, 255, 0.2);
              padding: 4px 10px;
              border-radius: 4px;
              font-size: 18px;
            }
          }
        }

      }
      .bottom{
        width: 100%;
        height: auto;
        margin-top: 330px;

        .service-box{
          width: 100%;
          height: 33px;
          line-height: 33px;
          display: flex;
          flex-direction: row;
          margin-top: 30px;
          color: #000 !important;
          font-size: 24px;
          .service{
            width: 50px;
            height: 33px;
            line-height: 33px;
            margin-left: 50px;
          }
          .service-name{
            margin-left: 18px;
            width: fit-content;
            height: 33px;
            line-height: 33px;
            display: inline-block;
            .icon-warn{
              display: inline-block;
              width: 12px;
              height: 12px;
              background-repeat: no-repeat;
              background-size: 12px 12px;
              background: url('/src/assets/section/warn.png');
              margin-right: 10px;
            }
            .icon-yes-blue{
              display: inline-block;
              width: 12px;
              height: 12px;
              background-repeat: no-repeat;
              background-size: 12px 12px;
              background: url('/src/assets/section/yes-blue.png');
              margin-right: 10px;
            }
            span{
              width: fit-content;
              height: 33px;
              line-height: 33px;
            }
          }

        }
        .line{
          margin: 20px 0px 20px 50px;
          width: 97%;
          height: 2px;
          background-color: #cccccc;
          opacity: 0.7;
        }
      }
      .isRealName{
        margin-bottom: 20px;
        .left{
          position: relative;
          display: flex;
          flex: 1 1 0%;
          overflow: hidden;
          -webkit-box-orient: vertical;
          flex-direction: column;
          place-self: center flex-start;
          margin-left: 43px;
          width: fit-content;
          -webkit-box-flex: 1;
          height: auto;
          float: left;
          .title{
            position: relative;
            display: flex;
            flex-shrink: 0;
            flex-grow: 0;
            font-size: 24px;
            place-self: flex-start center;
            width: fit-content;
            height: auto;
            -webkit-box-pack: start;
            justify-content: flex-start;
            -webkit-box-align: center;
            align-items: center;
            overflow: hidden;
            max-width: none;
          }
          .notice{
            position: relative;
            display: flex;
            flex: 1 1 0%;
            font-size: 24px;
            place-self: flex-start center;
            width: fit-content;
            -webkit-box-flex: 1;
            color: rgba(255, 55, 29, 0.85);
            margin-top: 6px;
            height: auto;
            -webkit-box-pack: start;
            justify-content: flex-start;
            -webkit-box-align: center;
            align-items: center;
            overflow: hidden;
            max-width: none;
          }
        }
        .right{
          float: left;
          margin-left: 43px;
          .btn{
            position: relative;
            display: flex;
            flex-shrink: 1;
            flex-grow: 0;
            overflow: hidden;
            margin-right: 43px;
            background-color: rgba(255, 55, 29, 0.85);
            place-self: center flex-end;
            box-shadow: rgba(255, 55, 29, 0.85) 0px 0px 0px 1px inset;
            width: 110px;
            height: 55px;
            border-radius: 28px;
            border: none;
            font-size: 24px;
          }
        }
        .ticketInfo{
          width: 100%;
          padding-left: 143.36px;
          padding-right: 143.36px;
          height: auto;
          min-height: 10.67vmin;
          //display: flex;
          -webkit-box-orient: horizontal;
          flex-direction: row;
          -webkit-box-pack: justify;
          justify-content: space-between;
          -webkit-box-align: center;
          align-items: center;
          .ticket{
            width: 100%;
            height: 136px;
            display: flex;
            flex-direction: row;
            align-items: center;
            .info{
              position: relative;
              display: flex;
              flex: 1 1 0%;
              overflow: hidden;
              -webkit-box-orient: vertical;
              flex-direction: column;
              place-self: center flex-start;
              margin-left: 43px;
              width: fit-content;
              -webkit-box-flex: 1;
              height: auto;
              float: left;
              .title{
                position: relative;
                display: flex;
                flex-shrink: 0;
                flex-grow: 0;
                place-self: flex-start center;
                width: fit-content;
                height: auto;
                -webkit-box-pack: start;
                justify-content: flex-start;
                -webkit-box-align: center;
                align-items: center;
                font-size: 4.27vmin;
                color: rgb(0, 0, 0);
                max-width: 60vmin;
                margin-right: 1.2vmin;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              .card{
                font-size: 3.2vmin;
                color: rgb(156, 156, 165);
                width: auto;
                overflow: hidden;
                white-space: nowrap;
                margin-right: 2.4vmin;
                //position: relative;
                //display: flex;
                //flex-direction: row;
                //flex: 1 1 0%;
                //place-self: flex-start center;
                //width: fit-content;
                //-webkit-box-flex: 1;
                //margin-top: 6px;
                //height: auto;
                //-webkit-box-pack: start;
                //justify-content: flex-start;
                //-webkit-box-align: center;
                //align-items: center;
                //overflow: hidden;
                //max-width: none;
                //font-size: 3.2vmin;
                //color: rgb(156, 156, 165);
                .cardType{
                  width: 100px;
                  font-size: 24px;
                  color:rgb(156, 156, 165);
                  display: inline-block;
                }
                .cardId{

                }
              }

            }

            .chx{}
          }

        }

      }
      .line {
        margin: 114px 0px 20px 50px;
        width: 97%;
        height: 1px;
        background-color: #cccccc;
        opacity: 0.7;
      }
      .sendMethod{
        margin-left: 43px;
        .sendMethodTitle{
          position: relative;
          display: flex;
          flex: 1 1 0%;
          margin-right: 10px;
          font-size: 24px;
          place-self: center flex-start;
          width: fit-content;
          -webkit-box-flex: 1;
          color: rgb(0, 0, 0);
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          max-width: none;
        }
        .ticketType{
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          font-size: 33px;
          place-self: center flex-start;
          width: fit-content;
          color: rgb(0, 0, 0);
          height: 45px;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          max-width: none;
          margin-top: 20px;
          margin-bottom: 10px;
          .ticketbtn {
            position: relative;
            display: flex;
            flex-shrink: 0;
            flex-grow: 0;
            font-size: 20px;
            place-self: center;
            width: fit-content;
            -webkit-box-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            align-items: center;
            color: rgb(255, 146, 0);
            height: auto;
            overflow: hidden;
            max-width: none;
            border: 1px solid rgb(255, 146, 0);
            border-radius: 20px;
          }
        }
        .ticketInfo{
          position: relative;
          display: flex;
          flex-shrink: 0;
          flex-grow: 0;
          font-size: 24px;
          width: 100%;
          overflow: hidden;
          color: rgb(156, 156, 165);
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          max-width: none;
        }
      }
      .sendline{
        margin: 20px 0px 20px 50px;
        width: 97%;
        height: 1px;
        background-color: #cccccc;
        opacity: 0.7;
      }
      .tel{
        margin-left: 43px;
        .title{
          position: relative;
          display: flex;
          flex: 1 1 0%;
          font-size: 24px;
          place-self: center flex-start;
          width: fit-content;
          -webkit-box-flex: 1;
          color: rgb(0, 0, 0);
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          max-width: none;
         margin: 20px 0;
        }
        .telNum{
          width: 100%;
          height: 100%;
          outline: none;
          border: none;
          padding: 0px;
          margin: 0px;
          user-select: auto;
          font-size: 33px;
          color: rgb(0, 0, 0);
          text-align: left;
        }
      }
      .payMethod{
        margin-left: 43px;
        .title{
          position: relative;
          display: flex;
          flex: 1 1 0%;
          font-size: 24px;
          place-self: center flex-start;
          width: fit-content;
          -webkit-box-flex: 1;
          color: rgb(0, 0, 0);
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          overflow: hidden;
          max-width: none;
          margin: 20px 0;
        }
        .payMoney{
          display: flex;
          height: 300px;
          img{
            width: 80px;
            height: 80px;
          }
          span{
            padding: 30px 15px;
            font-size: 3.4vmin;
            color: #000000;
            letter-spacing: 0;
            line-height: 15px;
            margin-right: 1500px;
          }
          .radioPay{

          }
        }
      }
      .info{
        width: 100%;
        height: 150px;
        position: fixed;
        bottom: 0px;
        background: #ffffff;
        z-index: 100000;
        .descript{
          position: relative;
          display: flex;
          font-size: 22px;
          visibility: visible;
          width: fit-content;
          overflow: hidden;
          color: rgb(156, 156, 165);
          margin-top: 4px;
          height: auto;
          -webkit-box-pack: start;
          justify-content: flex-start;
          -webkit-box-align: center;
          align-items: center;
          max-width: none;
          margin-left: 43px;
        }
        .price{
          display: flex;
          flex-direction: row;
          margin: 20px 0px 20px 43px;
          .num{
            position: relative;
            display: flex;
            flex-shrink: 0;
            flex-grow: 0;
            margin-right: 6px;
            font-size: 41px;
            place-self: center flex-start;
            width: fit-content;
            color: rgba(255, 55, 29, 0.85);
            height: auto;
            -webkit-box-pack: start;
            justify-content: flex-start;
            -webkit-box-align: center;
            align-items: center;
            overflow: hidden;
            max-width: none;
          }
          .detail{
            position: relative;
            display: flex;
            flex-shrink: 0;
            flex-grow: 0;
            font-size: 24px;
            place-self: center flex-start;
            width: fit-content;
            overflow: hidden;
            color: rgb(0, 0, 0);
            height: auto;
            -webkit-box-pack: start;
            justify-content: flex-start;
            -webkit-box-align: center;
            align-items: center;
            max-width: none;
          }
          .submit{
            position: absolute;
            right: 30px;
            display: flex;
            font-size: 33px;
            width: 266px;
            -webkit-box-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            align-items: center;
            color: rgb(255, 255, 255);
            height: 90px;
            overflow: hidden;
            max-width: none;
            border-radius: 20px;
            background: rgba(255, 55, 29, 0.85);
            border: none;

          }
        }
      }
    }
  }
  .content{
    width: 100%;
    height:30px;
    line-height: 30px;
    text-align: center;
    font-size: 24px;
    margin-top: 100px;
  }
  .btn1{
    width: 300px;
    height: 50px;
    background: rgb(255, 55, 29);
    color: #FFFFFF;
    display: block;
    margin: 0 auto;
    border-radius: 50px;
    font-size: 20px;
  }
  .btn2{
    width: 300px;
   border: none;
    display: block;
    margin: 20px auto;
    background: transparent;
    font-size: 20px;
  }
}
:deep(.el-dialog){

  border-radius: 20px;
}
:deep(.el-dialog__footer){
  padding-top: 100px ;
}
:deep(.el-radio__input.is-checked .el-radio__inner) {
  border-color: rgba(255, 55, 29, 0.85);
  background: rgba(255, 55, 29, 0.85);
}
:deep(.el-checkbox.el-checkbox--large .el-checkbox__inner) {
  width: 4.3vmin;
  height: 4.3vmin;
  color: #dddddd;
}
:deep(.el-checkbox__input.is-checked .el-checkbox__inner ){
  background-color: rgba(255, 55, 29, 0.85);
  border-color: rgba(255, 55, 29, 0.85);
  font-size:4.3vmin ;
}
:deep(.el-checkbox__inner::after){
  box-sizing: content-box;
  content: "";
  border: 1px solid var(--el-checkbox-checked-icon-color);
  border-left: 0;
  border-top: 0;
  height: 30px;
  left: 9px;
  position: absolute;
  top: -3px;
  transform: rotate(45deg) scaleY(0);
  width: 19px;
  transition: transform .15s ease-in 50ms;
  transform-origin: center;
}

</style>
