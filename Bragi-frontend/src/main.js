import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/theme/index.css'

import contentmenu from 'v-contextmenu'
import '@/assets/v-contextmenu/index.css'
import './assets/icon/iconfont.css' //引入阿里巴巴图标库css
import '@/assets/font/font.css';


Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(contentmenu)
import vClickOutside from 'v-click-outside'

Vue.use(vClickOutside)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
