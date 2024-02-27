<template>
  <div class="home">
    <div class="page-title">
      <h2>
        LATER LADDER
      </h2>
    </div>
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

h2 {
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
  font-family: 'League Gothic', sans-serif;
  font-size: xx-large;
  line-height: 0.2;
  padding-left: 16px;
}
</style>
