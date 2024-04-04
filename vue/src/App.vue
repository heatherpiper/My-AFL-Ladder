<template>
  <div id="app">

    <div class="content-wrapper">

      <notification-modal v-if="showNotificationModal && $route.name !== 'login' && $route.name !== 'register'" :message="notificationMessage" @close="handleNotificationClose"></notification-modal>
      <NavBar />
      
      <router-view />

    </div>

    <footer class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <router-link to="/privacy-policy" class="footer-link">Privacy Policy</router-link>
          <router-link to="/terms-of-service" class="footer-link">Terms of Service</router-link>
          <a href="mailto:support@laterladder.com" class="footer-link">Contact Us</a>
        </div>
        <div class="copyright">
          &copy; 2024 Later Ladder. All rights reserved.
        </div>
      </div>
    </footer>

  </div>
</template>

<script>
import NotificationModal from './components/NotificationModal.vue';
import NavBar from './components/NavBar.vue';

export default {
  components: {
    NotificationModal,
    NavBar,
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

.footer {
  background-color: var(--afl-800);
  color: var(--afl-250);
  padding: 1.5em 0;
  text-align: center;
  margin-top: auto;
}

.footer-content {
  font-size: 0.9em;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.footer-links {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 0.5em;
  line-height: 1.25;
}

.footer-link {
  color: var(--afl-250);
  text-decoration: none;
  margin: 0 0.75em;
}

.footer-link:hover {
  text-decoration: underline;
}

.footer-link:visited {
  color: var(--afl-250);
}

.copyright {
  color: var(--afl-250);
  margin-top: 0.5em;
}

@media (max-width: 600px) {

  .content-wrapper {
    margin-bottom: .75em;
  }

  .footer-links {
    flex-direction: column;
    align-items: center;
  }
}

</style>