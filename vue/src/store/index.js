import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem('token')
const currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken != null) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  state: {
    token: currentToken || '',
    user: currentUser || {},
    isDarkMode: true
  },
  mutations: {
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem('user', JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      state.token = '';
      state.user = {};
      axios.defaults.headers.common = {};
    },
    TOGGLE_MODE(state) {
      state.isDarkMode = !state.isDarkMode;
    }
  },
  actions: {
    toggleMode({ commit }) {
      commit('TOGGLE_MODE');
    },
    async loginWithGoogle({ commit }, { idToken }) {
      try {
        const response = await axios.post('/auth/google', { idToken });
        commit('SET_USER', response.data.user);
        commit('SET_AUTH_TOKEN', response.data.token);
      } catch (error) {
        console.error('Error during Google login:', error);
      }
    },
    async exchangeAuthorizationCode({ commit }, authorizationCode) {
      try {
        const response = await axios.post('/auth/google/exchange', { code: authorizationCode });
        const { token, user } = response.data;
        commit('SET_AUTH_TOKEN', token);
        commit('SET_USER', user);
      } catch (error) {
        console.error('Error during token exchange:', error);
        throw error;
      }
    }
  },
  getters: {
    isDarkMode: state => state.isDarkMode
  }
})
