<template>
  <div id="app">
    <div class="content-wrapper">
      <notification-modal v-if="showNotificationModal && $route.name !== 'login' && $route.name !== 'register'" :message="notificationMessage" @close="handleNotificationClose"></notification-modal>
      <NavBar />
      <router-view />
    </div>
    <Footer />
  </div>
</template>

<script>
import NotificationModal from './components/NotificationModal.vue';
import NavBar from './components/NavBar.vue';
import Footer from './components/Footer.vue';

export default {
  components: {
    NotificationModal,
    NavBar,
    Footer,
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
    handleNotificationClose() {
      this.$store.commit('LOGOUT');
      this.$router.push({ name: 'login' });
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

body, html {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  margin: 0;
  padding: 0;
}

#app {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.content-wrapper {
  flex: 1;
  margin-bottom: 4em;
}

body {
  background: radial-gradient(circle at center, var(--afl-500) 0%, var(--afl-600) 40%, var(--afl-800) 100%);
  background-attachment: fixed;
  font-family: 'Inter', sans-serif;
}

img {
  max-width: 100%;
}

@media (max-width: 600px) {

  .content-wrapper {
    margin-bottom: .75em;
  }
}

</style>