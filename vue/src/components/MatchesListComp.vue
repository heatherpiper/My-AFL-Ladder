<template>
    <div>
      <h1>Match List</h1>
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
    name: "matches",
    data() {
      return {
        matches: [],
      };
    },
    mounted() {
        axios.get('/matches').then(response => {
            this.matches = response.data;
        })
        .catch(error => {
            console.error('Error fetching matches:', error);
        });
    },
  };
  </script>
  
  <style scoped>
  /* Add your component-specific styles here */
  </style>
  