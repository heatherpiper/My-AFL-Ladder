import axios from 'axios';

const API_URL = 'http://localhost:9000';

export default {
    addGamesToWatchedList(userId, gameIds) {
        return axios.post(`${API_URL}/users/${userId}/watched-games/watch`, { gameIds: [gameIds] });
    },

    removeGamesFromWatchedList(userId, gameIds) {
        return axios.post(`${API_URL}/users/${userId}/watched-games/unwatch`, { gameIds: [gameIds] });
    },

    getWatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games`);
    },

    getUnwatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games/unwatched`);
    }
}