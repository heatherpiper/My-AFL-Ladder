<template>
  <div class="home">
    <div class="home-container">
      <component :is="ladderComponent" :userId="userId" ref="ladderComponent" class="width-half"/>
      <commponent :is="gameListComponent" @recalculateLadder="recalculateLadder" @watchedStatusChanged="handleGameStatusChanged" @gameStatusChanged="handleGameStatusChanged" class="width-half"/>
    </div>
  </div>
</template>

<script>
import GameListComp from '../components/GameListComp';
import LadderComp from '../components/LadderComp';
import GameListGuestComp from '../components/GameListGuestComp';
import LadderGuestComp from '../components/LadderGuestComp';

export default {
  name: "home",
  components: {
    GameListComp,
    LadderComp,
    GameListGuestComp,
    LadderGuestComp,
  },
  computed: {
    userId() {
      return this.$store.state.user.id;
    },
    gameListComponent() {
      return this.userId ? 'GameListComp' : 'GameListGuestComp';
    },
    ladderComponent() {
      return this.userId ? 'LadderComp' : 'LadderGuestComp';
    }
  },
  data() {
    return {
      teamLadder: []
    }
  },
  methods: {
    recalculateLadder() {
      if (this.$refs.ladderComponent && typeof this.$refs.ladderComponent.calculateLadder === 'function') {
        this.$refs.ladderComponent.calculateLadder();
      }
    },
    handleGameStatusChanged() {
      const isGuest = !this.userId;

      if (this.$refs.ladderComponent) {
        if (isGuest && typeof this.$refs.ladderComponent.calculateLadder === 'function') {
          this.$refs.ladderComponent.calculateLadder();
        } else if (!isGuest && typeof this.$refs.ladderComponent.fetchLadder === 'function') {
          this.$refs.ladderComponent.fetchLadder();
        } else {
          console.log('Appropriate ladder update method is not accessible.')
        }
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
