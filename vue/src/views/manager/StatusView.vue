<template>
  <div>
    <div>
      渲染开关数据
    </div>

    <template>
      <div>

        <div v-for="device in statusList" :key="device.id">
          Device {{ device.id }}:
          <el-switch v-model="device.light" @change="handleSwitchChange(device, 'light')"/>
          <el-switch v-model="device.heater" @change="handleSwitchChange(device, 'heater')"/>
          <el-switch v-model="device.fan" @change="handleSwitchChange(device, 'fan')"/>
          <el-switch v-model="device.atomizer" @change="handleSwitchChange(device, 'atomizer')"/>
          <el-switch v-model="device.curtain" @change="handleSwitchChange(device, 'curtain')"/>
          <el-switch v-model="device.drencher" @change="handleSwitchChange(device, 'drencher')"/>
          <el-switch v-model="device.man" @change="handleSwitchChange(device, 'man')"/>
          <el-switch v-model="device.pig" @change="handleSwitchChange(device, 'pig')"/>
          <el-switch v-model="device.water" @change="handleSwitchChange(device, 'water')"/>
        </div>
      </div>
    </template>
    <div>
      <el-button type="primary" @click="refresh">刷新</el-button>
    </div>

  </div>
</template>

<script>

import request from "../../utils/request.is";
import axios from "axios";

export default {
  name: 'StatusView',
  data() {
    return {
      statusList: [],
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
    }
  },

  computed: {},
  /*页面加载好后再请求数据*/

  mounted() {
    this.refresh()
    //const refreshInterval = 1000; // 刷新间隔，单位为毫秒（这里是1秒）
    //setInterval(this.refresh, refreshInterval);
  },

  methods: {
    handleSwitchChange(device, switchType) {
      request.post('/status/update',device
      ).then(
          res => {
            if (res.code === 200) {
              this.$message.success("修改成功")
            } else {
              this.$message.error(res.msg)
            }

          }
      )
      // 在这里处理开关状态变化后的逻辑，例如向后端发送更新请求
      /*console.log(`Device   ${device.id} ${switchType} 状态变化：`, device[switchType]);*/
    },
    refresh() {
      this.$request.get('/status/list').then(res => {
        this.statusList = res.data;
      })
    }


  }
}
</script>

