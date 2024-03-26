<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card>
          <div style="width: 100%;height:400px " id="line">
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>

          <div style="width: 100%;height:400px " id="line">
            co2含量图

          </div>
        </el-card>

      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import request from "@/utils/request.is";

const option = {
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [150, 230, 224, 218, 135, 147, 200],
      type: 'line'
    }
  ]
};

export default {
  name: "AirDataChart",

  mounted() {
    //页面加载好后再触发初始化，等待页面加载好DOM
    let chartDom = document.getElementById('line');
    let lineChart = echarts.init(chartDom);
    request.get('air/{id}').then(res => {
      this.airList = res.data;
    })

    lineChart.setOption(option)//图标加载数据
  }
}

</script>

<style scoped>

</style>