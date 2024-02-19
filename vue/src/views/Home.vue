<template>
  <div class="home">
    <div class="page-title">
      <h2>My AFL Ladder</h2>
    </div>
    <GameListComp />
    <LadderComp :userId="userId" />
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
  created() {
    const userId = this.$store.state.user.id;
    fetch(`http://localhost:8080/ladder/${userId}`)
    .then(response => response.json())
    .then(data => this.teamLadder = data)
    .catch(error => console.error('Error: ', error));
  }
};
</script>

<style scoped>

</style>
