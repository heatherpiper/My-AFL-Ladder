<template>
  <div class="home">
    <div class="home-container">
      <LadderComp :userId="userId" ref="ladderComponent" class="width-half"/>
      <GameListComp @gameStatusChanged="handleGameStatusChanged" class="width-half"/>
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
  flex-direction: row;
  justify-content: space-between;
}

.width-half {
  width: 45%;
}

.game-list, .ladder {
  margin-top: 0;
  padding-top: 0;
}

@media (max-width: 768px) {
  .home-container {
    flex-direction: column;
  }
  .width-half {
    width: 100%;
  }
}

</style>
