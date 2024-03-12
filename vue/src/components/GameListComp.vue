<template>
  <div class="game-list">
    <h1>GAMES</h1>
    <div class="tabbed-content">
      <div class="tabs-and-round-selection">
        <div class="tabs">
          <button :class="{ active: activeTab === 'unwatched' }" @click="setActiveTab('unwatched')">Unwatched</button>
          <button :class="{ active: activeTab === 'watched' }" @click="setActiveTab('watched')">Watched</button>
        </div>
        <div class="round-selection">
          <select v-model="currentRound">
            <option disabled value="">Select Round</option>
            <option v-for="round in rounds" :key="round" :value="round">
              Round {{ round }}
            </option>
          </select>
        </div>
      </div>
      <div v-if="activeTab">
        <div class="games-container">
          <div class="game-card" v-for="game in currentRoundGames" :key="game.id">
            <div class="vs-container">
              <span class="vs-text">vs</span>
              <div class="team-name">{{ game.hteam }}</div>
              <div class="team-name">{{ game.ateam }}</div>
            </div>
            <div class="image-container" 
              @click.stop="selectGame(game.id)"
              @mouseover="hover = game.id"
              @mouseleave="hover = null">
              <img :src="getImageSrc(game.id)" alt="Action button">
            </div>
        </div>

        </div>
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
       * The games that the user has selected to mark as watched/unwatched
       */
      selectedGames: [],
      /**
       * The round number that the user has selected
       */
      currentRound: '',
      /**
       * Whether to show the confirm button for marking games as watched/unwatched
       */
      showConfirmButton: false,
      /**
       * Whether to show the hover effect for the action button
       */
      hover: null,
      /**
       * The games that are currently being processed as watched/unwatched
       */
      processingGames: [],
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
     * Games grouped by round number
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
     * Get the image source for the action button
     * @param {String} gameId The ID of the game
     * @returns {String} The image source for the action button
     */
    getImageSrc(gameId) {
      const isSelected = this.selectedGames.includes(gameId);
      const isHovering = this.hover === gameId;

      if(this.activeTab === 'unwatched') {
        if (isHovering && !isSelected) return '/checkmark-filled.svg';
        return isSelected ? '/checkmark-filled.svg' : 'checkmark-greyed.svg'
      } else {
        if (isHovering && !isSelected) return '/remove-filled.svg';
        return isSelected ? '/remove-filled.svg' : '/remove-greyed.svg'
      }
    },
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
     * Select a game to mark as watched/unwatched
     * @param {String} gameId The ID of the game to select
     * @param {Event} event The event object
     */
    selectGame(gameId) {
      this.processingGames.push(gameId);

      this.$nextTick(() => {
        setTimeout(() => {
          const operation = this.activeTab === 'unwatched' ? 'add' : 'remove';
          const serviceMethod = operation === 'add' ? WatchedGamesService.addGamesToWatchedList : WatchedGamesService.removeGamesFromWatchedList;

          serviceMethod(this.$store.state.user.id, gameId)
            .then(() => {
              this.moveGameBetweenLists(gameId, operation);
              this.$emit('gameStatusChanged', { gameId: gameId, operation: operation });
            })
            .catch(error => {
              console.error(`Error marking game as ${operation === 'add' ? 'watched' : 'unwatched'}:`, error);
            })
            .finally(() => {
              const index = this.processingGames.indexOf(gameId);
              if (index > -1) {
                this.processingGames.splice(index, 1);
              }
            });
        }, 500);
      });
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
      console.log(`Marking game with ID: ${gameId} as watched`);
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
      console.log(`Unwatched games: ${JSON.stringify(this.unwatchedGames)}`);
      console.log(`Watched games: ${JSON.stringify(this.watchedGames)}`);
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
  background-color: var(--afl-800);
  margin: auto;
  padding-left: 16px;
  border-radius: 8px;
}
.game-list {
  padding: 16px;
  color: var(--afl-900);
}

.tabbed-content {
  background-color: var(--afl-800);
  margin: 10px auto;
  padding: 10px;
  border-radius: 8px;
}

.tabs-and-round-selection {
  display: flex;
  justify-content: space-between;
  margin-right: 6px;
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
  font-size: 1rem;
}

button:hover, button.active {
  background-color: var(--afl-500);
  color: var(--afl-100);
}

.tabs button {
  border-radius: 8px 8px 0 0;
}

.confirm-button {
  background-color: var(--afl-500);
  color: var(--afl-100)
}

.confirm-button:hover {
  background-color: var(--afl-400);
}

.round-selection select {
  background-color: var(--afl-200);
  color: var(--afl-900);
  margin-left: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.games-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  justify-content: center;
  grid-gap: 16px; 
  padding: 8px;
}

.game-card {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: var(--afl-600);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  color: var(--afl-200);
  min-height: 5em;
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

.image-container {
  position: absolute;
  bottom: 10px;
  right: 10px;
  width: 36px;
  height: 36px;
}

@media (max-width: 768px) {
  .games-container {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

</style>
  