<template>
  <div class="game-list">
    <h1>Games</h1>
    <div class="tabs">
      <button :class="{ active: activeTab === 'unwatched' }" @click="setActiveTab('unwatched')">Unwatched</button>
      <button :class="{ active: activeTab === 'watched' }" @click="setActiveTab('watched')">Watched</button>
    </div>
    <div v-if="activeTab">
      <div @click="toggleCheckboxes" class="mark-as-toggle">
        Mark games as {{ activeTab === 'unwatched' ? 'watched' : 'unwatched' }}
      </div>
      <div class="games-container">
        <div class="game-card" v-for="game in currentGames" :key="game.id">
          {{ game.hteam }} vs {{ game.ateam }}
          <input type="checkbox" v-if="showCheckboxes" :id="`${activeTab}-${game.id}`" @change="selectGame(game.id, $event)">
          <label v-if="showCheckboxes" :for="`${activeTab}-${game.id}`">{{ activeTab === 'unwatched' ? 'Watched' : 'Unwatched' }}</label>
        </div>
      </div>
      <button v-if="showCheckboxes" @click="confirmSelection">Confirm</button>
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
      showCheckboxes: false,
      selectedGames: new Set(),
    };
  },
  computed: {
    currentGames() {
      return this.activeTab === 'unwatched' ? this.unwatchedGames : this.watchedGames;
    }
  },
  methods: {
    setActiveTab(tab) {
      this.activeTab = tab;
      this.showCheckboxes = false; // Reset checkboxes visibility when tab changes
    },
    toggleCheckboxes() {
      this.showCheckboxes = !this.showCheckboxes;
    },
    selectGame(gameId, event) {
      if (event.target.checked) {
        this.selectedGames.add(gameId);
      } else {
        this.selectedGames.delete(gameId);
      }
    },
    confirmSelection() {
      const operation = this.activeTab === 'unwatched' ? 'add' : 'remove';
      Array.from(this.selectedGames).forEach(gameId => {
        const serviceMethod = operation === 'add' ? WatchedGamesService.addGameToWatchedList : WatchedGamesService.removeGameFromWatchedList;
        serviceMethod(this.$store.state.user.id, gameId)
          .then(() => {
            this.moveGameBetweenLists(gameId, operation);
          })
          .catch(error => {
            console.error(`Error marking game as ${operation === 'add' ? 'watched' : 'unwatched'}:`, error);
          });
      });
      this.selectedGames.clear();
      this.showCheckboxes = false;
    },
    moveGameBetweenLists(gameId, operation) {
      const sourceList = operation === 'add' ? this.unwatchedGames : this.watchedGames;
      const targetList = operation === 'add' ? this.watchedGames : this.unwatchedGames;
      const gameIndex = sourceList.findIndex(game => game.id === gameId);
      if (gameIndex !== -1) {
        const [game] = sourceList.splice(gameIndex, 1);
        targetList.push(game);
      }
    },
    fetchGames() {
      this.fetchUnwatchedGames();
      this.fetchWatchedGames();
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
  