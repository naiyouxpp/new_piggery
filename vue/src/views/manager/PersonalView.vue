<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span>个人资料</span>
      </div>

      <!--      <div style="display: flex; align-items: center;justify-content: center">
              头像上传下载
              <el-upload
                  class="avatar-uploader"
                  action="https://localhost:8080/file/upload"
                  :on-success="handleUpload"
                  :file-list="fileList">

                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </div>-->
      <el-form :model="user" :rules="rules" ref="userForm" label-width="80px">

        <el-form-item prop="avatar">
          <div style="display: flex;align-items: center;justify-content: center">
            <el-upload style="margin-bottom: 20px;"
                       class="avatar-uploader"
                       :action="$baseUrl + '/file/upload'"
                       :headers="{token: user.token, 'X-Current-User-Id': user.id}"
                       :on-success="(user,file,fileList)=>handleAvatarSuccess(this.user,file,fileList)"
            >
              <img v-if="this.user.avatar" :src="this.user.avatar" class="avatar">
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" :disabled="isEditing"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" :disabled="isEditing"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="user.phone" :disabled="isEditing"></el-input>
        </el-form-item>
        <el-form-item label="住址" prop="address">
          <el-input v-model="user.address" :disabled="isEditing"></el-input>
        </el-form-item>

        <!-- 其他个人信息字段 -->

        <el-form-item>
          <el-button @click="handleEdit">编辑</el-button>
          <el-button @click="handleSave" :disabled="this.isEditing===true">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>

export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"), // 包含用户信息的对象
      isEditing: true, // 是否处于编辑状态
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入邮箱', trigger: 'blur'},
          {type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change']},
        ],
        // 其他个人信息字段的验证规则
      },
    };
  },
  methods: {

    handleAvatarSuccess(user, file, fileList) {
      console.log(user, file, fileList)

      user.avatar = file.response.data;

      this.$request.post('user/update', user).then(res => {
        if (res.code === 200) {

          localStorage.setItem("honey-user", JSON.stringify(res.data))//更新用户数据

          this.$message.success('上传成功')
        } else {
          this.$message.success(res.msg)
        }
      })

    },


    handleEdit() {
      this.isEditing = !this.isEditing;
    },
    handleSave() {

      this.$request.post('/user/update', this.user).then(res => {
        if (res.code === 200) {

          localStorage.setItem("honey-user", JSON.stringify(res.data))//更新用户数据

          this.$message.success("个人信息修改成功")
        } else {
          this.$message.error(res.msg)
        }
      })
      // 在这里可以进行保存用户信息的操作
      this.isEditing = true;
    },
  },
  mounted() {
    // 假设 user 变量已经在登录时被设置
    // 如果数据是通过异步请求获取的，可以在此处进行异步请求获取数据并设置给 user
    // this.user = yourAsyncDataFetchingFunction();
  },
};
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>