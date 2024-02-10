<template>
  <div class="home">
    <div class="page-title">
      <h1>My AFL Ladder</h1>
    </div>
    <MatchesListComp />
    <LadderComp />
  </div>
</template>

<script>
import MatchesListComp from '../components/MatchesListComp';
import LadderComp from '../components/LadderComp';

export default {
  name: "home",
  components: {
    MatchesListComp,
    LadderComp,
  },
};
</script>

<style scoped>
h1 {
  font-family: 'Protest Strike', sans-serif;
}
</style>
