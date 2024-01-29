import axios from 'axios';

export default {
    getAllMatches() {
        return axios.get('/matches/');
    },

    getMatchById(matchId) {
        return axios.get(`/matches/${matchId}`);
    }
}