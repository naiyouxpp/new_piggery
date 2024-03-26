<template>
  <div style="height: 100vh ; overflow: hidden;display: flex;align-items: center;justify-content: center ; background-color: dodgerblue">

  <div style="display: flex; background-color: white ; border-radius: 5px;overflow: hidden"   ><!--图片盖住圆角了，用overflow:hidden-->
    <div style="flex: 1">
      <img alt="" src="@/assets/register.png" style="width: 100%;">
    </div>


    <div style="flex: 1;display: flex;align-items: center;justify-content: center" >
      <el-form :model="user" :rules="rules" ref="registerRef">
        <div style="font-size: 20px;font-weight: bold;text-align: center;margin-bottom: 20px">欢迎注册后台管理系统</div>
          <el-form-item prop="username" style="width: 300px">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号" v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPass">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请确认密码" v-model="user.confirmPass"></el-input>
          </el-form-item>
          <el-form-item prop="role"> <div style="display: flex; justify-content: center;align-items: center">

          </div>
              <el-radio-group v-model="user.role">

                <el-radio-button label="用户">
                </el-radio-button>
                <el-radio-button label="商家">
                </el-radio-button>

              </el-radio-group>


           </el-form-item>



          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="register">
              注         册
            </el-button>
          </el-form-item>
        <div style="display: flex">
          <div style="flex: 1">
            已有账号？返回
            <span style="color: #409EFF;cursor: pointer" @click="$router.push('/login')">登录</span>
          </div>

        </div>
      </el-form>
    </div>


  </div>

  </div>
</template>

<script>
export default {
  name: "Register.vue",
  data(){
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'));
      } else if (confirmPass!== this.user.password){
        callback(new Error('两次输入密码不一致'));
      }else{
        callback();
      }
    };
    return{
      user:{
        confirmPass:'',//密码的二次确认
        username:'',
        password:'',
      },
      /*rules是给表单校验用的，rules中的username和password必须和prop中的属性以及user对象中的属性一一对应(也就是名字相同)*/
      rules:{
        username: [{
          required:true,message:'请输入账号',trigger: 'blur'
        }],
        password: [{
          required:true,message:'请输入密码',trigger: 'blur'
        }],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        role: [{
          required:true,message:'选择角色',trigger: 'blur'
        }],
      },
    }
  },
  methods:{
    register(){
      this.$refs['registerRef'].validate((valid)=>{
        if(valid){
          //验证通过，可以发请求
          this.$request.post('/user/register',this.user).then( res =>{
            if(res.code === 200){
              this.$router.push('/login')
              this.$message.success("注册成功")
            }
            else{
              this.$message.error(res.msg)
            }
          })
        }
      })

    }
  }
}

</script>

<style scoped>

</style>