<template>
    <div id="nav" class="nav-bar">
        <router-link to="/" class="site-title-link">
          <img src="@/assets/LaterLadder-Wordmark.webp" alt="Later Ladder title" class="site-title">
        </router-link>
        <div class="nav-links-container" :class="{ 'show-menu': showMenu }">
          <div class="nav-links">
            <div class="nav-links-inner">
              <router-link class="nav-link" to="/admin" v-if="isAdmin">Admin Panel</router-link>
              <router-link class="nav-link" v-bind:to="{ name: 'about' }" v-if="$route.path !== '/about'">About</router-link>
              <router-link class="nav-link" v-bind:to="{ name: 'login' }" v-if="$store.state.token === ''">Login</router-link>
              <router-link class="nav-link" v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
              <button class="mode-toggle" @click="toggleMode">
                <i class="fas" :class="{ 'fa-sun': isDarkMode, 'fa-moon': !isDarkMode }"></i>
              </button>
            </div>
          </div>
        </div>
        <button class="hamburger" @click="toggleMenu">&#9776;</button>
    </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
    name: 'NavBar',
    components: {},
    data() {
      return {
        showMenu: false
      };
    },
    computed: {
        ...mapGetters(['isDarkMode']),
        ...mapGetters(['isAdmin']),
    },
    methods: {
        ...mapActions(['toggleMode']),
        toggleMenu() {
          this.showMenu = !this.showMenu;
        }
    },
};
</script>

<style scoped>

.nav-bar {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--afl-800);
  padding: 8px 16px;
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

.nav-links-container {
  display: none;
  position: absolute;
  top: 100%;
  right: 16px;
  min-width: 200px;
}

.nav-links-inner {
  background-color: var(--afl-800);
  padding: 16px 32px 16px 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--afl-700);
  border-radius: 4px;
}

.nav-links-container.show-menu {
  display: block;
}

.nav-links {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.nav-link {
  display: block;
  padding: 8px 16px;
  color: var(--afl-200);
  text-decoration: none;
  transition: background-color 0.3s;
  width: 100%;
}

.nav-link:visited {
  color: var(--afl-200);
}

.nav-link:hover {
  background-color: var(--afl-600);
}

.hamburger {
  display: block;
  background: none;
  border: none;
  color: var(--afl-200);
  cursor: pointer;
  font-size: 1.5rem;
  margin-left: 2rem;
  outline: none;
  transition: color 0.3s;
}

.hamburger:hover {
  color: var(--afl-450);
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