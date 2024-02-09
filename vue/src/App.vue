<template>
  <div id="app">
    <div id="nav">
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
    </div>
    <router-view />
    <div class="matches-section">
      <MatchesListComp />
    </div>
    <div class="ladder-section">
      <LadderComp />
    </div>
  </div>
</template>

<script>
import MatchesListComp from './components/MatchesListComp';
import LadderComp from './components/LadderComp';

export default {
  components: {
    MatchesListComp,
    LadderComp,
  },
};
</script>


<style>

:root {
  --dark-blue: #031745;
  --dark-blue-rgb: rgb(3, 23, 69);
  --blue: #0C3F91;
  --blue-rgb: rgb(12, 63, 145); 
  --afl-900: #03061C; /* Background primary */
  --afl-800: #031745; /* Nav secondary */
  --afl-700: #01285E; /* Nav primary */
  --afl-600: #004099; /* Main brand color, accent primary */
  --afl-500: #046FD9; 
  --afl-400: #1FB4FF; /* Accent secondary */
  --afl-300: #E8001B; /* Accent tertiary */
  --afl-200: #EDF4F7;
  --afl-100: #F2F4F7;

  --color-orange-dark: rgb(228, 99, 0);
  --color-orange: rgb(248, 137, 7);
  --color-orange-light: rgb(255, 186, 71);
  --color-yellow-dark: rgb(199, 141, 0);
  --color-yellow: rgb(255, 189, 12);
  --color-yellow-light: rgb(255 239 81);
  --color-green-dark: rgb(0, 135, 0);
  --color-green: rgb(22, 176, 25);
  --color-green-light: rgb(104, 235, 99);
  --color-purple-dark: rgb(99, 0, 189);
  --color-purple: rgb(153 63 241);
  --color-purple-light: rgb(207 113 255);
  --color-teal: #00cdc2;

}

body {
background: radial-gradient(circle at center, rgba(0, 64, 153, 1) 0%, rgba(3,23,69,1) 50%, rgba(3, 6, 28, 1) 90%);
background-attachment: fixed;
}

h1 {
  color: var(--afl-100);
}

th {
  color: var(--afl-400);
}

</style>