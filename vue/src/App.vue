<template>
  <div id="app">
    <notification-modal v-if="showNotificationModal && $route.name !== 'login' && $route.name !== 'register'" :message="notificationMessage" @close="showNotificationModal = false"></notification-modal>
    <div id="nav" class="nav-bar">
      <router-link to="/" class="site-title-link">
        <h1 class="site-title">Later Ladder</h1>
      </router-link>
      <div class="nav-links">
        <router-link class="nav-link" v-bind:to="{ name: 'about' }" v-if="$route.path !== '/about'">About</router-link>
        <router-link class="nav-link" v-bind:to="{ name: 'login' }" v-if="$store.state.token === ''">Login</router-link>
        <router-link class="nav-link" v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
import NotificationModal from './components/NotificationModal.vue';

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
    closeNotification() {
      this.showNotificationModal = false;
    },
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
  font-family: 'Roboto Condensed', sans-serif;
  text-transform: uppercase;
  color: var(--afl-200);
  margin: 0;
  padding: 0 8px;
  font-size: x-large;
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


@media (max-width: 600px) {
  .nav-bar {
    padding: 8px;
    margin-bottom: 1.25rem;
  }

  .site-title {
    font-size: x-large;
    padding: 0 4px;
  }

  .nav-link {
    margin: 0 8px;
  }
}

</style>