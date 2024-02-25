<template>
  <div class="game-list">
    <h1>Games</h1>
    <div class="tabs">
      <button :class="{ active: activeTab === 'unwatched' }" @click="activeTab = 'unwatched'">Unwatched</button>
      <button :class="{ active: activeTab === 'watched' }" @click="activeTab = 'watched'">Watched</button>
    </div>
    <div class="games-container" v-if="activeTab === 'unwatched'">
      <div class="game-card" v-for="game in unwatchedGames" :key="game.id">
        {{ game.hteam }} vs {{ game.ateam }}
        <input type="checkbox" :id="'watched-' + game.id" @change="markAsWatched(game.id, $event)">
        <label :for="'watched-' + game.id">Watched</label>
      </div>
    </div>
    <div class="games-container" v-if="activeTab === 'watched'">
      <div class="game-card" v-for="game in watchedGames" :key="game.id">
        {{ game.hteam }} vs {{ game.ateam }}
        <input type="checkbox" :id="'watched-' + game.id" @change="markAsWatched(game.id, $event)" checked>
        <label :for="'watched-' + game.id">Watched</label>
      </div>
    </div>
  </div>
</template>

  
<script>
  import WatchedGamesService from '../services/WatchedGamesService';

  export default {
    name: "GameListComp",
    data() {
      return {
        unwatchedGames: [],
        watchedGames: [],
        activeTab: 'unwatched',
      };
    },
    methods: {
      fetchWatchedGames() {
        const userId = this.$store.state.user.id;
        WatchedGamesService.getWatchedGames(userId)
          .then(response => {
            this.watchedGames = response.data;
          })
          .catch(error => {
            console.error('Error fetching watched games:', error);
          });
      },
      fetchUnwatchedGames() {
        const userId = this.$store.state.user.id;
        WatchedGamesService.getUnwatchedGames(userId)
          .then(response => {
            this.unwatchedGames = response.data;
          })
          .catch(error => {
            console.error('Error fetching unwatched games:', error);
          });
      },
      markAsWatched(gameId, event) {
        const userId = this.$store.state.user.id;
        if (userId) {
          if (event.target.checked) {
            WatchedGamesService.addGameToWatchedList(userId, gameId)
              .then(() => {
                console.log('Game marked as watched');
                const gameIndex = this.unwatchedGames.findIndex(game => game.id === gameId);
                if (gameIndex !== -1) {
                  const [game] = this.unwatchedGames.splice(gameIndex, 1);
                  this.watchedGames.push(game);
                }
              })
              .catch(error => {
                console.error('Error marking game as watched:', error);
              });
          } else {
            WatchedGamesService.removeGameFromWatchedList(userId, gameId)
              .then(() => {
                console.log('Game marked as unwatched');
                const gameIndex = this.watchedGames.findIndex(game => game.id === gameId);
                if (gameIndex !== -1) {
                  const [game] = this.watchedGames.splice(gameIndex, 1);
                  this.unwatchedGames.push(game);
                }
              })
              .catch(error => {
                console.error('Error marking game as unwatched:', error);
              });
          }
        } else {
          console.error('User ID is undefined.');
        }
      }
    },
    mounted() {
        this.fetchWatchedGames();
        this.fetchUnwatchedGames();
    }
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

.active {
  background-color: aqua;
}
</style>
  