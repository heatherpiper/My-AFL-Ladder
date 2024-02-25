<template>
  <div class="game-list">
    <h1>Games</h1>
    <div class="tabs">
      <button :class="{ active: activeTab === 'unwatched' }" @click="activeTab = 'unwatched'; showCheckboxes = false">Unwatched</button>
      <button :class="{ active: activeTab === 'watched' }" @click="activeTab = 'watched'; showCheckboxes = false">Watched</button>
    </div>
    <div v-if="activeTab === 'unwatched'">
      <div @click="showCheckboxesUnwatched = !showCheckboxesUnwatched" class="mark-as-watched">
        Mark games as watched
      </div>
      <div class="games-container">
        <div class="game-card" v-for="game in unwatchedGames" :key="game.id">
          {{ game.hteam }} vs {{ game.ateam }}
          <input type="checkbox" v-if="showCheckboxesUnwatched" :id="'watched-' + game.id" @change="selectGame(game.id, $event)">
          <label v-if="showCheckboxesUnwatched" :for="'watched-' + game.id">Watched</label>
        </div>
      </div>
      <button v-if="showCheckboxesUnwatched" @click="confirmWatched">Confirm</button>
    </div>
    <div class="games-container" v-if="activeTab === 'watched'">
      <div @click="toggleCheckboxesForWatched" class="mark-as-unwatched">
        Mark games as unwatched
      </div>
      <div class="game-card" v-for="game in watchedGames" :key="game.id">
        {{ game.hteam }} vs {{ game.ateam }}
        <input type="checkbox" v-if="showCheckboxesWatched" :id="'unwatched-' + game.id" @change="selectGame(game.id, $event)">
        <label v-if="showCheckboxesWatched" :for="'unwatched-' + game.id">Unwatched</label>
      </div>
      <button v-if="showCheckboxesWatched" @click="confirmUnwatched">Confirm</button>
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
        showCheckboxesUnwatched: false,
        showCheckboxesWatched: false,
        selectedGamesForUnwatched: new Set(),
        selectedGamesForWatched: new Set(),
      };
    },
    methods: {
      toggleCheckboxesForWatched() {
        this.showCheckboxesWatched = !this.showCheckboxesWatched;
      },
      selectGame(gameId, event) {
        const targetSet = this.activeTab === 'unwatched' ? this.selectedGamesForUnwatched : this.selectedGamesForWatched;
        if (event.target.checked) {
          targetSet.add(gameId);
        } else {
          targetSet.delete(gameId);
        }
      },
      confirmWatched() {
        const userId = this.$store.state.user.id;
        Array.from(this.selectedGamesForUnwatched).forEach(gameId => {
          WatchedGamesService.addGameToWatchedList(userId, gameId)
            .then(() => {
              const gameIndex = this.unwatchedGames.findIndex(game => game.id === gameId);
              if (gameIndex !== -1) {
                const [game] = this.unwatchedGames.splice(gameIndex, 1);
                this.watchedGames.push(game);
              }
            })
            .catch(error => {
              console.error('Error marking game as watched:', error);
            });
        });
        this.selectedGamesForUnwatched.clear(); 
        this.showCheckboxesUnwatched = false;
      },
      confirmUnwatched() {
        const userId = this.$store.state.user.id;
        Array.from(this.selectedGamesForWatched).forEach(gameId => {
          WatchedGamesService.removeGameFromWatchedList(userId, gameId)
            .then(() => {
              const gameIndex = this.watchedGames.findIndex(game => game.id === gameId);
              if (gameIndex !== -1) {
                const [game] = this.watchedGames.splice(gameIndex, 1);
                this.unwatchedGames.push(game);
              }
            })
            .catch(error => {
              console.error('Error marking game as unwatched:', error);
            });
        });
        this.selectedGamesForWatched.clear();
        this.showCheckboxesWatched = false;
      },
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
  