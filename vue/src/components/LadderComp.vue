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
        <tr v-for="(team, index) in teams" :key="team.id">
          <td>{{ index + 1 }}</td>
          <td>{{ team.name }}</td>
          <td>{{ team.percentage }}</td>
          <td>{{ team.points }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'LadderComp',
  data() {
    return {
      teams: []
    };
  },
  mounted() {
    // Fetch the teams data when the component is mounted
    this.fetchTeams();
  },
  methods: {
    fetchTeams() {
      // Placeholder for fetching teams data
      // this.teams = fetchedData;
    }
  }
};
</script>

<style>


</style>
  