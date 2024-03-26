<template>
  <div style="height: 100vh ; overflow: hidden;display: flex;align-items: center;justify-content: center ; background-color: #0f9876">

  <div style="display: flex; background-color: white ; border-radius: 5px;overflow: hidden"><!--图片盖住圆角了，用overflow:hidden-->
    <div style="flex: 1">
      <img alt="" src="@/assets/login.png" style="width: 100%">
    </div>


    <div style="flex: 1;display: flex;align-items: center;justify-content: center" >
      <el-form :model="user" :rules="rules" ref="loginRef">
        <div style="font-size: 20px;font-weight: bold;text-align: center;margin-bottom: 20px">欢迎登录后台管理系统</div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号" v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div style="display: flex">
              <el-input prefix-icon="el-icon-check" size="medium" style="flex: 1" v-model="user.code"></el-input>
              <div style="flex: 1">
                <valid-code @update:value="getCode" style="height: 36px"></valid-code>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">
              登录
            </el-button>
          </el-form-item>
        <div style="display: flex">
          <div style="flex: 1">
            还没有账号？请
            <span style="color: #0f9876;cursor: pointer" @click="$router.push('/register')">注册</span>
          </div>
          <div style="flex: 1;text-align: right">


            <span style="color: yellowgreen;cursor: pointer">忘记密码</span>

          </div>

        </div>
      </el-form>
    </div>


  </div>

  </div>
</template>

<script>
import ValidCode from "@/components/validCode";

export default {
  name: "Login.vue",
  components:{
    ValidCode
  },
  data(){
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'));
      } else if (value.toLowerCase()!== this.code){
        callback(new Error('验证码错误'));
      }else{
        callback();
      }
    };
    return{
      code:'',//验证码组件传递过来的code
      user:{
        code:'',//用户输入的code
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
        code: [
          { validator: validateCode, trigger: 'blur' }
        ],
      },
    }
  },
  mounted() {

  },
  methods:{
    // 在需要的地方设置本地存储的数据

    getCode(code){
      console.log(code)
      this.code = code.toLowerCase();//toLowerCase()是变成小写
    },
    login() {
      this.$refs['loginRef'].validate((valid)=>{
        if(valid){
          //验证通过，可以发请求
          this.$request.post('/user/login',this.user).then( res =>{

            if(res.code === 200){
              localStorage.setItem("honey-user",JSON.stringify(res.data))//存储用户数据



              this.$router.push('/')
              this.$message.success("登录成功")
            }
            else{
              this.$message.error(res.msg)
            }
          })
        }
      })

    },

  },
}

</script>

<style scoped>

</style>