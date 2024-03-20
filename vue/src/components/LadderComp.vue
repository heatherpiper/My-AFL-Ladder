<template>
  <div class="ladder">
    <h1>Team Ladder</h1>
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
  font-family: 'Roboto', sans-serif;
  font-size: xx-large;
  color: var(--afl-200);
  text-shadow: 1px 1px 2px var(--afl-900);
  background-color: var(--afl-800);
  margin: auto;
  padding: 4px 12px;
  border-radius: 8px;
}

.ladder {
  margin: auto;
}

.table-container {
  background-color: var(--afl-800);
  margin: 10px auto;
  border-radius: 8px;
  padding: 10px;
  overflow: hidden;
}

table {
  border-collapse: collapse;
  width: 100%;
}

thead th {
  color: var(--afl-200);
  text-align: left;
  background-color: var(--afl-800);
  padding: 0.5rem;
  text-transform: uppercase;
}

tbody tr {
  background-color: var(--afl-600);
  color: var(--afl-200);
  line-height: 1rem;
}

td {
  padding: 0.4rem;
}

td:not(:last-child) {
  border-right: none;
}

.team-rank{
  font-weight: 900;
  background-color: var(--afl-800);
  border: 2px solid var(--afl-800);
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
  border: 2px solid var(--afl-500);
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

@media (max-width: 768px) {
  h1 {
    font-size: x-large;
  }

  .table-container {
    margin-bottom: 1.25rem;
  }

  thead th {
    padding: 0.4rem;
    font-size: small;
  }
  
  td {
    padding: 0.4rem;
  }

  .team-rank {
    width: 1.75rem;
    height: 1.75rem;
    font-weight: 700;
    margin-left: 0.25rem;
  }

  .team-name {
    font-weight: 700;
  }

  .points {
    font-weight: 700;
  }
}

</style>
  