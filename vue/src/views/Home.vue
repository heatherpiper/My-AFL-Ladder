<template>
  <div class="home">
    <div class="home-container">
      <LadderComp :userId="userId" ref="ladderComponent" class="width-smaller"/>
      <GameListComp @gameStatusChanged="handleGameStatusChanged" class="width-larger"/>
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

.width-smaller {
  width: 45%;
}

.width-larger {
  width: 55%;
}

.game-list, .ladder {
  margin-top: 0;
  padding-top: 0;
}
</style>
