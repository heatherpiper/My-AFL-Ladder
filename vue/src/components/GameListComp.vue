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
        <div v-if="filteredUnwatchedGames.length" class="games-container">
          <div class="game-card game-card-transition"
            v-for="game in filteredUnwatchedGames"
            :key="game.id"
            :class="{'game-card-hide': processingGames.includes(game.id)}"
            @click.stop="selectGame(game.id)"
            @mouseover="hover = game.id"
            @mouseleave="hover = null">
            <div class="game-text-container">
              <div class="team-name">{{ game.hteam }}</div>
              <div class="vs-container">
                <span class="vs-text">v.</span>
                <div class="team-name">{{ game.ateam }}</div>
              </div>
              <div v-if="game.complete === 100" class="complete-status" >Full time</div>
              <div v-else class="complete-status">Not yet played</div>
            </div>
            <div class="image-container"
                 @click.stop="selectGame(game.id)"
                 @mouseover="hover = game.id"
                 @mouseleave="hover = null">
              <img :src="getImageSrc(game.id)" alt="Watched check mark">
            </div>
          </div>
        </div>
        <div v-else class="no-games-message">
          No more unwatched games.
        </div>
      </div>

      <div class="section-container">
        <h2 class="section-header">Watched Games</h2>
        <div v-if="filteredWatchedGames.length" class="games-container">
          <div class="game-card game-card-transition" 
            v-for="game in filteredWatchedGames"
            :key="game.id"
            :class="{'game-card-hide': processingGames.includes(game.id)}"
            @click.stop="selectGame(game.id)"
            @mouseover="hover = game.id"
            @mouseleave="hover = null">

            <div class="game-text-container">
              <div class="team-name">
                  {{ game.hteam }}
                  <span v-if="game.winner === game.hteam">&#x2714;</span>
                </div>
              <div class="vs-container">
                <span class="vs-text">v.</span>
                <div class="team-name">
                {{ game.ateam }}
                <span v-if="game.winner === game.ateam">&#x2714;</span>
                </div>
              </div>
              <div class="game-score">{{ game.hscore }} - {{ game.ascore }}</div>
            </div>

            <div class="image-container"
                 @click.stop="selectGame(game.id)"
                 @mouseover="hover = game.id"
                 @mouseleave="hover = null">
              <img :src="getImageSrc(game.id, true)" alt="Action button">
            </div>
          </div>
        </div>
        <div v-else class="no-games-message">
          Click a game to mark it as watched.
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
    /**
     * The unwatched games for the selected round
     * @returns {Array} An array of unwatched games
     */
    filteredUnwatchedGames() {
      if (this.currentRound === null || this.currentRound === undefined) return [];
      const currentRoundNumber = Number(this.currentRound);
      return this.unwatchedGames.filter(game => Number(game.round) === currentRoundNumber);
    },
    /**
     * The watched games for the selected round
     * @returns {Array} An array of watched games
     */
    filteredWatchedGames() {
      if (this.currentRound === null || this.currentRound === undefined) return [];
      const currentRoundNumber = Number(this.currentRound);
      return this.watchedGames.filter(game => Number(game.round) === currentRoundNumber);
    }
  },
  watch: {
    currentRound(newValue) {
      localStorage.setItem('currentRound', newValue);
    }
  },
  methods: {
    determineDefaultRound() {
      let defaultRound = this.rounds[this.rounds.length - 1];

      const earliestUnwatchedRound = this.rounds.find(round =>
        this.unwatchedGames.some(game => Number(game.round) === round));
      
        if (earliestUnwatchedRound !== undefined) {
          defaultRound = earliestUnwatchedRound;
        }

      this.currentRound = defaultRound.toString();
    },
    /**
     * Get the image source for the action button
     * @param {String} gameId The ID of the game
     * @returns {String} The image source for the action button
     */
    getImageSrc(gameId) {
      const game = this.unwatchedGames.concat(this.watchedGames).find(game => game.id === gameId);
      const isHovering = this.hover === gameId;
      const isWatched = this.watchedGames.some(game => game.id === gameId);

      const isIncomplete = game && game.complete !== 100;

      if (isWatched) {
        return isHovering ? '/checkmark-hover.svg' : '/checkmark-filled.svg';
      } else {
        if (isHovering && !isIncomplete) {
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
      // Check if the game is incomplete; if so, do nothing
      const game = this.unwatchedGames.find(game => game.id === gameId);
      if (game && game.complete !== 100) {
        return;
      }

      // Determine if the game is currently marked as watched
      const isWatched = this.watchedGames.some(game => game.id === gameId);

      // Proceed with adding or removing game from the watched list
      const operation = isWatched ? 'remove' : 'add';
      this.processingGames.push(gameId);
      
      const serviceMethod = operation === 'add' ? WatchedGamesService.addGamesToWatchedList : WatchedGamesService.removeGamesFromWatchedList;
      
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
        this.hover = null;
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
     * Fetch all games, watched and unwatched
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
    const savedRound = localStorage.getItem('currentRound');
    this.fetchGames().then(() => {
      if (savedRound) {
        this.currentRound = savedRound;
      } else {
        this.determineDefaultRound();
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
  padding: 4px 12px;
  background-color: var(--afl-800);
  border-radius: 8px;
  margin: auto;
}

h1 {
  margin: 0;
  flex-grow: 1;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: xx-large;
  text-transform: uppercase;
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
  background-color: var(--afl-800);
}

.round-selection select {
  background-color: var(--afl-200);
  color: var(--afl-900);
  padding: 4px 8px;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
  font-size: x-large;
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
  padding: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  color: var(--afl-200);
  min-height: 5em;
}

.game-text-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  padding-left: 10px;
}

.vs-container {
  display: flex;
  align-items: center;
}

.team-name {
  margin: 4px 0;
  font-weight: 900;
}

.vs-text {
  margin-right: 8px;
  color: var(--afl-250);
  font-weight: bold;
}

.team-name span {
  color: #7EC466;
  margin-left: 4px;
}

.complete-status {
  margin-top: 8px;
  color: var(--afl-250);
  font-style: italic;
}

.game-score {
  margin-top: 8px;
  font-weight: bold;
  color: var(--afl-250);
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

.no-games-message {
  color: var(--afl-250);
  padding: 2rem;
  text-align: center;
  font-style: italic;
}

@media (max-width: 768px) {
  .games-container {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }

  h1 {
    font-size: x-large;
  }

}

</style>
  