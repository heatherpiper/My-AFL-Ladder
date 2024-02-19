<template>
  <div class="home">
    <div class="page-title">
      <h2>My AFL Ladder</h2>
    </div>
    <GamesListComp />
    <LadderComp />
  </div>
</template>

<script>
import GamesListComp from '../components/GamesListComp';
import LadderComp from '../components/LadderComp';

export default {
  name: "home",
  components: {
    GamesListComp,
    LadderComp,
  },
};
</script>

<style scoped>

</style>
