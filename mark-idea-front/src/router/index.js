import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'basic',
    component: () => import('../components/navbar.vue'),
    children:[
      {
        path: 'content/:notebookName/:noteTitle',
        name: 'content',
        component: () => import('../views/Content.vue')
      },
      {
        path: 'setting',
        name: 'setting',
        component: () => import('../views/Setting.vue')
      },
      {
        path: 'admin',
        name: 'admin',
        component: () => import('../views/Admin.vue')
      },
      {
        path: 'article/:author/:id',
        name: 'article',
        props: true,
        component: () => import('../views/Article.vue')
      }
    ]
  },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },

]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// 设置路由导航守卫，注册全局前置守卫，判断用户是否登录
// router.beforeEach((to, from, next) => {
//     if (to.path === '/login') {
//         var token1 = this.$store.getters.getToken;
//         //没有token或token过期，跳到登录界面,否则跳到welcome，不允许访问login
//         if (token1 === null || token1 === '') {
//             next();
//         } else {
//             next('/');
//         }
//     } else {
//         var token = this.$store.getters.getToken;
//         //没有token或token过期，跳到登录界面
//         if (token === null || token === '') {
//             next('/login');
//         } else {
//             next();
//         }
//     }
// })


export default router
