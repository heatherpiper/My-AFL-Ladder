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
        <tr v-for="(teamLadder, index) in teamLadder" :key="teamLadder.id">
          <td>{{ index + 1 }}</td>
          <td>{{ teamLadder.name }}</td>
          <td>{{ teamLadder.percentage }}</td>
          <td>{{ teamLadder.points }}</td>
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
      teamLadder: []
    }
  },
  created() {
    LadderService.getLadder()
      .then(response => this.teamLadder = response.data)
      .catch(error => console.error('Error: ', error));
  }
}
</script>

<style>
</style>
  