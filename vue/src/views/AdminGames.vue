<template>
    <div class="admin-games">
        <h1>Games Management</h1>

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
      games: []
    };
  },
  mounted() {
    this.fetchGames();
  },
  methods: {
    fetchGames() {
      GameService.getAllGames()
        .then(response => {
          this.games = response.data;
        })
        .catch(error => {
          console.error('Error fetching games:', error);
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
th, td {
  border: 1px solid black;
  padding: 8px;
  text-align: left;
}
</style>