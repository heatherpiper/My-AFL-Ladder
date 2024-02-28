<template>
  <div class="home">
    <div class="home-container">
      <GameListComp @gameStatusChanged="handleGameStatusChanged" class="half-width"/>
      <LadderComp :userId="userId" ref="ladderComponent" class="half-width"/>
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
  },
  methods: {
    handleGameStatusChanged() {
      console.log(this.$refs);
      if (this.$refs.ladderComponent) {
        this.$refs.ladderComponent.fetchLadder();
      } else {
        console.log('Ladder component is not accessible.');
      }
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

.game-list, .ladder {
  margin-top: 0;
  padding-top: 0;
}
</style>
