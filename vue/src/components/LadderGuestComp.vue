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
    
  export default {
    name: 'LadderGuestComp',
    props: ['userId'],
    data() {
      return {
        teamLadder: [],
        error: null,
      };
    },
    created() {
      let ladderData = sessionStorage.getItem('ladderData');
      if (!ladderData) {
        ladderData = this.getDefaultLadderData();
        sessionStorage.setItem('ladderData', JSON.stringify(ladderData));
      } else {
        ladderData = JSON.parse(ladderData);
      }
      this.teamLadder = ladderData;
    },
    methods: {
        calculateLadder() {
            // Initialize ladder with default data
            let updatedLadder = this.getDefaultLadderData().map(team => ({
                ...team,
                pointsFor: 0,
                pointsAgainst: 0,
            }));

            const gamesData = JSON.parse(sessionStorage.getItem('gamesData') || '[]');
            
            gamesData.filter(game => game.watched).forEach(game => {
                const homeTeam = updatedLadder.find(team => team.teamName === game.hteam);
                const awayTeam = updatedLadder.find(team => team.teamName === game.ateam);

                // Update pointsFor and pointsAgainst
                homeTeam.pointsFor += game.hscore;
                homeTeam.pointsAgainst += game.ascore;
                awayTeam.pointsFor += game.ascore;
                awayTeam.pointsAgainst += game.hscore;

                // Update points based on game outcome
                if (game.winner === game.hteam) {
                    homeTeam.points += 4;
                } else if (game.winner === game.ateam) {
                    awayTeam.points += 4;
                } else {
                    homeTeam.points += 2;
                    awayTeam.points += 2;
                }
            });

            // Recalculate percentage
            updatedLadder.forEach(team => {
                team.percentage = team.pointsAgainst === 0 ? 100 : (team.pointsFor / team.pointsAgainst * 100).toFixed(2);
            });

            // Sort updatedLadder by points, then percentage
            updatedLadder.sort((a, b) => b.points - a.points || b.percentage - a.percentage);

            this.teamLadder = updatedLadder;
        },
        getDefaultLadderData() {
            return [
                { teamId: 1, teamName: 'Adelaide', percentage: 100, points: 0 },
                { teamId: 2, teamName: 'Brisbane Lions', percentage: 100, points: 0},
                { teamId: 3, teamName: 'Carlton', percentage: 100, points: 0},
                { teamId: 4, teamName: 'Collingwood', percentage: 100, points: 0},
                { teamId: 5, teamName: 'Essendon', percentage: 100, points: 0},
                { teamId: 6, teamName: 'Fremantle', percentage: 100, points: 0},
                { teamId: 7, teamName: 'Geelong', percentage: 100, points: 0},
                { teamId: 8, teamName: 'Gold Coast', percentage: 100, points: 0},
                { teamId: 9, teamName: 'Greater Western Sydney', percentage: 100, points: 0},
                { teamId: 10, teamName: 'Hawthorn', percentage: 100, points: 0},
                { teamId: 11, teamName: 'Melbourne', percentage: 100, points: 0},
                { teamId: 12, teamName: 'North Melbourne', percentage: 100, points: 0},
                { teamId: 13, teamName: 'Port Adelaide', percentage: 100, points: 0},
                { teamId: 14, teamName: 'Richmond', percentage: 100, points: 0},
                { teamId: 15, teamName: 'St Kilda', percentage: 100, points: 0},
                { teamId: 16, teamName: 'Sydney', percentage: 100, points: 0},
                { teamId: 17, teamName: 'West Coast', percentage: 100, points: 0},
                { teamId: 18, teamName: 'Western Bulldogs', percentage: 100, points: 0}
            ];
        },
    }
  }
  </script>
  
  <style scoped>
  
  h1 {
    font-family: 'Roboto', sans-serif;
    font-size: xx-large;
    text-transform: uppercase;
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
    