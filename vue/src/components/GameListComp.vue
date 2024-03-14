<template>
  <div class="game-list">

    <div class="header-section">
      <h1>Games</h1>
      <div class="round-selection">
          <select v-model="currentRound">
            <option disabled value="">Select Round</option>
            <option v-for="round in rounds" :key="round" :value="round">
              Round {{ round }}
            </option>
          </select>
      </div>
    </div>

    <div class="games-section">
      <div class="section-container">
        <h2 class="section-header">Unwatched Games</h2>
        <div class="games-container">
          <div class="game-card game-card-transition" 
            v-for="game in filteredUnwatchedGames"
            :key="game.id"
            :class="{'game-card-hide': processingGames.includes(game.id)}"
            @click.stop="selectGame(game.id)"
            @mouseover="hover = game.id"
            @mouseleave="hover = null">
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

      <div class="section-container">
        <h2 class="section-header">Watched Games</h2>
        <div class="games-container">
          <div class="game-card game-card-transition" 
            v-for="game in filteredWatchedGames"
            :key="game.id"
            :class="{'game-card-hide': processingGames.includes(game.id)}"
            @click.stop="selectGame(game.id)"
            @mouseover="hover = game.id"
            @mouseleave="hover = null">
            <div class="vs-container">
              <span class="vs-text">vs</span>
              <div class="team-name">{{ game.hteam }}</div>
              <div class="team-name">{{ game.ateam }}</div>
            </div>
            <div class="image-container"
                 @click.stop="selectGame(game.id)"
                 @mouseover="hover = game.id"
                 @mouseleave="hover = null">
              <img :src="getImageSrc(game.id, true)" alt="Action button">
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
       * The games that the user has marked as watched
       */
      watchedGames: [],
      /**
       * The games that the user has selected to mark as watched/unwatched
       */
      selectedGames: [],
      /**
       * The round number that the user has selected
       */
      currentRound: '',
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
      const allGames = [...this.unwatchedGames, ...this.watchedGames];
      const rounds = allGames.map(game => game.round);
      return Array.from(new Set(rounds)).sort((a, b) => a - b); 
    },

    filteredUnwatchedGames() {
      if (!this.currentRound) return [];
      const currentRoundNumber = Number(this.currentRound);
      return this.unwatchedGames.filter(game => Number(game.round) === currentRoundNumber);
    },
    filteredWatchedGames() {
      if (!this.currentRound) return [];
      const currentRoundNumber = Number(this.currentRound);
      return this.watchedGames.filter(game => Number(game.round) === currentRoundNumber);
    }
  },
  methods: {
    /**
     * Get the image source for the action button
     * @param {String} gameId The ID of the game
     * @returns {String} The image source for the action button
     */
    getImageSrc(gameId) {
      const isHovering = this.hover === gameId;
      const isWatched = this.watchedGames.some(game => game.id === gameId);
      const isProcessing = this.processingGames.includes(gameId);

      if (isWatched) {
        if (isHovering && !isProcessing) {
          return '/checkmark-hover.svg';
        } else {
          return '/checkmark-filled.svg';
        }
      } else {
        if (isHovering && !isProcessing) {
          return '/checkmark-hover.svg';
        } else {
          return '/checkmark-greyed.svg';
        }
      }
    },
    /**
     * Select a game to mark as watched/unwatched
     * @param {String} gameId The ID of the game to select
     * @param {Event} event The event object
     */
     selectGame(gameId) {
      const isWatched = this.watchedGames.findIndex(game => game.id === gameId) !== -1;
      const operation = isWatched ? 'remove' : 'add';
      if (!this.selectedGames.includes(gameId)) {
        this.selectedGames.push(gameId);
      }

      const serviceMethod = operation === 'add' ? WatchedGamesService.addGamesToWatchedList : WatchedGamesService.removeGamesFromWatchedList;
      
      this.processingGames.push(gameId);
      serviceMethod(this.$store.state.user.id, gameId)
      .then(() => {
        setTimeout(() => {
          this.moveGameBetweenLists(gameId, operation);
          const index = this.processingGames.indexOf(gameId);
          if (index > -1) {
            this.processingGames.splice(index, 1);
          }
        }, 100);
        this.$emit('gameStatusChanged', { gameId: gameId, operation: operation });
      })
      .catch(error => {
        console.error(`Error marking game as ${operation}:`, error);
        const index = this.processingGames.indexOf(gameId);
        if (index > -1) {
          this.processingGames.splice(index, 1);
        }
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
      return Promise.all([this.fetchUnwatchedGames(), this.fetchWatchedGames()]);
    },
    /**
     * Fetch the games that the user has watched
     */
    fetchWatchedGames() {
      const userId = this.$store.state.user.id;
      return WatchedGamesService.getWatchedGames(userId)
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
      return WatchedGamesService.getUnwatchedGames(userId)
        .then(response => {
          this.unwatchedGames = response.data;
        })
        .catch(error => {
          console.error('Error fetching unwatched games:', error);
        });
    },
  },
  mounted() {
    // Fetch games when the component is mounted
    this.fetchGames().then(() => {
      if (this.rounds.length > 0) {
        this.currentRound = this.rounds[0].toString();
        console.log("Set currentRound after fetching:", this.currentRound);
      }
    }).catch(error => console.error("Error in mounted hook:", error));
  },
};
</script>
  
<style scoped>

.header-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-right: 16px;
  background-color: var(--afl-800);
  border-radius: 8px;
  margin: auto;
}

h1 {
  margin: 0;
  flex-grow: 1;
  padding-left: 16px;
  border-radius: 8px;
  font-family: 'League Gothic', sans-serif;
  font-size: xxx-large;
  text-transform: uppercase;
  letter-spacing: 2px;
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
  background-color: var(--afl-800);
}

.games-section {
  background-color: var(--afl-800);
  margin: 10px auto;
  padding: 10px;
  border-radius: 8px;
}

.game-list {
  color: var(--afl-900);
  margin: auto;
}

h2 {
  color: var(--afl-200);
  margin-left: 8px;
}

.round-selection select {
  background-color: var(--afl-200);
  color: var(--afl-900);
  padding: 8px 10px;
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

.game-card-transition {
  transition: opacity 1s ease, visibility 1s ease;
  opacity: 1;
  visibility: visible
}

.game-card-hide {
  opacity: 0;
  visibility: hidden;
}

@media (max-width: 768px) {
  .games-container {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

</style>
  