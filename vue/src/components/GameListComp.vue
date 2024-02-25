<template>
  <div class="game-list">
    <h1>Games</h1>
    <div class="games-container">
      <div class="game-card" v-for="game in games" :key="game.id">
        {{ game.hteam }} vs {{ game.ateam }}
        <input type="checkbox" :id="'watched-' + game.id" @change="markAsWatched(game.id, $event)">
        <label :for="'watched-' + game.id">Watched</label>
      </div>
    </div>
  </div>
</template>

  
<script>
  import GameService from '../services/GameService';
  import WatchedGamesService from '../services/WatchedGamesService';

  export default {
    name: "games",
    data() {
      return {
        games: [],
      };
    },
    methods: {
      markAsWatched(gameId, event) {
        const userId = this.$store.state.user.id;
        if (userId && event.target.checked) {
          WatchedGamesService.addGameToWatchedList(userId, gameId)
            .then(() => {
              console.log('Game marked as watched');
            })
            .catch(error => {
              console.error('Error marking game as watched:', error);
            });
        } else {
          console.error('User ID is undefined or checkbox is not checked.');
        }
      }
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
.game-list {
  max-width: 100%;
  padding: 16px;
}

.games-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* Creates a 2-column layout */
  grid-gap: 16px; /* Space between cards */
  max-height: calc(3 * 120px); /* Adjust based on your card size */
  overflow-y: auto; /* Enables vertical scrolling */
  padding: 8px;
}

.game-card {
  color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
  padding: 16px;
  background-color: #03061C;
  border-radius: 8px;
}
</style>
  