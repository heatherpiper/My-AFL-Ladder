import axios from 'axios';

export default {
    getAllGames() {
        return axios.get('/matches/');
    },

    getMatchById(gameId) {
        return axios.get(`/matches/${gameId}`);
    }
}