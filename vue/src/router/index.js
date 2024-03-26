import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import(
        '../views/Manager'
        ),
    meta: { title: '首页' },
    redirect:'home',
    children:[
      {path:'home',name:'Home',component:()=>import('../views/manager/HomeView')}, /*首页*/
      {path:'air',name:'Air',component:()=>import('../views/manager/AirView')},    /*空气数据页*/
      {path:'air_2.0',name:'Air_2.0',component:()=>import('../views/manager/AirView_2.0')},    /*空气数据页*/
      {path:'status',name:'Status',component:()=>import('../views/manager/StatusView')}, /*下位机开关页*/
      {path:'user',name:'User',component:()=>import('../views/manager/UserView')}, /*员工信息页*/
      {path:'personal',name:'Personal',component:()=>import('../views/manager/PersonalView')}, /*个人信息页*/
      {path:'403',name:'Auth',component:()=>import('../views/manager/AuthView')}, /*403页面*/
      {path:'airDataChart',name:'AirDataChart',component:()=>import('../views/manager/AirDataChartView')}, /*空气数据图标统计页*/
    ]
  },
    /*登录页面*/
  {
    path: '/login',
    name: 'Login',
    component: ()=> import('../views/Login')
  },
    /*注册页面*/
  {
    path: '/register',
    name: 'Register',
    component: ()=> import('../views/Register'),
  }  ,
  /*404页面*/
  {
    path: '*',
    name: '404',
    component: ()=> import('../views/404'),
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//这是路由守卫
router.beforeEach((to, from, next)=>{
  let adminPaths=['/user']
  let user = JSON.parse(localStorage.getItem("honey-user")||"{}")
  if (user.role !== '管理员' && adminPaths.includes(to.path)){
    next('/403')//跳转到权限不足得页面
  }
  else
  {
    next()//放行
  }
})

export default router
