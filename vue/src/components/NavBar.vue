<template>
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
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
    name: 'NavBar',
    components: {},
    computed: {
        ...mapGetters(['isDarkMode']),
    },
    methods: {
        ...mapActions(['toggleMode']),
    },
};
</script>

<style scoped>

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--afl-800);
  padding: 8px;
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
  display: none; /* Hide the mode toggle until light/dark modes are done */
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