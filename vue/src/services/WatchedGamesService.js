import axios from 'axios';

const API_URL = 'http://localhost:9000';

export default {
    addGameToWatchedList(userId, gameId) {
        return axios.post(`${API_URL}/users/${userId}/watched-games/watch/${gameId}`);
    },

    removeGameFromWatchedList(userId, gameId) {
        return axios.delete(`${API_URL}/users/${userId}/watched-games/unwatch/${gameId}`);
    },

    getWatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games`);
    },

    getUnwatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games/unwatched`);
    },

    markAllGamesWatched(userId) {
        return axios.post(`${API_URL}/users/${userId}/watched-games/watch/all`)
    },

    markAllGamesUnwatched(userId) {
        return axios.post(`${API_URL}/users/${userId}/watched-games/unwatch/all`)
    }
}