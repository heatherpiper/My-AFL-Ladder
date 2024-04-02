import Vue from 'vue'
import App from './App.vue'
import router from './router/index'
import store from './store/index'
import axios from 'axios'
import { bus } from './event-bus.js'
import GAuth from 'vue-google-oauth2'

Vue.config.productionTip = false;

Vue.prototype.$bus = bus;

axios.defaults.baseURL = process.env.VUE_APP_REMOTE_API;

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response && error.response.status === 401) {
    Vue.prototype.$bus.$emit('show-notification', 'Your session has expired. Please log in again.');
  }
  return Promise.reject(error);
});

const gauthOption = {
  clientId: process.env.GOOGLE_CLIENT_ID,
  scope: 'profile email',
  prompt: 'select_account',
};

Vue.use(GAuth, gauthOption);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
