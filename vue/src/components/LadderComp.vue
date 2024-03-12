<template>
  <div class="ladder">
    <h1>Ladder</h1>
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
              <div class="team-rank" :class="{ 'highlight-rank': index < 8 }">{{ index + 1 }}</div>
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
/**
 * LadderComp component
 * @component
 */
import LadderService from '../services/LadderService';

export default {
  name: 'LadderComp',
  /**
   * Props
   * @prop {number} userId - The user id
   */
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
    /**
     * Fetch ladder data from the server
     */
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
  font-family: 'League Gothic', sans-serif;
  font-size: xxx-large;
  text-transform: uppercase;
  letter-spacing: 2px;
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
  padding-left: 10px;
}

.table-container {
  background-color: var(--afl-800);
  margin: 10px auto;
  border-radius: 8px;
  padding: 10px;
  overflow: hidden;
  width: 80%;
}

table {
  border-collapse: collapse;
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
  background-color: var(--afl-700);
  color: var(--afl-200);
  line-height: 1rem;
}

td {
  padding: 0.5rem;
}

td:not(:last-child) {
  border-right: none;
}

.team-rank{
  font-weight: 900;
  background-color: var(--afl-500);
  border: 2px solid var(--afl-500);
  border-radius: 8px;
  text-align: center;
  margin-left: 0.5rem;
  width: 2rem;
  height: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
  
}

.highlight-rank {
  border: 2px solid var(--afl-400);
}

tbody tr:nth-child(8) {
  border-bottom: 4px solid var(--afl-800);
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
  