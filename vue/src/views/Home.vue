<template>
  <div class="home">
    <transition name="fade">
      <div v-if="showGuestNotice && !userId" class="guest-notice">
        <button class="close-button" @click="dismissGuestNotice">&times;</button>
        <p>Your watched games and ladder standings will not be saved without an account. Please <router-link to="/login">log in</router-link> or <router-link to="/register">register</router-link> if you wish to save your data.</p>
      </div>
    </transition>
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
      teamLadder: [], 
      showGuestNotice: true
    }
  },
  methods: {
    dismissGuestNotice() {
      this.showGuestNotice = false;
    },
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

.guest-notice {
  position: relative;
  text-align: center;
  margin: 1.5em auto;
  background-color: var(--afl-200);
  color: var(--afl-800);
  border: 1px solid var(--afl-800);
  border-radius: 12px;
  width: 80%;
  box-sizing: border-box;
  box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.1), 0 6px 6px 0 rgba(0, 0, 0, 0.1);
}

.guest-notice a {
  color: var(--afl-500);
  text-decoration-line: none;
}

.guest-notice a:visited {
  color: var(--afl-600);
}

.guest-notice a:hover {
  text-decoration-line: underline;
  cursor: pointer;
}

.close-button {
  float: right;
  font-size: large;
  font-weight: bold;
  background: none;
  border: none;
  color: var(--afl-800);
  cursor: pointer;
  margin: 2px 8px;
}

.guest-notice p {
  padding: 8px;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.25s;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .home-container {
    flex-direction: column;
  }

  .width-half {
    width: 100%;
  }

  .guest-notice {
    width: 100%;
    font-size: small;
  }

  .close-button {
    margin: 2px 4px 1px 1px;
  }
}

</style>
