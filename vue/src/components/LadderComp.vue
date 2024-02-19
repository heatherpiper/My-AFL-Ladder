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
export default {
  name: 'LadderComp',
  props: ['userId'],
  data() {
    return {
      teamLadder: []
    }
  },
  methods: {
    fetchTeamLadder() {
      fetch(`http://localhost:8080/ladder/${this.$store.state.user.id}`)
      .then(response => response.json())
      .then(data => this.teamLadder = data)
      .catch(error => console.error('Error: ', error));
    }
  },
  created() {
    this.fetchTeamLadder();
  }
}
</script>

<style>


</style>
  