<template>
  <div class="ladder">
    <h1>LADDER</h1>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>
              <div class="header-rank"></div>
            </th>
            <th>
              <div class="header-team">Team</div>
            </th>
            <th>
              <div class="header-percentage">%</div>
            </th>
            <th>
              <div class="header-points">Points</div>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(ladderEntry, index) in teamLadder" :key="ladderEntry.teamId">
            <td>
              <div class="team-rank">{{ index + 1 }}</div>
            </td>
            <td>
              <div class="team-name">{{ ladderEntry.teamName }}</div>
            </td>
            <td>
              <div class="percentage">{{ ladderEntry.percentage }}</div>
            </td>
            <td>
              <div class="points">{{ ladderEntry.points }}</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
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

<style scoped>

h1 {
  padding-left: 10px;
}

.table-container {
  background-color: var(--afl-800);
  margin: 10px;
  border-radius: 8px;
  padding: 10px;
  overflow: hidden;
}

table {
  border-collapse: separate;
  border-spacing: 0;
  width: 100%;
}

thead th {
  color: var(--afl-200);
  text-align: left;
  background-color: var(--afl-800);
  padding: 0.8rem;
  text-transform: uppercase;
}

tbody tr {
  margin: 0;
  padding: 0;
  background-color: var(--afl-700);
  color: var(--afl-200);
  line-height: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
}

td {
  padding: 1rem;
}

td:not(:last-child) {
  border-right: none;
}

.team-rank{
  font-weight: 900;
  background-color: var(--afl-500);
  border-radius: 8px;
  padding: 0.2rem;
  text-align: center;
  margin: 2px;
}

.team-name {
  font-weight: 900;
}

.percentage {
  text-align: left;
}

.points {
  font-weight: 900;
  text-align: center;
}

</style>
  