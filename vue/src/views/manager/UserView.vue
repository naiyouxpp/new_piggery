<template>
  <div style="display: flex;flex-flow: column">
    <el-card style="width: 100%;margin-right: 10px">
      <div slot="header" class="clearfix">
        渲染员工数据
      </div>
      <div>
        <el-table :data="userList">
            <el-table-column label="头像" prop="avatar">
              <template v-slot="scope">
                <el-image
                    v-if="scope.row.avatar"
                    :src="scope.row.avatar"
                    style="width: 60px; height: 60px"
                    :preview-src-list="[scope.row.avatar]"
                >
                </el-image>
              </template>
            </el-table-column>    
          <el-table-column label="姓名" prop="name"></el-table-column>
          <el-table-column label="账号" prop="username"></el-table-column>
          <el-table-column label="手机号码" prop="phone"></el-table-column>
          <el-table-column label="邮箱" prop="email"></el-table-column>
          <el-table-column label="住址" prop="address"></el-table-column>


        </el-table>
      </div>
    </el-card>

  </div>

</template>

<script>

import request from "../../utils/request.is";

export default {
  name: 'UserView',
  data() {
    return {
      userList: [],
      user: JSON.parse(localStorage.getItem("honey-user") || "{}")
    }
  },
  /*页面加载好后再请求数据*/
  mounted() {
    request.get('/user/list').then(
        res => {
          this.userList = res.data
        }
    )
  },
  methods: {}
}
</script>
