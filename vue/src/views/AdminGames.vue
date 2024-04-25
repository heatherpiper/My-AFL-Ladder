<template>
    <div class="admin-games">
        <h1>Games Management</h1>
        <div class="round-selection">
            <select v-model="currentRound" @change="fetchGamesByRound">
                <option disabled value="">Choose a round</option>
                <option v-for="round in rounds" :key="round" :value="round">
                    Round {{ round }}
                </option>
            </select>
        </div>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Round</th>
                    <th>Year</th>
                    <th>Date</th>
                    <th>Home Team</th>
                    <th>Away Team</th>
                    <th>Home Score</th>
                    <th>Away Score</th>
                    <th>Winner</th>
                    <th>Complete</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="game in games" :key="game.id">
                    <td>{{ game.id }}</td>
                    <td>{{ game.round }}</td>
                    <td>{{ game.year }}</td>
                    <td>{{ game.date }}</td>
                    <td>{{ game.hteam }}</td>
                    <td>{{ game.ateam }}</td>
                    <td>{{ game.hscore }}</td>
                    <td>{{ game.ascore }}</td>
                    <td>{{ game.winner }}</td>
                    <td>{{ game.complete }}</td>
                </tr>
            </tbody>
        </table>

    </div>
</template>

<script>
import GameService from '@/services/GameService';

export default {
    data() {
        return {
            games: [],
            currentRound: '',
            allRounds: []
        };
    },
    computed: {
        rounds() {
            return this.allRounds;
        }
    },
    mounted() {
        this.fetchAllGames();
    },
    methods: {
        fetchAllGames() {
            GameService.getAllGames()
                .then(response => {
                    this.games = response.data.sort((a, b) => new Date(a.date) - new Date(b.date));
                    this.allRounds = Array.from(new Set(this.games.map(game => game.round))).sort((a, b) => a - b);
                })
                .catch(error => {
                    console.error('Error fetching games:', error);
                });
        },
        fetchGamesByRound() {
            GameService.getGamesByRound(this.currentRound)
                .then(response => {
                    this.games = response.data.sort((a, b) => new Date(a.date) - new Date(b.date));
                })
                .catch(error => {
                    console.error('Error fetching games by round:', error);
                });
        }
    }
}
</script>

<style>
table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid black;
    padding: 8px;
    text-align: left;
}
</style>