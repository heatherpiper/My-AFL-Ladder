<template>
    <div>
      <h1>Games</h1>
      <ul>
        <li v-for="game in games" :key="game.id">
          {{ game.hteam }} vs {{ game.ateam }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  import GameService from '../services/GameService';

  export default {
    name: "games",
    data() {
      return {
        games: [],
      };
    },
    mounted() {
        GameService.getAllGames('/games').then(response => {
            console.log(response.data);
            this.games = response.data;
        })
        .catch(error => {
            console.error('Error fetching games:', error);
        });
    },
  };
</script>
  
<style scoped>

</style>
  