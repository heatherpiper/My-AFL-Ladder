<template>
  <div class="dashboard">
    <LadderComp :userId="userId" ref="ladderComponent" class="width-half ladder"/>
    <GameListComp @watchedStatusChanged="handleGameStatusChanged" @gameStatusChanged="handleGameStatusChanged" class="width-half games"/>
  </div>
</template>

<script>
import GameListComp from '../components/GameListComp';
import LadderComp from '../components/LadderComp';

export default {
  name: "Dashboard",
  components: {
    GameListComp,
    LadderComp,
  },
  computed: {
    userId() {
      return this.$store.state.user.id;
    },
  },
  methods: {
    handleGameStatusChanged() {
      this.$refs.ladderComponent.fetchLadder();
    }
  }
};
</script>

<style scoped>

.dashboard {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.width-half {
  width: 45%;
}

.games, .ladder {
  margin-top: 0;
  padding-top: 0;
}

@media (max-width: 768px) {
  .dashboard {
    flex-direction: column;
  }

  .width-half {
    width: 100%;
  }
}

</style>
