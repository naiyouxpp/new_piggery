<template>
  <div>
    <el-container>
      <el-aside width="asideWidth" style=" min-height: 100vh ; background-color: #81D8CF ;z-index: 1;">
        <!--     侧边栏   -->


        <div style="height: 60px; color: #ffffff; display:flex; align-items: center; justify-content: center" >
          <!--     logo     -->
          <img src="@/assets/logo8.png" alt="" style="width: 50px;height: 50px;">
          <span class="logo-title" v-show="!isCollapse">Intelligent Piggery</span>
        </div>


        <el-menu  :collapse="isCollapse" :collapse-transition="false" router style="border: none; background-color: #81D8CF ;" text-color="rgba(255,255,255,0.65)" active-text-color="#fff" :default-active="$route.path"  >
          <!--    菜单      -->

          <el-menu-item index="/home">
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

          <!--跟下位机相关的（空气数据和开关信息）-->

          <el-submenu index="/info">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>数据勘查</span>
            </template>
            <el-menu-item  index="/air">
              <template slot="title">
                <span>空气数据</span>
              </template>
            </el-menu-item>

            <el-menu-item  index="/air_2.0">
              <template slot="title">
                <span>空气数据2.0</span>
              </template>
            </el-menu-item>

            <el-menu-item  index="/status">
              <template slot="title">
                <span>开关情况</span>
              </template>
            </el-menu-item>
          </el-submenu>

          <!--     跟账号或者个人资料相关的     -->

          <el-submenu index="/4">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理</span>
            </template>
            <el-menu-item  v-if="user.role=== '管理员'" index="/user">
              <template slot="title">
                <span>员工信息</span>
              </template>
            </el-menu-item>
            <el-menu-item  index="/personal">
              <template slot="title">
                <span>个人资料</span>
              </template>
            </el-menu-item>
          </el-submenu>



        </el-menu>


      </el-aside>
      <el-container>
        <!--    右半部分    -->
        <el-header style="z-index: 1;">
          <!--头头-->
          <i :class="collapseIcon" style="font-size:26px ;" @click="handleCollapse">  </i>

          <!--     面包屑     -->
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-right: 20px">
            <el-breadcrumb-item>首页</el-breadcrumb-item>
          </el-breadcrumb>
          <div style="flex: 1;width: 0;display: flex;align-items: center;justify-content: flex-end"><!--头像框居右-->
            <i class="el-icon-FullScreenquanping" style="font-size: 26px" @click="handleFullScreen"></i>
            <el-dropdown placement="bottom"><!--下拉框居中-->
              <!--      头像        -->
              <!--      cursor:让鼠标悬停的时候不显示文字选中的样式，任然保留鼠标箭头的样式        -->

              <div style="display: flex;align-items: center;cursor: default">
                <img src="@/assets/logo8.png" alt="" style="width: 40px;height: 40px ; margin: 0 5px">
                <span>管理员</span>
              </div>
              <el-dropdown-menu solt="dropdown">
                <el-dropdown-item @click.native="personalData">个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item><!--下拉框没有click事件，必须用@click.native-->
              </el-dropdown-menu>
            </el-dropdown>
          </div>

        </el-header>
        <el-main style="background-color: #F8F5D6; ">
          <!--主体部分-->
          <!--    框架，主体在这切换      -->
          <router-view />

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

import request from "../utils/request.is";

export default {
  name: 'HomeView',
  data(){
    return{
      isCollapse: false, //默认不收缩
      asideWidth: "200px",
      collapseIcon: 'el-icon-s-fold',//默认是收缩图标
      airList:[],
      fileList:[],
      user:JSON.parse(localStorage.getItem("honey-user")||"{}"),

    }
  },
  /*页面加载好后再请求数据*/
  mounted(){
    /*axios.get('http://localhost:8080/air/list').then(res=>{
      console.log(res)
      this.airList=res.data.data
    })*/
    if(!this.user.id){
      this.$router.push('/login');
    }else{
      request.get('/air/list').then(
          res=>{
            this.airList=res.data
          }
      )

    }

  },
  methods:{

    personalData(){
      //如果当前的访问路径已经是personal，那么跳转到相同路径会报错，提前判断一下当前路径是否===personal
      if (this.$route.path !== '/personal') {
      console.log('Navigating to /personal');
      this.$router.replace('/personal');
    }

    },
    logout(){
      localStorage.removeItem('honey-user')//清除当前的token和user数据
      localStorage.removeItem('currentUserId')//清除当前的token和user数据
      this.$router.push('/login')
    },
    handleFullScreen(){
      document.documentElement.requestFullscreen()
    },
    handleCollapse(){
      this.isCollapse=!this.isCollapse
      this.asideWidth=this.isCollapse ? '64px' : '200px'
      this.collapseIcon=this.collapseIcon ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    }
  }
}
</script>

<style>
.el-menu--inline{
  background-color: #3ABEB0 !important;
}
.el-menu--inline .el-menu-item{
  background-color: #3ABEB0 !important;
  padding-left: 49px !important;
}
.el-menu-item :hover{
  color: rgb(255, 255, 255) !important;
}
.el-menu-item :hover i{
   color: rgb(255, 255, 255) !important;
 }
.el-submenu__title:hover{
  color: #000000 !important;
}
.el-submenu__title:hover i{
  color: #000000 !important;
}

.el-menu-item.is-active {
  background-color: #02EEE6 !important;
  border-radius: 8px !important;
  width: calc(100% - 8px)!important;
  height:40px!important;
  margin: 4px;
  }
.el-menu-item.is-active i,.el-menu-item.is-active .el-tooltip {
  margin-left: -4px;
}
.el-menu-item{

  height: 40px !important;
  line-height: 40px !important;

}
.el-submenu__title{

  height: 40px !important;
  line-height: 40px !important;

}
.el-submenu .el-menu-item{
  min-width: 0px !important;
}
.el-menu--inline .el-menu-item.is-active{
  padding-left: 45px !important;
}

/*为啥动画没效果呢!!*/
.el-aside {
  transition: width 0.3s;
  box-shadow: 2px 0 6px rgba(0,21,41,.35)!important;
}

.el-submenu__icon-arrow{
  margin-top: -5px !important;
}

/*为啥动画还是没效果呢!!!*/
.logo-title{
  margin-right:5px;
  font-size: 13px;
  transition: all .3s;
}
.el-header{
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  align-items: center;
}


</style>