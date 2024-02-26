<template>
  <div class="ladder">
    <h1>Current Ladder</h1>
    <table>
      <thead>
        <tr>
          <th>Rank</th>
          <th>Team</th>
          <th>Percentage</th>
          <th>Points</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(ladderEntry, index) in teamLadder" :key="ladderEntry.teamId">
          <td>{{ index + 1 }}</td>
          <td>{{ ladderEntry.teamName }}</td>
          <td>{{ ladderEntry.percentage }}</td>
          <td>{{ ladderEntry.points }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import LadderService from '../services/LadderService';

export default {
  name: 'LadderComp',
  props: ['userId'],
  data() {
    return {
      teamLadder: [],
      error: null,
    };
  },
  created() {
    this.fetchLadder();
  },
  methods: {
    fetchLadder() {
      LadderService.getLadder(this.userId)
        .then(response => {
          console.log('Fetched ladder data:', response.data)
          this.teamLadder = response.data;
        })
        .catch(error => {
          this.error = error;
          console.error('Error fetching ladder data:', error);
        });
    }
  }
}
</script>

<style>
</style>
  