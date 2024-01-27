<template>
  <div id="app">
    <div id="nav">
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
    </div>
    <router-view />
    <div class="matches-section">
      <MatchesComp />
    </div>
    <div class="ladder-section">
      <LadderComp />
    </div>
  </div>
</template>

<script>
import MatchesComp from './components/MatchesComp.vue';

export default {
  components: {
    MatchesComp,
    LadderComp,
  },
};
</script>


<style>

/* global styles go here*/

</style>