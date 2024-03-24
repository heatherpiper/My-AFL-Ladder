<template>
  <div id="app">
    <notification-modal v-if="showNotificationModal && $route.name !== 'login' && $route.name !== 'register'" :message="notificationMessage" @close="handleNotificationClose"></notification-modal>
    <div id="nav" class="nav-bar">
      <router-link to="/" class="site-title-link">
        <img src="@/assets/LaterLadder-Wordmark.webp" alt="Later Ladder title" class="site-title">
      </router-link>
      <div class="nav-links">
        <router-link class="nav-link" v-bind:to="{ name: 'about' }" v-if="$route.path !== '/about'">About</router-link>
        <router-link class="nav-link" v-bind:to="{ name: 'login' }" v-if="$store.state.token === ''">Login</router-link>
        <router-link class="nav-link" v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
        <button class="mode-toggle" @click="toggleMode">
          <i class="fas" :class="{ 'fa-sun': isDarkMode, 'fa-moon': !isDarkMode }"></i>
        </button>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
import NotificationModal from './components/NotificationModal.vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  components: {
    NotificationModal,
  },
  data() {
    return {
      showNotificationModal: false,
      notificationMessage: '',
    }
  },
  computed: {
    ...mapGetters(['isDarkMode']),
  },
  created() {
    this.$bus.$on('show-notification', this.showNotification);
    this.$bus.$on('reset-notification-modal', this.resetNotificationModal)
  },
  beforeDestroy() {
    this.$bus.$off('show-notification', this.showNotification);
  },
  methods: {
    resetNotificationModal() {
      this.showNotificationModal = false;
    },
    showNotification(message) {
      this.notificationMessage = message;
      this.showNotificationModal = true;
    },
    handleNotificationClose() {
      this.$store.commit('LOGOUT');
      this.$router.push({ name: 'login' });
    },
    ...mapActions(['toggleMode']),
  },
  watch: {
    '$route'() {
      this.showNotificationModal = false;
    }
  }
};
</script>


<style>

:root {
  --afl-900: #03061C;
  --afl-800: #031745;
  --afl-700: #01285E;
  --afl-600: #004099;
  --afl-550: #0e56b9;
  --afl-500: #046FD9;
  --afl-450: #0e87ff;
  --afl-400: #1FB4FF; 
  --afl-300: #E8001B;
  --afl-250: #8ebeee;
  --afl-200: #EDF4F7;
  --afl-100: #F2F4F7;
}

body, html {
  margin: 0;
  padding: 0;
}

body {
  background: radial-gradient(circle at center, var(--afl-500) 0%, var(--afl-600) 40%, var(--afl-800) 100%);
  background-attachment: fixed;
  font-family: 'Inter', sans-serif;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--afl-800);
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: right;
  margin-bottom: 1.5rem;
}

.site-title {
  height: 2.5em;
  width: auto;
  margin: 0;
  padding: 0 16px;
}

.site-title-link {
  text-decoration: none;
  color: inherit;
}

.site-title-link:hover,
.site-title-link:visited {
  text-decoration: none;
}

.nav-links {
  display: flex;
  align-items: center;
  text-transform: uppercase;
}

.nav-link {
  color: var(--afl-200);
  text-decoration: none;
  margin: 0 16px;
  position: relative;
}

.nav-link:visited {
  color: var(--afl-200);
}

.mode-toggle {
  background: none;
  border: none;
  color: var(--afl-500);
  cursor: pointer;
  font-size: 1.25rem;
  margin-left: 16px;
  outline: none;
  transition: color 0.3s;
}

.mode-toggle:hover {
  color: var(--afl-450);
}

@media (max-width: 600px) {
  .nav-bar {
    padding: 8px;
    margin-bottom: 1.25rem;
  }

  .site-title {
    padding: 0 4px;
  }

  .nav-link {
    margin: 0 8px;
  }
}

</style>