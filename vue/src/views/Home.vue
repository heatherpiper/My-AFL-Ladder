<template>
  <div class="home">
    <div class="page-title">
      <h2>My AFL Ladder</h2>
    </div>
    <div class="home-container">
      <GameListComp class="half-width"/>
      <LadderComp :userId="userId" class="half-width"/>
    </div>
  </div>
</template>

<script>
import GameListComp from '../components/GameListComp';
import LadderComp from '../components/LadderComp';

export default {
  name: "home",
  components: {
    GameListComp,
    LadderComp,
  },
  computed: {
    userId() {
      return this.$store.state.user.id;
    }
  },
  data() {
    return {
      teamLadder: []
    }
  }
};
</script>

<style scoped>
.home-container {
  display: flex;
  justify-content: space-between;
}

.half-width {
  width: 50%;
}
</style>
