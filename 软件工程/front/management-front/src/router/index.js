import VueRouter from "vue-router";
import Vue from "vue";
import Home from './Home.vue';
import Login from './Login.vue';
import AdminLogin from './AdminLogin.vue';
import MainPage from "./MainPage.vue";
import Book from "./Book.vue";
import BookManage from "./BookManage.vue";
import Role from './Role.vue';
import User from './User.vue';
import Auth from './Auth.vue';

Vue.use(VueRouter)

const router = new VueRouter({
    routes:[
        {
            path:'/admin',
            component:Home,
            children:[
                {
                    path:'/role',
                    component:Role,
                },
                {
                    path:'/user',
                    component:User
                    
                },
                {
                    path:'/auth',
                    component:Auth
                },
                {
                    path:'/bookm',
                    component:BookManage
                }
            ]
        },
        {
            path:'/',
            component:Login
        },
        {
            path:'/admin_login',
            component: AdminLogin
        },
        {
            path:'/main_page',
            component: MainPage,
            children:[
                {
                    path: '/book',
                    component: Book,
                    
                },
              
            ]
        },
        
    ]
})


function getToken() {
  return sessionStorage.getItem('jwtToken');
}

router.beforeEach((to, from, next) => {
  const token = getToken();

  if (!token && to.path !== '/' && to.path !== '/admin_login') { // 如果没有token且当前路径不是登录页
    next('/'); // 重定向到登录页
  } else {
    next(); // 如果有token或者当前就在登录页，允许继续导航
  }
});
export default router