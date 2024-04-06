<template>
  <div class="guest-dashboard">
    <transition name="fade">
      <div v-if="showGuestNotice" class="guest-notice">
        <button class="close-button" @click="dismissGuestNotice">&times;</button>
        <p>Your watched games and ladder standings will not be saved without an account. Please <router-link to="/login">log in</router-link> or <router-link to="/register">register</router-link> if you wish to save your data.</p>
      </div>
    </transition>
    <div class="ladder-and-games">
      <LadderGuestComp class="width-half ladder"/>
      <GameListGuestComp class="width-half games" @recalculateLadder="recalculateLadder"/>
    </div>
  </div>
</template>
  
<script>
import GameListGuestComp from '../components/GameListGuestComp';
import LadderGuestComp from '../components/LadderGuestComp';
  
export default {
  name: "GuestDashboard",
  components: {
    GameListGuestComp,
    LadderGuestComp,
  },
  data() {
    return {
      showGuestNotice: true
    }
  },
  methods: {
    dismissGuestNotice() {
      this.showGuestNotice = false;
    },
    recalculateLadder() {
      this.$emit('recalculateLadder');
    }
  },
};
</script>

<style scoped>

.guest-notice {
  position: relative;
  text-align: center;
  margin: 0 auto 1.5em;
  background-color: var(--afl-200);
  color: var(--afl-900);
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

.ladder-and-games {
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

  .guest-notice {
    width: 100%;
    font-size: 0.9em;
  }

  .ladder-and-games {
    flex-direction: column;
  }

  .width-half {
    width: 100%;
  }

  .close-button {
    margin: 2px 4px 1px 1px;
  }
}

</style>