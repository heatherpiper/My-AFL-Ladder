<template>
  <div class="game-list">
    <h1>GAMES</h1>
    <div class="tabbed-content">
      <div class="tabs">
        <button :class="{ active: activeTab === 'unwatched' }" @click="setActiveTab('unwatched')">Unwatched</button>
        <button :class="{ active: activeTab === 'watched' }" @click="setActiveTab('watched')">Watched</button>
      </div>
      <div v-if="activeTab">
        <div @click="toggleCheckboxes" class="mark-as-toggle">
          Mark games as {{ activeTab === 'unwatched' ? 'watched' : 'unwatched' }}
        </div>

        <div class="round-selection">
          <select v-model="currentRound">
            <option disabled value="">Select Round</option>
            <option v-for="round in rounds" :key="round" :value="round">
              Round {{ round }}
            </option>
          </select>
        </div>

        <div class="games-container">
          <div class="game-card" v-for="game in currentRoundGames" :key="game.id">
            <div class="vs-container">
              <span class="vs-text">vs</span>
              <div class="team-name">{{ game.hteam }}</div>
              <div class="team-name">{{ game.ateam }}</div>
            </div>
            <div v-if="showCheckboxes" class="game-checkbox">
              <input type="checkbox" :id="`${activeTab}-${game.id}`" @change="selectGame(game.id, $event)">
              <label :for="`${activeTab}-${game.id}`">{{ activeTab === 'unwatched' ? 'Watched' : 'Unwatched' }}</label>
          </div>
        </div>

        </div>
        <button v-if="showCheckboxes" @click="confirmSelection" class="confirm-button">Confirm</button>
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
      /**
       * The games that the user has not watched yet
       */
      unwatchedGames: [],
      /**
       * The games that the user has already watched
       */
      watchedGames: [],
      /**
       * The currently active tab (unwatched or watched)
       */
      activeTab: 'unwatched',
      /**
       * Whether to show checkboxes for marking games as watched/unwatched
       */
      showCheckboxes: false,
      /**
       * The games that the user has selected to mark as watched/unwatched
       */
      selectedGames: new Set(),
      /**
       * The round number that the user has selected
       */
      currentRound: '',
    };
  },
  computed: {
    /**
     * The round numbers for available games, filtered for duplicates and sorted
     * @returns {Array} An array of round numbers
     */
    rounds() {
      const rounds = this.currentGames.map(game => game.round);
      return Array.from(new Set(rounds)).sort((a, b) => a - b); 
    },
    /**
     * The games grouped by round number
     * @returns {Object} An object with round numbers as keys and games for each round as values
     */
    gamesByRound() {
      return this.rounds.reduce((round_object, round) => {
        round_object[round] = this.currentGames.filter(game => game.round === round); // Create an object with round numbers as keys and games for each round as values
        return round_object;
      }, {});
    },
    /**
     * The games for the currently selected round
     * @returns {Array} An array of games for the current round, returns an empty array if currentRound is not set
     */
    currentRoundGames() {
      if(!this.currentRound) {
        return [];
      }
      return this.gamesByRound[this.currentRound] || []; 
    },
    /**
     * The games for the currently active tab
     * @returns {Array} An array of games for the active tab, either unwatched or watched
     */
    currentGames() {
      return this.activeTab === 'unwatched' ? this.unwatchedGames : this.watchedGames;
    }
  },
  methods: {
    /**
     * Set the active round number
     * @param {String} round The round number to set as active
     */
    setActiveRound(round) {
      this.currentRound = round;
    },
    /**
     * Set the active tab, reset checkbox visibility
     * @param {String} tab The tab to set as active, either 'unwatched' or 'watched'
     */
    setActiveTab(tab) {
      this.activeTab = tab;
      this.showCheckboxes = false;
    },
    /**
     * Toggle the visibility of checkboxes for marking games as watched/unwatched
     */
    toggleCheckboxes() {
      this.showCheckboxes = !this.showCheckboxes;
    },
    /**
     * Select a game to mark as watched/unwatched
     * @param {String} gameId The ID of the game to select
     * @param {Event} event The event object
     */
    selectGame(gameId, event) {
      if (event.target.checked) {
        this.selectedGames.add(gameId);
      } else {
        this.selectedGames.delete(gameId);
      }
    },
    /**
     * Mark the selected games as watched/unwatched
     */
    confirmSelection() {
      const operation = this.activeTab === 'unwatched' ? 'add' : 'remove';
      Array.from(this.selectedGames).forEach(gameId => {
        const serviceMethod = operation === 'add' ? WatchedGamesService.addGamesToWatchedList : WatchedGamesService.removeGamesFromWatchedList;
        serviceMethod(this.$store.state.user.id, gameId)
          .then(() => {
            this.moveGameBetweenLists(gameId, operation);
            console.log('Emitting gameStatusChanged event for gameId:', gameId);
            this.$emit('gameStatusChanged', { gameId: gameId, operation: operation });
          })
          .catch(error => {
            console.error(`Error marking game as ${operation === 'add' ? 'watched' : 'unwatched'}:`, error);
          });
      });
      this.selectedGames.clear();
      this.showCheckboxes = false;
    },
    /**
     * Move a game between the watched and unwatched lists
     * @param {String} gameId The ID of the game to move
     * @param {String} operation The operation to perform, either 'add' or 'remove'
     */
    moveGameBetweenLists(gameId, operation) {
      const sourceList = operation === 'add' ? this.unwatchedGames : this.watchedGames;
      const targetList = operation === 'add' ? this.watchedGames : this.unwatchedGames;
      const gameIndex = sourceList.findIndex(game => game.id === gameId);
      if (gameIndex !== -1) {
        const [game] = sourceList.splice(gameIndex, 1);
        targetList.push(game);
      }
    },
    /**
     * Fetch the games for the unwatched and watched tabs
     */
    fetchGames() {
      this.fetchUnwatchedGames();
      this.fetchWatchedGames();
    },
    /**
     * Fetch the games that the user has watched
     */
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
    /**
     * Fetch the games that the user has not watched yet
     */
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
    /**
     * Mark a game as watched or unwatched
     * @param {String} gameId The ID of the game to mark
     * @param {Event} event The event object
     */
    markAsWatched(gameId, event) {
      const userId = this.$store.state.user.id;
      if (userId) {
        if (event.target.checked) {
          WatchedGamesService.addGamesToWatchedList(userId, gameId)
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
          WatchedGamesService.removeGamesFromWatchedList(userId, gameId)
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
    // Fetch games when the component is mounted
      this.fetchGames();
    // Set the active round when the component is mounted
      this.$nextTick(() => {
        if (this.rounds.length > 0) {
          this.setActiveRound(this.rounds[0]);
        }
      });
    }
  };
</script>
  
<style scoped>

h1 {
  font-family: 'League Gothic', sans-serif;
  font-size: xxx-large;
  text-transform: uppercase;
  letter-spacing: 2px;
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
}
.game-list {
  max-width: 100%;
  padding: 16px;
  color: var(--afl-900);
}

.tabbed-content {
  background-color: var(--afl-800);
  padding: 10px;
  border-radius: 8px;
}

button {
  background-color: var(--afl-600);
  color: var(--afl-200);
  border: 1px solid var(--afl-900);
  padding: 10px 20px;
  margin: 0 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
  border-radius: 6px;
  font-family: 'Inter', sans-serif;
  font-size: medium;
}

button:hover, button.active {
  background-color: var(--afl-500);
  color: var(--afl-100);
}

.tabs button {
  border-radius: 6px 6px 0 0;
}

select {
  margin-left: 10px;
  padding: 10px;
  border: 1px solid var(--afl-900);
  border-radius: 6px;
  font-size: medium;
}

.games-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  justify-content: center;
  grid-gap: 16px; 
  padding: 8px;
}

.game-card {
  background-color: var(--afl-600);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  color: var(--afl-200);
}

.vs-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  padding-left: 30px
}

.vs-text {
  position: absolute;
  left: 0px;
  top: 50%;
  transform: translateY(-50%);
  font-weight: bold;
  color: var(--afl-400);
}

.team-name {
  width: 100%;
  text-align: left;
  margin: 4px 0;
  font-weight: 900;
}

.game-checkbox {
  display: flex;
  align-items: center;
  margin-top: 8px;
}

input[type="checkbox"] {
  margin-right: 5px;
}

.mark-as-toggle {
  cursor: pointer;
  background-color: var(--afl-500);
  color: var(--afl-100);
  margin: 20px 0;
  padding: 10px;
  text-align: center;
  border-radius: 6px;
  transition: background-color 0.3s ease
}

.mark-as-toggle:hover {
  background-color: var(--afl-450);
}

@media (max-width: 768px) {
  .games-container {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

</style>
  