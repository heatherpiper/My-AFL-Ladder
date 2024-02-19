<template>
    <div>
      <h1>Matches</h1>
      <ul>
        <li v-for="match in matches" :key="match.id">
          {{ match.team1 }} vs {{ match.team2 }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  import axios from 'axios';

  export default {
    name: "games",
    data() {
      return {
        games: [],
      };
    },
    mounted() {
        axios.get('/games').then(response => {
            this.games = response.data;
        })
        .catch(error => {
            console.error('Error fetching games:', error);
        });
    },
  };
</script>
  
<style scoped>

</style>
  