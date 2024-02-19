<template>
    <div>
        <h1>Game Details</h1>
        <div v-if="game">
            <p><strong>Home Team:</strong> {{ game.hteam }}</p>
            <p><strong>Away Team:</strong> {{ game.ateam }}</p>
            <p><strong>Final score:</strong>
                <span v-if="game.complete === 100">{{  game.hscore }} to {{ game.ascore }}</span>
                <span v-else>-</span>
            </p>
        </div>
        <div v-else>
            <p>Loading...</p>
        </div>
    </div>
</template>

<script>
import GameService from '../services/GameService';

export default {
    data() {
        return {
            game: null,
        };
    },
    async created() {
        const gameId = this.$route.params.id;
        try {
            const response = await GameService.getGameById(gameId);
            this.game = response.data;
        } catch (error) {
            console.error('Error fetching game:', error);
        }
    },
};
</script>

<style scoped>

</style>