<template>
  <div class="home">
    <div class="page-title">
      <h2>My AFL Ladder</h2>
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

</style>
