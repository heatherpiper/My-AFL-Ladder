<template>
  <div id="app">
    <div id="nav" class="nav-bar">
      <h1 class="page-title">Later Ladder</h1>
      <div class="nav-links">
        <router-link class="nav-link" v-bind:to="{ name: 'home' }">Home</router-link>
        <div class="separator">&nbsp;|&nbsp;</div>
        <router-link class="nav-link" v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>

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
  --afl-200: #EDF4F7;
  --afl-100: #F2F4F7;
}

body {
  background: radial-gradient(circle at center, var(--afl-500) 0%, var(--afl-700) 60%, var(--afl-900) 100%);
  background-attachment: fixed;
  font-family: 'Inter', sans-serif;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--afl-800);
  padding: 0.66rem 10px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: right;
  margin-bottom: 20px;
}

.page-title {
  font-family: 'League Gothic', sans-serif;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: var(--afl-200);
  margin: 0;
  padding: 0 10px;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-link {
  color: var(--afl-200);
  text-decoration: none;
  margin: 0 15px;
  font-size: large;
  text-transform: uppercase;
  position: relative;
}

.nav-link:visited {
  color: var(--afl-200);
}

.separator {
  color: var(--afl-500);
}

</style>