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
        path: '',
        name: 'warmup',
        component: () => import('../components/Warmup.vue'),
    },
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
        path: 'article/:author/:id',
        name: 'article',
        props: true,
        component: () => import('../views/Article.vue')
      },
        {
            path: 'reference',
            name: 'reference',
            props: true,
            component: () => import('../components/Reference.vue')
        }
    ]
  },
      {
          path:'/retrievepwd',
          name: 'retrievepwd',
          component:()=>import('../views/Retrievepwd.vue'),
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
    if (to.path === '/login' || to.path === '/signup'||to.path === '/retrievepwd') {
        var token1 = store.getters.getToken;

        console.log('路径守卫1')
        console.log(token1)

        //没有token或token过期，跳到登录界面,否则跳到welcome，不允许访问login
        if (!token1 || token1 === null || token1 === '') {

            console.log('0')

            next();
        } else {

            console.log('2')

            next('/');
        }
    } else {

        console.log('路径守卫开启了')

        var token = store.getters.getToken;

        console.log(token)

        //没有token或token过期，跳到登录界面
        if (!token ||token === null || token === '') {

            console.log('未登录')

            next('/login');
        } else {
            console.log('已登录，直接跳转')

            next();
        }
    }
})


export default router
