<template>
  <div class="seat-select-container">
    <Header></Header>
    <div class="main-content">
      <!-- 顶部时间和票价区域 -->
      <div class="top-bar">
        <div class="date-time">{{ seatData.showTime }} {{ seatData.showWeekTime }}</div>
        <div class="price-tags">
          <div 
            v-for="price in seatData.priceList" 
            :key="price" 
            class="price-tag"
            :class="{ active: selectedPrice === price }"
            @click="filterByPrice(price)"
          >
            <span class="color-dot" :style="{ backgroundColor: getPriceColor(price) }"></span>
            <span class="price-text" :class="{ 'active-price': selectedPrice === price }">{{ selectedPrice === price ? price + '元' : '¥' + price }}</span>
          </div>
          <div 
            class="price-tag"
            :class="{ active: selectedPrice === '' }"
            @click="filterByPrice('')"
          >
            <span class="price-text">全部</span>
          </div>
        </div>
      </div>

      <!-- 座位图区域 -->
      <div class="seat-area">
        <div class="venue-wrapper">
          <!-- 舞台提示 -->
          <div class="stage-box">
            <span>舞台</span>
          </div>
          <!-- 场馆轮廓 - 梯形效果 -->
          <div class="venue-outline">
            <div class="seat-map-container">
            <div 
              v-for="(row, rowIndex) in allRows" 
              :key="row.rowCode" 
              class="seat-row"
              :style="getRowStyle(rowIndex)"
            >
              <span class="row-label">{{ row.rowCode }}排</span>
              <div class="seats">
                <!-- 左区座位 -->
                <div class="seat-section left-section">
                  <div 
                    v-for="seat in getLeftSeats(row.seats)" 
                    :key="seat.id"
                    class="seat"
                    :class="getSeatClass(seat)"
                    @click="toggleSeat(seat)"
                    :title="`${seat.rowCode}排${seat.colCode}座 ¥${seat.price}`"
                  >
                    <span 
                      class="seat-dot"
                      :style="{ backgroundColor: getSeatColor(seat), opacity: getSeatOpacity(seat) }"
                    ></span>
                  </div>
                </div>
                <!-- 左过道 -->
                <div class="aisle"></div>
                <!-- 中区座位 -->
                <div class="seat-section center-section">
                  <div 
                    v-for="seat in getCenterSeats(row.seats)" 
                    :key="seat.id"
                    class="seat"
                    :class="getSeatClass(seat)"
                    @click="toggleSeat(seat)"
                    :title="`${seat.rowCode}排${seat.colCode}座 ¥${seat.price}`"
                  >
                    <span 
                      class="seat-dot"
                      :style="{ backgroundColor: getSeatColor(seat), opacity: getSeatOpacity(seat) }"
                    ></span>
                  </div>
                </div>
                <!-- 右过道 -->
                <div class="aisle"></div>
                <!-- 右区座位 -->
                <div class="seat-section right-section">
                  <div 
                    v-for="seat in getRightSeats(row.seats)" 
                    :key="seat.id"
                    class="seat"
                    :class="getSeatClass(seat)"
                    @click="toggleSeat(seat)"
                    :title="`${seat.rowCode}排${seat.colCode}座 ¥${seat.price}`"
                  >
                    <span 
                      class="seat-dot"
                      :style="{ backgroundColor: getSeatColor(seat), opacity: getSeatOpacity(seat) }"
                    ></span>
                  </div>
                </div>
              </div>
              <span class="row-label">{{ row.rowCode }}排</span>
            </div>
          </div>
          </div>
        </div>
      </div>

      <!-- 底部栏 -->
      <div class="bottom-bar">
        <div class="left-section">
          <div class="price-display">
            <span class="currency">¥</span>
            <span class="amount">{{ totalPrice }}</span>
          </div>
          <div class="selected-seats" v-if="selectedSeats.length > 0">
            <span 
              v-for="seat in selectedSeats" 
              :key="seat.id" 
              class="seat-tag"
              @click="removeSeat(seat)"
            >
              {{ seat.rowCode }}排{{ seat.colCode }}座
              <i class="remove-icon">×</i>
            </span>
          </div>
        </div>
        <button 
          class="buy-btn"
          :class="{ disabled: selectedSeats.length === 0 }"
          :disabled="selectedSeats.length === 0"
          @click="submitOrder"
        >
          立即购买
        </button>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script setup name="seatSelect">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSeatList } from '@/api/seatDetail'
import Header from '@/components/header/index'
import Footer from '@/components/footer/index'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 从路由state获取节目详情
const detailList = ref({})
const seatData = ref({
  programId: '',
  place: '',
  showTime: '',
  showWeekTime: '',
  priceList: [],
  seatVoMap: {}
})

// 选中的座位
const selectedSeats = ref([])
// 价格筛选
const selectedPrice = ref('')
// 最大可选座位数
const maxSeats = 6

// 价格颜色映射 - 大麦风格颜色
const priceColors = {
  0: '#8CD790',  // 淡绿色
  1: '#FFD966',  // 淡黄色
  2: '#F4A460',  // 淡橙色
  3: '#FF9EAA',  // 淡粉色
  4: '#87CEEB',  // 天蓝色
  5: '#DDA0DD',  // 梅红色
  6: '#B8D4E3',  // 淡蓝色
  7: '#F0E68C',  // 卡其色
  8: '#98D8C8',  // 薄荷绿
  9: '#F7CAC9'   // 玫瑰粉
}

// 根据价格获取颜色
const getPriceColor = (price) => {
  const index = seatData.value.priceList.indexOf(price.toString())
  return priceColors[index % 10] || '#ccc'
}

// 筛选后的座位图
const filteredSeatMap = computed(() => {
  if (selectedPrice.value === '') {
    return seatData.value.seatVoMap
  }
  const filtered = {}
  if (seatData.value.seatVoMap[selectedPrice.value]) {
    filtered[selectedPrice.value] = seatData.value.seatVoMap[selectedPrice.value]
  }
  return filtered
})

// 获取所有行的座位数据（并根据行号计算梯形显示）
const allRows = computed(() => {
  const rowMap = {}
  // 始终显示所有票档的座位
  Object.values(seatData.value.seatVoMap).forEach(seats => {
    seats.forEach(seat => {
      if (!rowMap[seat.rowCode]) {
        rowMap[seat.rowCode] = {
          rowCode: seat.rowCode,
          seats: []
        }
      }
      rowMap[seat.rowCode].seats.push(seat)
    })
  })
  
  // 按列排序
  Object.values(rowMap).forEach(row => {
    row.seats.sort((a, b) => Number(a.colCode) - Number(b.colCode))
  })
  
  // 按行号排序返回
  const sortedRows = Object.values(rowMap).sort((a, b) => Number(a.rowCode) - Number(b.rowCode))
  
  // 为每行计算梯形显示的座位
  const totalRows = sortedRows.length
  return sortedRows.map((row, rowIndex) => {
    // 前排显示比例小，后排显示比例大，形成梯形
    // 第一排显示60%，最后一排显示100%
    const minRatio = 0.55  // 前排最小显示比例
    const maxRatio = 1.0   // 后排最大显示比例
    const ratio = minRatio + (rowIndex / Math.max(totalRows - 1, 1)) * (maxRatio - minRatio)
    
    const totalSeats = row.seats.length
    const displayCount = Math.ceil(totalSeats * ratio)
    // 从中间截取座位，保持居中显示
    const skipCount = Math.floor((totalSeats - displayCount) / 2)
    
    return {
      rowCode: row.rowCode,
      seats: row.seats.slice(skipCount, skipCount + displayCount),
      allSeats: row.seats,  // 保留全部座位用于其他计算
      rowIndex: rowIndex
    }
  })
})

// 计算总价
const totalPrice = computed(() => {
  return selectedSeats.value.reduce((sum, seat) => sum + Number(seat.price), 0)
})

// 获取座位颜色
const getSeatColor = (seat) => {
  const isSelected = selectedSeats.value.some(s => s.id === seat.id)
  
  // 选中状态 - 粉红色
  if (isSelected) {
    return '#FF375D'
  }
  
  // 已售 - 浅灰色
  if (seat.sellStatus !== '1') {
    return '#E8E8E8'
  }
  
  // 未售 - 根据价格显示颜色
  return getPriceColor(seat.price)
}

// 获取座位透明度
const getSeatOpacity = (seat) => {
  // 已选中的座位始终不透明
  const isSelected = selectedSeats.value.some(s => s.id === seat.id)
  if (isSelected) {
    return 1
  }
  
  // 已售的座位始终不透明
  if (seat.sellStatus !== '1') {
    return 1
  }
  
  // 没有选择票档，所有未售座位都半透明
  if (selectedPrice.value === '') {
    return 0.4
  }
  
  // 选中票档的座位100%不透明，其他票档半透明
  return seat.price.toString() === selectedPrice.value ? 1 : 0.3
}

// 按行分组
const groupByRow = (seats) => {
  const rowMap = {}
  seats.forEach(seat => {
    if (!rowMap[seat.rowCode]) {
      rowMap[seat.rowCode] = {
        rowCode: seat.rowCode,
        seats: []
      }
    }
    rowMap[seat.rowCode].seats.push(seat)
  })
  // 按列排序
  Object.values(rowMap).forEach(row => {
    row.seats.sort((a, b) => Number(a.colCode) - Number(b.colCode))
  })
  // 按行号排序返回
  return Object.values(rowMap).sort((a, b) => Number(a.rowCode) - Number(b.rowCode))
}

// 获取座位样式类
const getSeatClass = (seat) => {
  const isSelected = selectedSeats.value.some(s => s.id === seat.id)
  return {
    'seat-available': seat.sellStatus === '1',
    'seat-sold': seat.sellStatus !== '1',
    'seat-selected': isSelected
  }
}

// 获取行样式 - 梯形效果已通过座位数量实现
const getRowStyle = (rowIndex) => {
  return {}
}

// 将座位分成三个区域：左、中、右
const getLeftSeats = (seats) => {
  const total = seats.length
  if (total < 6) return []  // 座位太少就不分区
  const leftCount = Math.max(Math.floor(total * 0.18), 2)  // 左区约18%
  return seats.slice(0, leftCount)
}

const getCenterSeats = (seats) => {
  const total = seats.length
  if (total < 6) return seats  // 座位太少就全部放中间
  const leftCount = Math.max(Math.floor(total * 0.18), 2)
  const rightCount = Math.max(Math.floor(total * 0.18), 2)
  return seats.slice(leftCount, total - rightCount)
}

const getRightSeats = (seats) => {
  const total = seats.length
  if (total < 6) return []  // 座位太少就不分区
  const rightCount = Math.max(Math.floor(total * 0.18), 2)  // 右区约18%
  return seats.slice(total - rightCount)
}

// 切换座位选中状态
const toggleSeat = (seat) => {
  // 已售出的座位不可选
  if (seat.sellStatus !== '1') {
    ElMessage.warning('该座位已售出')
    return
  }
  
  const index = selectedSeats.value.findIndex(s => s.id === seat.id)
  if (index > -1) {
    // 取消选中
    selectedSeats.value.splice(index, 1)
  } else {
    // 检查是否超过最大数量
    if (selectedSeats.value.length >= maxSeats) {
      ElMessage.warning(`每笔订单最多选择${maxSeats}个座位`)
      return
    }
    // 添加选中
    selectedSeats.value.push(seat)
  }
}

// 移除选中的座位
const removeSeat = (seat) => {
  const index = selectedSeats.value.findIndex(s => s.id === seat.id)
  if (index > -1) {
    selectedSeats.value.splice(index, 1)
  }
}

// 按价格筛选
const filterByPrice = (price) => {
  selectedPrice.value = price
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 提交订单
const submitOrder = () => {
  if (selectedSeats.value.length === 0) {
    ElMessage.warning('请先选择座位')
    return
  }
  
  // 获取选中座位的ID列表
  const seatIdList = selectedSeats.value.map(seat => seat.id)
  // 获取票档ID（取第一个选中座位的票档ID）
  const ticketCategoryId = selectedSeats.value[0].ticketCategoryId
  
  // 跳转到订单页面，传递选座信息
  router.replace({
    path: '/order/index',
    state: {
      'detailList': JSON.stringify(detailList.value),
      'allPrice': totalPrice.value,
      'countPrice': selectedSeats.value[0].price,
      'num': selectedSeats.value.length,
      'ticketCategoryId': ticketCategoryId,
      'seatIdList': JSON.stringify(seatIdList),
      'isChooseSeat': true,
      'selectedSeats': JSON.stringify(selectedSeats.value)
    }
  })
}

// 获取座位数据
const fetchSeatData = async () => {
  try {
    const programId = detailList.value.id
    const response = await getSeatList({ programId })
    if (response.code === '0' && response.data) {
      seatData.value = response.data
    } else {
      ElMessage.error('获取座位信息失败')
    }
  } catch (error) {
    console.error('获取座位信息失败:', error)
    ElMessage.error('获取座位信息失败')
  }
}

onMounted(() => {
  // 从history.state获取节目详情
  if (history.state && history.state.detailList) {
    detailList.value = JSON.parse(history.state.detailList)
    fetchSeatData()
  } else {
    ElMessage.error('缺少节目信息')
    router.back()
  }
})
</script>

<style scoped lang="scss">
.seat-select-container {
  min-height: 100vh;
  background: #f5f5f5;
  
  .main-content {
    width: 100%;
    max-width: 1400px;
    margin: 0 auto;
    padding: 20px 20px 100px;
    
    // 顶部时间和票价区域
    .top-bar {
      background: #fff;
      padding: 15px 20px;
      margin-bottom: 15px;
      
      .date-time {
        font-size: 16px;
        color: #333;
        margin-bottom: 15px;
        font-weight: 500;
      }
      
      .price-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        
        .price-tag {
          display: inline-flex;
          align-items: center;
          padding: 6px 12px;
          border: 1px solid #e5e5e5;
          border-radius: 4px;
          cursor: pointer;
          transition: all 0.2s;
          background: #fff;
          
          &:hover {
            border-color: #FF375D;
          }
          
          &.active {
            border-color: #FF375D;
            background: #FFF5F7;
          }
          
          .color-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            margin-right: 6px;
          }
          
          .price-text {
            font-size: 14px;
            color: #666;
            
            &.active-price {
              color: #FF375D;
              font-weight: 500;
            }
          }
        }
      }
    }
    
    // 座位图区域
    .seat-area {
      background: #E8ECF0;
      border-radius: 8px;
      padding: 30px 20px;
      min-height: 500px;
      
      .venue-wrapper {
        max-width: 1100px;
        margin: 0 auto;
        
        // 舞台提示框
        .stage-box {
          width: 240px;
          margin: 0 auto 25px;
          padding: 14px 0;
          background: #fff;
          border: 2px solid #d0d5dc;
          border-radius: 4px;
          text-align: center;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
          
          span {
            font-size: 18px;
            color: #333;
            font-weight: 500;
            letter-spacing: 8px;
          }
        }
        
        // 场馆轮廓
        .venue-outline {
          background: #fff;
          border-radius: 12px;
          padding: 35px 30px 45px;
          position: relative;
          border: 3px solid #d0d5dc;
          
          .seat-map-container {
            position: relative;
            z-index: 1;
            
            .seat-row {
              display: flex;
              align-items: center;
              justify-content: center;
              margin-bottom: 8px;
              transition: padding 0.3s ease;
              
              .row-label {
                width: 45px;
                font-size: 12px;
                color: #999;
                text-align: center;
                flex-shrink: 0;
              }
              
              .seats {
                display: flex;
                justify-content: center;
                align-items: center;
                flex: 1;
                
                // 座位区域
                .seat-section {
                  display: flex;
                  gap: 3px;
                  
                  &.left-section {
                    justify-content: flex-end;
                  }
                  
                  &.center-section {
                    justify-content: center;
                  }
                  
                  &.right-section {
                    justify-content: flex-start;
                  }
                }
                
                // 过道间隔
                .aisle {
                  width: 25px;
                  height: 16px;
                  flex-shrink: 0;
                }
                
                .seat {
                  width: 16px;
                  height: 16px;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  cursor: pointer;
                  transition: transform 0.15s;
                  
                  &:hover:not(.seat-sold) {
                    transform: scale(1.3);
                  }
                  
                  &.seat-sold {
                    cursor: not-allowed;
                  }
                  
                  &.seat-selected {
                    .seat-dot {
                      transform: scale(1.2);
                    }
                  }
                  
                  .seat-dot {
                    width: 12px;
                    height: 12px;
                    border-radius: 50%;
                    transition: all 0.15s;
                  }
                }
              }
            }
            
            // 上下区域之间的横向过道
            .horizontal-aisle {
              height: 20px;
              margin: 5px 0;
            }
          }
        }
      }
    }
    
    // 底部栏
    .bottom-bar {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      background: #fff;
      box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.08);
      padding: 12px 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      z-index: 100;
      
      .left-section {
        display: flex;
        align-items: center;
        gap: 20px;
        
        .price-display {
          .currency {
            font-size: 16px;
            color: #333;
          }
          
          .amount {
            font-size: 28px;
            font-weight: bold;
            color: #333;
          }
        }
        
        .selected-seats {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
          max-width: 600px;
          
          .seat-tag {
            display: inline-flex;
            align-items: center;
            padding: 4px 10px;
            background: #FFF0F3;
            color: #FF375D;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
            transition: all 0.2s;
            
            &:hover {
              background: #FFE0E6;
            }
            
            .remove-icon {
              margin-left: 4px;
              font-style: normal;
              font-size: 14px;
            }
          }
        }
      }
      
      .buy-btn {
        padding: 14px 50px;
        font-size: 18px;
        color: #fff;
        background: linear-gradient(135deg, #FF6B9D 0%, #FF375D 100%);
        border: none;
        border-radius: 25px;
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover:not(.disabled) {
          transform: translateY(-2px);
          box-shadow: 0 4px 15px rgba(255, 55, 93, 0.4);
        }
        
        &.disabled {
          background: #ccc;
          cursor: not-allowed;
        }
      }
    }
  }
}
</style>
