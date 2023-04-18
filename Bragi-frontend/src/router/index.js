import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store/index";

Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
    if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
    return originalPush.call(this, location).catch(err => err)
}

  const routes = [
  {
    path: '/',
    name: 'basic',
    component: () => import('../views/BasicLayout.vue'),
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
    {
      path: '/signup',
      name: 'signup',
      component: () => import('../views/Signup.vue')
    },

]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})


//设置路由导航守卫，注册全局前置守卫，判断用户是否登录
router.beforeEach((to, from, next) => {
    if (to.path === '/login') {
        var token1 = store.getters.getUsername;

        console.log('路径守卫1')
        console.log(token1)

        //没有token或token过期，跳到登录界面,否则跳到welcome，不允许访问login
        if (typeof(token1) == "undefined" || token1 === null || token1 === '') {

            console.log('0')

            next();
        } else {

            console.log('2')

            next('/');
        }
    } else {

        console.log('路径守卫开启了')

        var token = store.getters.getUsername;
        //没有token或token过期，跳到登录界面
        if (typeof(token) == "undefined" ||token === null || token === '') {

            console.log('未登录，想要跳到登录')

            next('/login');
        } else {
            console.log('已登录，直接跳转')

            next();
        }
    }
})


export default router
