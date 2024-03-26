<template>
  <div>
    <el-card style="width: 100%;margin-right: 10px">
      <div slot="header" class="clearfix">
        渲染空气数据
      </div>
      <div>
        <el-table :data="airList">
          <el-table-column label="ID" prop="id"></el-table-column>
          <el-table-column label="温度" prop="temperature"></el-table-column>
          <el-table-column label="二氧化碳" prop="co2"></el-table-column>
          <el-table-column label="甲醛" prop="ch2o"></el-table-column>
          <el-table-column label="TVOC" prop="tvoc"></el-table-column>
          <el-table-column label="PM2.5" prop="pm25"></el-table-column>
          <el-table-column label="PM10" prop="pm10"></el-table-column>
          <el-table-column label="湿度" prop="humidity"></el-table-column>
        </el-table>
      </div>
      <div>
<!--  暂时不开自动刷新搞个手动刷新的用用      -->
        <el-button type="primary" style="margin: 20px" @click="load">刷新</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>

import request from "../../utils/request.is";

export default {
  name: 'AirView',
  data() {
    return {
      airList: [],
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
    }
  },
  mounted() {
    this.load()
    //const refreshInterval = 1000; // 刷新间隔，单位为毫秒（这里是1秒）
    //setInterval(this.load, refreshInterval);
  },

  methods: {
    load() {
      this.$request.get('/air/list').then(res => {
        this.airList = res.data;
      })
    }

  }
}
</script>

