<template>

  <el-card shadow="always">
    <div v-for="(air, index) in airList" :key="index" style="padding: 20px;">

      <div class="data-box">

        <div style="color: #6A4D68;font-weight: bold ; font-size: 14px">猪舍号: {{ air.id }}</div>

        <div class="air-show-text" @click="pathFinder(air.id)" style="display: flex;justify-content: center;align-items: center;">

          <div style="padding: 10px">温度: {{ air.temperature }}°</div>
          <div style="padding: 10px">二氧化碳浓度: {{ air.co2 }}</div>
          <div style="padding: 10px">甲醛浓度: {{ air.ch2o }}</div>
          <div style="padding: 10px">二氧化碳浓度: {{ air.ch2o }}</div>
          <div style="padding: 10px">PM2.5浓度: {{ air.pm25 }}</div>
          <div style="padding: 10px">PM10浓度: {{ air.pm10 }}</div>
          <div style="padding: 10px">湿度: {{ air.humidity }}</div>
        </div>
      </div>
      <!-- 根据需要添加更多传感器数据字段 -->
    </div>
  </el-card>

</template>

<script>
import request from "@/utils/request.is";

export default {
  data() {
    return {
      airList: '',

    };
  },
  mounted() {
    request.get('air/list').then(res => {
      this.airList = res.data;
    })
  },
  methods:{
    pathFinder(id){
      //this.$router.push('/airDataChart')
      this.$router.push({ path: '/airDataChart', query: { id: id } });//带数据传参
    }
  },

};
</script>

<style>
.air-show-text {
  color: #8480B3;
  font-size: 16px;
}
/*.air-show-text:hover{
  color: #8480B3 !important;
  font-weight: bold !important;
}*/
.data-box {
  width: 100%;
  height: 80px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  transition: border 0.3s; /* 添加过渡效果 */
}
.data-box:hover {
  border-width: 2px !important;
  border-color: #007bff !important;
  box-shadow: 0 0 10px rgba(0, 123, 255, 0.5) !important;
  font-weight: bold !important;

}

</style>
